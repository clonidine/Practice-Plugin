package dev.mig.practice.manager;

import dev.mig.practice.api.exception.ObjectNotFoundException;
import dev.mig.practice.api.model.match.Match;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class MatchManager {

    private final Map<UUID, Match> matchMap = new HashMap<>();

    public boolean isInMatch(UUID playerUuid) {
        return matchMap.containsKey(playerUuid);
    }

    public Optional<Match> getMatch(UUID playerUuid) {
        final Match match = matchMap.get(playerUuid);

        if (match == null) {
            return Optional.empty();
        }

        return Optional.of(match);
    }

    public boolean removeFrom(UUID playerUuid) {
        final Optional<Match> matchToFind = getMatch(playerUuid);

        if (!matchToFind.isPresent()) {
            throw new ObjectNotFoundException("Match not found");
        }

        matchMap.remove(playerUuid);

        return true;
    }
}
