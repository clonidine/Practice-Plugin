package dev.mig.practice.command.subcommand;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    List<String> getAliases();

    boolean execute(CommandSender sender, String[] args);
}
