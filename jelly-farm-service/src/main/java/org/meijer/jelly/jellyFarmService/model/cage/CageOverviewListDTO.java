package org.meijer.jelly.jellyFarmService.model.cage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CageOverviewListDTO {
    @JsonProperty("cages")
    private List<CageOverviewDTO> cageOverviewDTOS;
}
