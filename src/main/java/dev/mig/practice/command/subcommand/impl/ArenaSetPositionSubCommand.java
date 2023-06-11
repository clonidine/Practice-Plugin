package dev.mig.practice.command.subcommand.impl;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.arena.positions.Position;
import dev.mig.practice.command.subcommand.SubCommand;
import dev.mig.practice.command.usage.CommandUsage;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ArenaSetPositionSubCommand implements SubCommand, CommandUsage {

    private final Repository<Arena> arenaRepository;
    private final Repository<Position> positionRepository;

    public ArenaSetPositionSubCommand(PracticePlugin plugin) {
        arenaRepository = plugin.getArenaRepository();
        positionRepository = plugin.getPositionsRepository();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("setposition");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        final boolean sufficientArgs = args.length == 2;

        if (!sufficientArgs) {
            final String usageMessage = getUsage();

            MessageUtils.send(sender, usageMessage);

            return false;
        }

        final String arenaName = args[1];

        final Optional<Arena> arenaToFind = arenaRepository.findOne("Name", arenaName);

        boolean arenaPresent = arenaToFind.isPresent();

        if (!arenaPresent) {
            final String arenaNotFoundMessage = String.format("The arena '%s' was not found. Please make sure that you have entered the arena name correctly.", arenaName);

            MessageUtils.send(sender, "&c" + arenaNotFoundMessage);

            return false;
        }

        final Player player = (Player) sender;

        final Location playerLocation = player.getLocation();

        final Position position = new Position(arenaName, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());

        final boolean saved = positionRepository.save(position);

        if (!saved) {
            MessageUtils.send(sender, "&cAn unexpected problem occurred when trying to save the position of your arena. Please check the console.");
            return false;
        }

        final Arena arena = arenaToFind.get();

        final int arenaPositionsSize = arena.getPositions().size();

        final String successMessage = String.format("You have successfully set the %dst position of the arena %s", arenaPositionsSize + 1, arenaName);

        MessageUtils.send(sender, "&a" + successMessage);

        return false;
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "/arena setposition <name>";
    }
}
