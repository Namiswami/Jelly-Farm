package org.meijer.jelly.jellyFarmService.model.adoption;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;

import javax.validation.constraints.NotNull;

@Getter
public class AdoptionRequestDTO {
    @JsonProperty("cageNumber")
    @NotNull
    private Long cageNumber;


    @JsonProperty("color")
    @NotNull
    private Color color;

    @JsonProperty("gender")
    @NotNull
    private Gender gender;
}
