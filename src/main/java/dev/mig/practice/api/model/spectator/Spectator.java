package dev.mig.practice.api.model.spectator;

import dev.mig.practice.api.model.match.Match;

import java.util.UUID;

public interface Spectator {

    String getName();

    UUID getId();

    Match getMatch();
}
