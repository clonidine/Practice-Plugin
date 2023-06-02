package dev.mig.practice.api.manager;

import com.github.mattnicee7.mattlib.cooldown.CooldownMap;
import lombok.Getter;

@Getter
public abstract class ObjectManager<T> {

    private final CooldownMap<T> cooldownMap = new CooldownMap<>();
}
