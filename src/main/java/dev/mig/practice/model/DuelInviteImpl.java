package dev.mig.practice.model;

import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.duel.DuelInvite;
import dev.mig.practice.api.model.fighter.Fighter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public final class DuelInviteImpl implements DuelInvite {

    private final Fighter fighterOne;
    private final Fighter fighterTwo;
    private final Date moment;
    private final Arena arena;
}
