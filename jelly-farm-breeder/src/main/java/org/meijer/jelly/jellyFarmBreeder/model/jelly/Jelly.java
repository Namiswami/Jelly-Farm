package org.meijer.jelly.jellyFarmBreeder.model.jelly;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class Jelly implements Serializable {
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

    public JellyCouple formCouple(List<Jelly> females) {
        Jelly mate = pickMate(females);

        if (mate != null)
            return new JellyCouple(this, mate);
        else {
            log.info("{} couldn't find a mate!", this.getId());
            return null;
        }
    }

    private Jelly pickMate(List<Jelly> underConsideration) {
        if (underConsideration.size() > 0) {
            Random r = new Random();
            return underConsideration.get(r.nextInt(underConsideration.size()));
        } else return null;
    }

}
