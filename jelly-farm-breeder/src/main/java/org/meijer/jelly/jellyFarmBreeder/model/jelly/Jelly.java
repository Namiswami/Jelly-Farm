package org.meijer.jelly.jellyFarmBreeder.model.jelly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Slf4j
public class Jelly {
    private UUID id;
    private Gender gender;
    private Color color;
    private LocalDateTime dateTimeSold;
    private int cageNumber;

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
