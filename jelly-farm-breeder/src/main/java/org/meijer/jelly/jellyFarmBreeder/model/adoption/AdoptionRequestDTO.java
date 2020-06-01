package org.meijer.jelly.jellyFarmBreeder.model.adoption;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyDTO;

@Getter
public class AdoptionRequestDTO {
    @JsonProperty("cageNumber")
    private Long cageNumber;
    @JsonProperty("color")
    private Color color;
    @JsonProperty("gender")
    private Gender gender;

    public AdoptionRequestDTO(JellyDTO jellyDTO) {
        this.cageNumber = jellyDTO.getCageNumber();
        this.color = jellyDTO.getColor();
        this.gender = jellyDTO.getGender();
    }
}
