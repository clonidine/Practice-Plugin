package dev.mig.practice.model;

import dev.mig.practice.api.model.fighter.rank.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
@Setter
public final class RankImpl implements Rank {

    private String name;
    private ChatColor color;
    private String format;
}
