package org.meijer.jelly.jellyFarmBreeder.model.cage;

import org.meijer.jelly.jellyFarmBreeder.model.jelly.JellyOverview;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Cage {
    @JsonProperty("cageNumber")
    private int cageNumber;

    @JsonProperty("habitatName")
    private String habitatName;

    @JsonProperty("jellyOverview")
    private JellyOverview jellyOverview;

    @JsonProperty("totalJellies")
    private long getTotalJellies() {
        return jellyOverview.getTotal();
    }

    public Cage(Cage cage, JellyOverview overview) {
        this.cageNumber = cage.getCageNumber();
        this.jellyOverview = overview;
    }
}
