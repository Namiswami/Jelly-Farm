package org.meijer.jelly.jellyFarmService.model.cage.dto;

import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.cage.entity.CageEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CageOverviewDTO {
    @JsonProperty("cage")
    private CageDTO cage;

    @JsonProperty("jellyOverview")
    private JellyOverviewDTO jellyOverview;


    @JsonProperty("totalJellies")
    private long getTotalJellies() {
        return jellyOverview.getTotal();
    }

    public CageOverviewDTO(CageEntity entity, JellyOverviewDTO overview) {
        this.cage = new CageDTO(entity);
        this.jellyOverview = overview;
    }

    public CageOverviewDTO(CageEntity entity) {
        this(entity, null);
    }
}
