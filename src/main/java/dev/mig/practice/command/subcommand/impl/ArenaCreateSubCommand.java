package dev.mig.practice.command.subcommand.impl;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.command.subcommand.SubCommand;
import dev.mig.practice.model.ArenaImpl;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class ArenaCreateSubCommand implements SubCommand {

    private final Repository<Arena> arenaRepository;

    public ArenaCreateSubCommand(PracticePlugin plugin) {
        arenaRepository = plugin.getArenaRepository();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("create");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        final boolean sufficientArgs = args.length == 2;

        if (!sufficientArgs) {
            MessageUtils.send(sender, "&e/arena create <name>");

            return false;
        }

        final String arenaName = args[1];

        final Optional<Arena> arenaToFind = arenaRepository.findOne("Name", arenaName);

        final boolean arenaPresent = arenaToFind.isPresent();

        if (arenaPresent) {
            MessageUtils.send(sender, "&cHey, this arena already exists!");
            return false;
        }

        final Arena arena = new ArenaImpl(arenaName, false);

        final boolean success = arenaRepository.save(arena);

        if (!success) {
            MessageUtils.send(sender, "&cA problem occurred when trying to create this arena. Please check the console log");

            return false;
        }

        final String successMessage = String.format("Arena %s successfully created!", arenaName);

        MessageUtils.send(sender, ChatColor.GREEN + successMessage);

        return false;
    }
}
