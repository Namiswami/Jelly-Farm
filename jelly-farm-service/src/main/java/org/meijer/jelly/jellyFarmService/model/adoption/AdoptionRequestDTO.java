package org.meijer.jelly.jellyFarmService.model.adoption;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;

@Getter
public class AdoptionRequestDTO {
    @JsonProperty("cageNumber")
    private Long cageNumber;
    @JsonProperty("color")
    private Color color;
    @JsonProperty("gender")
    private Gender gender;
}
