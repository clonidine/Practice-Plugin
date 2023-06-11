package dev.mig.practice.manager;

import dev.mig.practice.api.manager.ObjectManager;
import dev.mig.practice.api.model.duel.DuelInvite;
import dev.mig.practice.api.model.fighter.Fighter;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class DuelInviteManager extends ObjectManager<DuelInvite> {


    private final List<DuelInvite> duelInvites = new ArrayList<>();


    public Optional<DuelInvite> getInvite(@NotNull Player playerOne, @NotNull Player playerTwo) {

        for (DuelInvite duelInvite : duelInvites) {

            final Fighter fighterOne = duelInvite.getFighterOne();
            final Fighter fighterTwo = duelInvite.getFighterTwo();

            final UUID fighterOneUuid = fighterOne.getUuid();
            final UUID fighterTwoUuid = fighterTwo.getUuid();

            final boolean hasFoundInvite = fighterOneUuid.equals(playerOne.getUniqueId()) && fighterTwoUuid.equals(playerTwo.getUniqueId());

            if (hasFoundInvite) {
                return Optional.of(duelInvite);
            }

        }

        return Optional.empty();
    }
}
