package org.meijer.jelly.jellyFarmService.model.cage;

import org.meijer.jelly.jellyFarmService.model.jelly.JellyOverview;
import org.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CageOverviewDTO {
    @JsonProperty("cage")
    private CageDTO cage;

    @JsonProperty("jellyOverview")
    private JellyOverview jellyOverview;


    @JsonProperty("totalJellies")
    private long getTotalJellies() {
        return jellyOverview.getTotal();
    }

    public CageOverviewDTO(CageEntity entity, JellyOverview overview) {
        this.cage = new CageDTO(entity);
        this.jellyOverview = overview;
    }

    public CageOverviewDTO(CageEntity entity) {
        this(entity, null);
    }
}
