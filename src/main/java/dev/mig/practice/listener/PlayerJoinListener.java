package dev.mig.practice.listener;


import com.databridge.mig.repository.Repository;
import dev.mig.practice.PracticePlugin;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.fighter.rank.Rank;
import dev.mig.practice.api.model.fighter.stats.Statistics;
import dev.mig.practice.model.FighterImpl;
import dev.mig.practice.model.RankImpl;
import dev.mig.practice.util.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;
import java.util.UUID;

public final class PlayerJoinListener implements Listener {

    private final Repository<Fighter> fighterRepository;
    private static final Rank DEFAULT_RANK = new RankImpl("Default", ChatColor.GREEN, "[Default]");
    private static final Statistics DEFAULT_STATISTICS = new Statistics(0, 0, 0.0F);

    public PlayerJoinListener(PracticePlugin plugin) {

        fighterRepository = plugin.getFighterRepository();

        final Repository<Rank> rankRepository = plugin.getRankRepository();

        final Optional<Rank> defaultRankOptional = rankRepository.findOne("Name", DEFAULT_RANK.getName());

        final boolean defaultRankPresent = defaultRankOptional.isPresent();
        
        if (!defaultRankPresent) {
            rankRepository.save(DEFAULT_RANK);
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final UUID playerUuid = player.getUniqueId();

        final Optional<Fighter> fighterOptional = fighterRepository.findOne("id", String.valueOf(playerUuid));
        final boolean fighterFound = fighterOptional.isPresent();

        if (!fighterFound) {

            final Fighter fighter = new FighterImpl(playerUuid, DEFAULT_STATISTICS, DEFAULT_RANK);

            fighterRepository.save(fighter);
        }
    }
}
