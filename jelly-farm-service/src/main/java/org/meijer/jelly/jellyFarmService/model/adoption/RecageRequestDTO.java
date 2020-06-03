package org.meijer.jelly.jellyFarmService.model.adoption;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecageRequestDTO {
    @JsonProperty("jellyIds")
    @NotEmpty(message = "List of jelly Id's cannot be empty")
    private List<UUID> jellyIds;

    @JsonProperty("newCageNumber")
    private Long newCageNumber;
}
