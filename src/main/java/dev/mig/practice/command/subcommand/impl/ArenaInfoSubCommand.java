package dev.mig.practice.command.subcommand.impl;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.command.subcommand.SubCommand;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ArenaInfoSubCommand implements SubCommand {

    private final Repository<Arena> arenaRepository;

    public ArenaInfoSubCommand(PracticePlugin plugin) {
        arenaRepository = plugin.getArenaRepository();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("info", "information");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        final boolean sufficientArgs = args.length == 2;

        if (!sufficientArgs) {
            MessageUtils.send(sender, "&e/arena info <name>");

            return false;
        }

        final String arenaName = args[1];

        final Optional<Arena> arenaToFind = arenaRepository.findOne("Name", arenaName);

        final boolean arenaPresent = arenaToFind.isPresent();

        if (!arenaPresent) {
            final String arenaNotFoundMessage = String.format("The arena '%s' was not found. Please make sure that you have entered the arena name correctly.", arenaName);

            MessageUtils.send(sender, "&c" + arenaNotFoundMessage);

            return false;
        }

        final Arena arena = arenaToFind.get();

        MessageUtils.send(sender, "&eArena name: &a" + arenaName);
        MessageUtils.send(sender, "&eActive: " + (arena.isActive() ? "&a" : "&c") + arena.isActive());
        MessageUtils.send(sender, "&ePositions: &a" + arena.getPositions().size());

        return false;
    }
}
