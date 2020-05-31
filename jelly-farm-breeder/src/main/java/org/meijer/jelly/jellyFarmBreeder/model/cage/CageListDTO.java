package org.meijer.jelly.jellyFarmBreeder.model.cage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CageListDTO {
    @JsonProperty("cageList")
    private List<CageDTO> cageDTOList;
}
