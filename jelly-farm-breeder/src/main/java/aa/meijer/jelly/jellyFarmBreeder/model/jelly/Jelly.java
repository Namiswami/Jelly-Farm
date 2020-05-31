package aa.meijer.jelly.jellyFarmService.model.jelly;

import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import aa.meijer.jelly.jellyFarmService.repository.entity.JellyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

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

    public Jelly(JellyEntity entity) {
        color = entity.getColor();
        gender = entity.getGender();
        dateTimeSold = entity.getDateTimeSold();
        id = entity.getId();
        cageNumber = entity.getCageNumber();
    }

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
