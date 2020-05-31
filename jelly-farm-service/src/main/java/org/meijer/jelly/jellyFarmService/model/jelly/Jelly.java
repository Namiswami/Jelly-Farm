package org.meijer.jelly.jellyFarmService.model.jelly;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.repository.entity.JellyEntity;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Jelly {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("color")
    private Color color;
    @JsonProperty("dateTimeFreed")
    private LocalDateTime dateTimeFreed;
    @JsonProperty("cageNumber")
    private long cageNumber;

    public Jelly(JellyEntity entity) {
        color = entity.getColor();
        gender = entity.getGender();
        dateTimeFreed = entity.getDateTimeFreed();
        id = entity.getId();
        cageNumber = entity.getCageNumber();
    }
}
