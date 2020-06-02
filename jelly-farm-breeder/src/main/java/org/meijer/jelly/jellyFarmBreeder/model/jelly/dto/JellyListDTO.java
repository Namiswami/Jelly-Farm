package org.meijer.jelly.jellyFarmBreeder.model.jelly.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JellyListDTO {
    @JsonProperty("jellyList")
    private List<JellyDTO> jellyDTOList;
}
