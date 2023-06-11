package dev.mig.practice.api.model.arena;

import dev.mig.practice.api.model.arena.positions.Position;

import java.util.List;

public interface Arena {

    String getName();

    void setPositions(List<Position> positions);

    List<Position> getPositions();

    boolean isActive();
}
