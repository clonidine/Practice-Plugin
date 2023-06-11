package dev.mig.practice.model;

import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.fighter.Fighter;
import dev.mig.practice.api.model.match.Match;
import dev.mig.practice.api.model.spectator.Spectator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public final class MatchImpl implements Match {

    private final List<Fighter> fighters = new ArrayList<>();
    private final List<Spectator> spectators = new ArrayList<>();
    private final Arena arena;
}
