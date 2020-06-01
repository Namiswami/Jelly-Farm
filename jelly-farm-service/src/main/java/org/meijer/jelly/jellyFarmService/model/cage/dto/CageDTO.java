package org.meijer.jelly.jellyFarmService.model.cage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.meijer.jelly.jellyFarmService.model.cage.entity.CageEntity;

@Getter
@Setter
@AllArgsConstructor
public class CageDTO {
    @JsonProperty("cageNumber")
    private long cageNumber;

    @JsonProperty("habitatName")
    private String habitatName;

    public CageDTO(CageEntity entity) {
        this.cageNumber = entity.getCageNumber();
        this.habitatName = entity.getHabitatName();
    }
}
