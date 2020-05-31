package org.meijer.jelly.jellyFarmBreeder.model.cage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CageDTO {
    @JsonProperty("cageNumber")
    private long cageNumber;

    @JsonProperty("habitatName")
    private String habitatName;
}
