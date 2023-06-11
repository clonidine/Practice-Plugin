package dev.mig.practice.command;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.fighter.stats.Statistics;
import dev.mig.practice.command.usage.CommandUsage;
import dev.mig.practice.util.MessageUtils;
import dev.mig.practice.util.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.UUID;

public final class FighterStatisticsCommand implements CommandExecutor, CommandUsage {

    private final Repository<Fighter> fighterRepository;

    public FighterStatisticsCommand(PracticePlugin plugin) {
        fighterRepository = plugin.getFighterRepository();

        plugin.getCommand("statistics").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {
            final String commandUsage = getUsage();

            sender.sendMessage(commandUsage);

            return false;
        }

        final String playerName = args[0];

        final Optional<Fighter> fighterToFind = PlayerUtils.getFighterByName(fighterRepository, playerName);

        final boolean hasFound = fighterToFind.isPresent();

        if (!hasFound) {

            final String fighterNotFoundMessage = "&cThis fighter doesn't exist in our server.";

            MessageUtils.send(sender, fighterNotFoundMessage);

            return false;
        }

        final Fighter fighter = fighterToFind.get();

        final UUID fighterUuid = fighter.getUuid();

        final Statistics fighterStats = fighter.getStats();

        final int fighterKills = fighterStats.getKills();
        final int fighterDeaths = fighterStats.getDeaths();

        final float fighterKdr = fighterStats.getKdr();

        MessageUtils.send(sender, "&e" + playerName + "'s statistics:");
        MessageUtils.send(sender, "&aUUID: " + fighterUuid);
        MessageUtils.send(sender, "&aKills: " + fighterKills);
        MessageUtils.send(sender, "&aDeaths: " + fighterDeaths);
        MessageUtils.send(sender, "&aKdr: " + fighterKdr);

        return false;
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /stats <player>";
    }
}
