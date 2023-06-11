package dev.mig.practice.command.subcommand.impl;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.command.subcommand.SubCommand;
import dev.mig.practice.command.usage.CommandUsage;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class ArenaActivateSubCommand implements SubCommand, CommandUsage {

    private final Repository<Arena> arenaRepository;
    private static final int MINIMUM_POSITIONS = 2;

    public ArenaActivateSubCommand(PracticePlugin plugin) {
        arenaRepository = plugin.getArenaRepository();
    }

    @Override
    public String getUsage() {
        return ChatColor.YELLOW + "/arena activate <name>";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("activate");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        final boolean sufficientArgs = args.length == 2;

        if (!sufficientArgs) {
            MessageUtils.send(sender, getUsage());
            return false;
        }

        final String arenaName = args[1];

        final Optional<Arena> arenaToFind = arenaRepository.findOne("Name", arenaName);

        if (!arenaToFind.isPresent()) {
            MessageUtils.send(sender, "&cThis arena doesn't exist");

            return false;
        }

        final Arena arena = arenaToFind.get();

        if (arena.isActive()) {
            MessageUtils.send(sender, "&cThe arena '" + arenaName + "' is already active");
            return false;
        }

        final int positions = arena.getPositions().size();

        if (positions < MINIMUM_POSITIONS) {

            final int missingPositions = MINIMUM_POSITIONS - positions;

            final String activateArenaError = String.format("&cA few positions are missing to activate the &e'%s' &carena: &e%d", arenaName, missingPositions);

            MessageUtils.send(sender, activateArenaError);

            return false;
        }

        final boolean successfullySaved = arenaRepository.update("Name", "Active", arenaName, "true");

        if (!successfullySaved) {
            MessageUtils.send(sender, "&cAn unexpected error has occurred. Please check the console log");
            return false;
        }

        final String successMessage = String.format("You've successfully activated arena '%s'", arenaName);

        MessageUtils.send(sender, "&a" + successMessage);

        return false;
    }
}