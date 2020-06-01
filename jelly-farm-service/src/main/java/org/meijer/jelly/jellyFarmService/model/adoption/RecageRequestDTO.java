package org.meijer.jelly.jellyFarmService.model.adoption;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class RecageRequestDTO {
    @JsonProperty("jellyIds")
    private List<UUID> jellyIds;

    @JsonProperty("newCageNumber")
    private Long newCageNumber;
}
