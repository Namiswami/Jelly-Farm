package org.meijer.jelly.jellyFarmBreeder.model.jelly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.JellyCouple;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class JellyDTO implements Serializable {
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

    public JellyCouple formCouple(List<JellyDTO> females) {
        JellyDTO mate = pickMate(females);

        if (mate != null)
            return new JellyCouple(this, mate);
        else {
            log.info("{} couldn't find a mate!", this.getId());
            return null;
        }
    }

    private JellyDTO pickMate(List<JellyDTO> underConsideration) {
        if (underConsideration.size() > 0) {
            Random r = new Random();
            return underConsideration.get(r.nextInt(underConsideration.size()));
        } else return null;
    }

}
