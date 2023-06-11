package dev.mig.practice.api.model.arena.positions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class Position {

    private final String id;

    private final double x, y, z;

    private final float yaw, pitch;
}
