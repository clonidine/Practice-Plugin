package dev.mig.practice.command;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.duel.DuelInvite;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.command.subcommand.SubCommand;
import dev.mig.practice.command.subcommand.impl.duel.DuelAcceptSubCommand;
import dev.mig.practice.command.usage.CommandUsage;
import dev.mig.practice.manager.DuelInviteManager;
import dev.mig.practice.manager.MatchManager;
import dev.mig.practice.model.DuelInviteImpl;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class DuelCommand implements CommandExecutor, CommandUsage {

    private final Repository<Arena> arenaRepository;
    private final Repository<Fighter> fighterRepository;
    private final DuelInviteManager duelInviteManager;
    private final MatchManager matchManager;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public DuelCommand(PracticePlugin plugin) {
        duelInviteManager = plugin.getDuelInviteObjectManager();
        matchManager = plugin.getMatchManager();

        arenaRepository = plugin.getArenaRepository();
        fighterRepository = plugin.getFighterRepository();

        subCommands.add(new DuelAcceptSubCommand(plugin));

        plugin.getCommand("duel").setExecutor(this);
    }

    @Override
    public String getUsage() {
        return ChatColor.YELLOW
                + "/duel <player> <arena>\n"
                + "/duel accept <player>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            MessageUtils.send(sender, "&cOnly players can execute this command.");
            return false;
        }

        if (args.length == 0) {
            MessageUtils.send(sender, getUsage());
            return false;
        }

        else if (isSubCommand(args)) {

            for (SubCommand subCommand : subCommands) {
                final boolean validSubCommand = subCommand.getAliases().contains(args[0]);

                if (validSubCommand) {
                    return subCommand.execute(sender, args);
                }
            }
        }

        else {
            sendDuelInvite(sender, args);
        }

        return false;
    }

    private void sendDuelInvite(CommandSender sender, String[] args) {

        final Player inviteSender = (Player) sender;

        boolean inMatch = matchManager.isInMatch(inviteSender.getUniqueId());

        if (inMatch) {
            MessageUtils.send(sender, "&cYou need to finish your fight before you can invite a player to a duel.");
            return;
        }

        final String targetName = args[0];

        final Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null) {
            MessageUtils.send(sender, "&cThis player is offline.");
            return;
        }

        final Optional<Fighter> fighterSenderToFind = fighterRepository.findOne("Id", inviteSender.getUniqueId().toString());
        final Optional<Fighter> fighterTargetToFind = fighterRepository.findOne("Id", targetPlayer.getUniqueId().toString());

        if (!fighterSenderToFind.isPresent() || !fighterTargetToFind.isPresent()) {
            MessageUtils.send(sender, "&cAn unexpected error occurred. Please contact the administrators.");
            return;
        }

        inMatch = matchManager.isInMatch(targetPlayer.getUniqueId());

        if (inMatch) {
            MessageUtils.send(sender, "&cThis player is dueling. Please wait for him to finish.");
            return;
        }

        final Optional<DuelInvite> inviteToFind = duelInviteManager.getInvite(inviteSender, targetPlayer);

        final boolean hasFoundInvite = inviteToFind.isPresent();

        final String timeFormatted;

        if (hasFoundInvite) {

            final DuelInvite duelInvite = inviteToFind.get();

            final boolean isInCooldown = duelInviteManager.getCooldownMap().isInCooldown(duelInvite);

            if (!isInCooldown) {
                duelInviteManager.getDuelInvites().remove(duelInvite);
            } else {
                timeFormatted = duelInviteManager.getCooldownMap().getRemainingTimeFormatted(duelInvite);

                final String foundInviteMessage = String.format("&cYou have already sent an invitation to &e'%s' &crecently. Please wait: &e%s", targetPlayer.getName(), timeFormatted);

                MessageUtils.send(sender, foundInviteMessage);

                return;
            }
        }

        final String arenaName = args[1];

        final Optional<Arena> arenaToFind = arenaRepository.findOne("Name", arenaName);

        if (!arenaToFind.isPresent()) {
            final String arenaNotFoundMessage = String.format("Unfortunately, the arena '%s' was not found. Please make sure you have entered the arena name correctly.", arenaName);

            MessageUtils.send(sender, "&c" + arenaNotFoundMessage);

            return;
        }

        final Arena arena = arenaToFind.get();

        if (!arena.isActive()) {
            MessageUtils.send(sender, "&cThis arena has been found but is currently not active.");
            return;
        }

        final Fighter fighterOne = fighterSenderToFind.get();
        final Fighter fighterTwo = fighterTargetToFind.get();

        final DuelInvite duelInvite = new DuelInviteImpl(fighterOne, fighterTwo, new Date(), arena);

        final long time = 1000 * 60 * 5L;

        duelInviteManager.getCooldownMap().put(duelInvite, time);
        duelInviteManager.getDuelInvites().add(duelInvite);

        MessageUtils.send(sender, "&aThe dueling invitation has been sent successfully. The player will have 5 minutes to accept it.");

        timeFormatted = duelInviteManager.getCooldownMap().getRemainingTimeFormatted(duelInvite);

        final String targetInviteMessage = String.format("&aYou have received an invitation from &e'%s' &aand have &e%s &ato accept.", inviteSender.getName(), timeFormatted);

        MessageUtils.send(targetPlayer, targetInviteMessage);
    }

    private boolean isSubCommand(String[] args) {

        for (SubCommand subCommand : subCommands) {
            final boolean validSubCommand = subCommand.getAliases().contains(args[0]);

            if (validSubCommand) {
                return true;
            }
        }

        return false;
    }
}
