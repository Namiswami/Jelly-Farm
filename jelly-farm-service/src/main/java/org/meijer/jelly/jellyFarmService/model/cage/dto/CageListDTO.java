package org.meijer.jelly.jellyFarmService.model.cage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CageListDTO {
    @JsonProperty("cageList")
    private List<CageDTO> cageDTOList;
}
