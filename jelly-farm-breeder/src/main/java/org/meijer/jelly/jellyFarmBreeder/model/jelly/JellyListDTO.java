package org.meijer.jelly.jellyFarmBreeder.model.jelly;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JellyListDTO {
    @JsonProperty("jellyList")
    private List<Jelly> jellyList;
}
