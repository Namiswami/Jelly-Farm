package org.meijer.jelly.jellyFarmBreeder.model.jelly.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JellyListDTO {
    @JsonProperty("jellyList")
    private List<JellyDTO> jellyDTOList;
}
