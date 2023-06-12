package dev.mig.practice.command.subcommand.impl.duel;

import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.duel.DuelInvite;
import dev.mig.practice.api.model.match.Match;
import dev.mig.practice.command.subcommand.SubCommand;
import dev.mig.practice.command.usage.CommandUsage;
import dev.mig.practice.event.DuelAcceptedEvent;
import dev.mig.practice.manager.DuelInviteManager;
import dev.mig.practice.manager.MatchManager;
import dev.mig.practice.model.MatchImpl;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class DuelAcceptSubCommand implements SubCommand, CommandUsage {

    private final MatchManager matchManager;
    private final DuelInviteManager duelInviteManager;

    public DuelAcceptSubCommand(PracticePlugin plugin) {
        matchManager = plugin.getMatchManager();
        duelInviteManager = plugin.getDuelInviteObjectManager();
    }

    @Override
    public String getUsage() {
        return ChatColor.YELLOW + "/duel accept <player>";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("accept");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        final boolean sufficientArgs = args.length == 2;

        if (!sufficientArgs) {
            MessageUtils.send(sender, getUsage());
            return false;
        }

        final Player playerAcceptingDuel = (Player) sender;

        boolean inMatch = matchManager.isInMatch(playerAcceptingDuel.getUniqueId());

        if (inMatch) {
            MessageUtils.send(sender, "&cYou must first finish your match to accept a dueling invitation.");
            return false;
        }

        final String playerSendingDuelName = args[1];

        final Player playerSendingDuel = Bukkit.getPlayer(playerSendingDuelName);

        final Optional<DuelInvite> duelInviteToFind = duelInviteManager.getInvite(playerSendingDuel, playerAcceptingDuel);

        if (!duelInviteToFind.isPresent()) {
            final String inviteNotFoundMessage = String.format("&cThe player &e'%s' &cdidn't invite you to a duel", playerSendingDuel.getName());

            MessageUtils.send(sender, inviteNotFoundMessage);

            return false;
        }

        final DuelInvite duelInvite = duelInviteToFind.get();

        final Arena arena = duelInvite.getArena();

        final Match match = new MatchImpl(arena);

        final World world = playerSendingDuel.getWorld();

        match.getFighters().add(playerAcceptingDuel);
        match.getFighters().add(playerSendingDuel);

        duelInviteManager.getDuelInvites().remove(duelInvite);

        MessageUtils.send(sender, "&aMatch starting...");
        MessageUtils.send(playerSendingDuel, "&aMatch starting...");

        Bukkit.getServer().getPluginManager().callEvent(new DuelAcceptedEvent(match, world));

        return false;
    }
}
