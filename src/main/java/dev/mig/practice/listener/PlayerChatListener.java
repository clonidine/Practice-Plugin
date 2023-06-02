package dev.mig.practice.listener;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.fighter.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;
import java.util.UUID;

public class PlayerChatListener implements Listener {

    private final Repository<Fighter> fighterRepository;

    public PlayerChatListener(PracticePlugin plugin) {
        fighterRepository = plugin.getFighterRepository();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        final Player player = event.getPlayer();

        final UUID playerUuid = player.getUniqueId();

        final String playerMessage = event.getMessage();

        final Optional<Fighter> optionalFighter = fighterRepository.findOne("id", playerUuid.toString());

        optionalFighter.ifPresent(fighter -> {
            final String playerName = player.getName();

            final Rank rank = fighter.getRank();

            final String rankFormat = rank.getFormat();

            final ChatColor rankColor = rank.getColor();

            final String formattedMessage = String.format("%s %s: %s", rankColor + rankFormat, playerName, ChatColor.WHITE + playerMessage);

            Bukkit.getServer().getOnlinePlayers().forEach(bukkitPlayer -> bukkitPlayer.sendMessage(formattedMessage));
        });

    }
}
