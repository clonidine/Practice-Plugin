package dev.mig.practice.util;

import com.databridge.mig.repository.Repository;
import dev.mig.practice.api.model.fighter.Fighter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class PlayerUtils {

    public static Optional<Fighter> getFighterByName(Repository<Fighter> fighterRepository, String playerName) {

        OfflinePlayer offlinePlayerToFind = Bukkit.getOfflinePlayer(playerName);

        UUID playerUuid = offlinePlayerToFind.getUniqueId();

        return fighterRepository.findOne("id", String.valueOf(playerUuid));
    }
}
