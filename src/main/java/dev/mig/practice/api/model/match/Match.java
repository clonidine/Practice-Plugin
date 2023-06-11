package dev.mig.practice.api.model.match;

import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.spectator.Spectator;

import java.util.List;

public interface Match {

    List<Fighter> getFighters();

    List<Spectator> getSpectators();

    Arena getArena();
}
