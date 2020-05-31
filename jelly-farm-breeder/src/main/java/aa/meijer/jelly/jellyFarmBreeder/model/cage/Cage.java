package aa.meijer.jelly.jellyFarmService.model.cage;

import aa.meijer.jelly.jellyFarmService.model.jelly.JellyOverview;
import aa.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
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

    public Cage(CageEntity entity, JellyOverview overview) {
        this.cageNumber = entity.getCageNumber();
        this.habitatName = entity.getHabitatName();
        this.jellyOverview = overview;
    }
}
