package org.meijer.jelly.jellyFarmBreeder.model.jelly;

import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class JellyCouple {
    private Jelly father;
    private Jelly mother;

    public Jelly mate() {
        Jelly newBorn = Jelly.builder()
                .color(determineColor(father.getColor(), mother.getColor()))
                .gender(randomGender())
                .cageNumber(father.getCageNumber())
                .build();

        log.info("A new {} {} jelly was born",
                newBorn.getGender().toString().toLowerCase(),
                newBorn.getColor().toString().toLowerCase());
        return newBorn;
    }

    private Gender randomGender() {
        Random r = new Random();
        if(r.nextBoolean()) return Gender.MALE;
        else return Gender.FEMALE;
    }

    private Color determineColor(Color fatherColor, Color motherColor) {
        return Color.mixColors(fatherColor, motherColor);
    }
}
