package dev.mig.practice.model;

import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.fighter.rank.Rank;
import dev.mig.practice.api.model.fighter.stats.Statistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class FighterImpl implements Fighter {

    private final UUID uuid;
    private Statistics stats;
    private Rank rank;

    @Override
    public Rank getRank() {
        return rank;
    }
}
