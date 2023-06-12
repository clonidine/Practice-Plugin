package dev.mig.practice.listener;

import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.arena.positions.Position;
import dev.mig.practice.api.model.match.Match;
import dev.mig.practice.event.DuelAcceptedEvent;
import dev.mig.practice.manager.MatchManager;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class DuelAcceptedListener implements Listener {

    private final MatchManager matchManager;

    public DuelAcceptedListener(PracticePlugin plugin) {
        matchManager = plugin.getMatchManager();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAccept(DuelAcceptedEvent event) {

        final Match match = event.getMatch();

        final Arena arena = match.getArena();

        if (match.getFighters().size() > arena.getPositions().size()) {
            match.getFighters().forEach(player -> MessageUtils.send(player, "&cThis arena does not have enough positions for these players"));

            event.setCancelled(true);
        }

        final World world = event.getWorld();

        match.getFighters().stream()
                .filter(player -> !player.getGameMode().equals(GameMode.SURVIVAL))
                .forEach(player -> player.setGameMode(GameMode.SURVIVAL));

        int index = 0;

        while (index < match.getFighters().size()) {

            final Position position = arena.getPositions().get(index);

            final Location location = new Location(world, position.getX(), position.getY(), position.getZ(), position.getYaw(), position.getPitch());

            final Player player = match.getFighters().get(index);

            player.teleport(location);

            matchManager.getMatchMap().put(player.getUniqueId(), match);

            index++;
        }
    }
}
