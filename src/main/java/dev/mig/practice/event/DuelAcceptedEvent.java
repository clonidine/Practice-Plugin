package dev.mig.practice.event;

import dev.mig.practice.api.event.CustomEvent;
import dev.mig.practice.api.model.match.Match;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.event.Cancellable;

@Getter
@RequiredArgsConstructor
public final class DuelAcceptedEvent extends CustomEvent implements Cancellable {

    private final Match match;
    private final World world;

    @Setter
    private boolean cancelled;
}
