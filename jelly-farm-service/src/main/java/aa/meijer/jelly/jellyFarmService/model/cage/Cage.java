package aa.meijer.jelly.jellyFarmService.model.cage;

import aa.meijer.jelly.jellyFarmService.model.jelly.JellyOverview;
import aa.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cage {
    @JsonProperty("cageNumber")
    private long cageNumber;

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

    public Cage(CageEntity entity) {
        this(entity, null);
    }
}
