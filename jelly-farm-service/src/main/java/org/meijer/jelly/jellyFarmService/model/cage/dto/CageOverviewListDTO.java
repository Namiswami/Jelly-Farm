package org.meijer.jelly.jellyFarmService.model.cage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CageOverviewListDTO {
    @JsonProperty("cages")
    private List<CageOverviewDTO> cageOverviewDTOS;

    public CageOverviewListDTO(CageOverviewDTO cageOverview) {
        this.cageOverviewDTOS = Collections.singletonList(cageOverview);
    }
}
