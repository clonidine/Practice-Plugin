package dev.mig.practice.api.model.duel;

import dev.mig.practice.api.model.arena.Arena;
import dev.mig.practice.api.model.fighter.Fighter;

import java.util.Date;

public interface DuelInvite {

    Fighter getFighterOne();

    Fighter getFighterTwo();

    Date getMoment();

    Arena getArena();
}
