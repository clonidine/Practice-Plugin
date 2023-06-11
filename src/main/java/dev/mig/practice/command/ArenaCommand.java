package dev.mig.practice.command;

import dev.mig.practice.PracticePlugin;
import dev.mig.practice.command.subcommand.SubCommand;
import dev.mig.practice.command.subcommand.impl.ArenaCreateSubCommand;
import dev.mig.practice.command.subcommand.impl.ArenaInfoSubCommand;
import dev.mig.practice.command.subcommand.impl.ArenaSetPositionSubCommand;
import dev.mig.practice.command.usage.CommandUsage;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class ArenaCommand implements CommandExecutor, CommandUsage {

    private final List<SubCommand> subCommands = new ArrayList<>();

    public ArenaCommand(PracticePlugin plugin) {

        subCommands.add(new ArenaCreateSubCommand(plugin));
        subCommands.add(new ArenaSetPositionSubCommand(plugin));
        subCommands.add(new ArenaInfoSubCommand(plugin));

        plugin.getCommand("arena").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        final boolean player = sender instanceof Player;

        if (!player) {
            MessageUtils.send(sender, "&cOnly players can execute this command.");
            return false;
        }

        final boolean operator = sender.isOp();

        if (!operator) {
            MessageUtils.send(sender, "&cUnfortunately, you don't have sufficient permission to execute this command.");
            return false;
        }

        final boolean sufficientArgs = args.length > 0;

        if (!sufficientArgs) {
            final String commandUsage = getUsage();

            MessageUtils.send(sender, commandUsage);

            return false;
        }

        for (SubCommand subCommand : subCommands) {
            final boolean validSubCommand = subCommand.getAliases().contains(args[0]);

            if (validSubCommand) {
                return subCommand.execute(sender, args);
            }
        }

        return false;
    }

    @Override
    public String getUsage() {

        return ChatColor.YELLOW +
                "/arena create <name>\n" +
                "/arena delete <name>\n" +
                "/arena setposition <name>";
    }
}
