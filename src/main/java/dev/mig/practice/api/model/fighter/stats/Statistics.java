package dev.mig.practice.api.model.fighter.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Statistics {

    private int kills;
    private int deaths;
    private float kdr;
}
