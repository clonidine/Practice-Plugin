package dev.mig.practice.model;

import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.arena.positions.Position;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class ArenaImpl implements Arena {

    @Setter
    private List<Position> positions;

    private final String name;

    private final boolean active;
}
