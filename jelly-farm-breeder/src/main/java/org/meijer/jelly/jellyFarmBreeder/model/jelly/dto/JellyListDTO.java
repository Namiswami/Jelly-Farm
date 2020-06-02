package org.meijer.jelly.jellyFarmBreeder.model.jelly.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class JellyListDTO {
    @JsonProperty("jellyList")
    private List<JellyDTO> jellyDTOList;
}
