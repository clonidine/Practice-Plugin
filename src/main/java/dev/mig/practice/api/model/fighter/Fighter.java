package dev.mig.practice.api.model.fighter;

import dev.mig.practice.api.model.fighter.rank.Rank;
import dev.mig.practice.api.model.fighter.stats.Statistics;

import java.util.UUID;

public interface Fighter {

    UUID getUuid();

    Statistics getStats();

    Rank getRank();
}
