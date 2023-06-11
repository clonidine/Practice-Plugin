package dev.mig.practice.api.model.match;

import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.fighter.Fighter;

import java.util.List;

public interface Match {

    List<Fighter> getFighters();

    List<Fighter> getSpectators();

    Arena getArena();
}
