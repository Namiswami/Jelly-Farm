package org.meijer.jelly.jellyFarmBreeder;

import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyListDTO;

import java.util.ArrayList;
import java.util.List;

import static org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender.FEMALE;
import static org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender.MALE;

public class ObjectHelper {
    public static JellyListDTO getJellyListDTO(int numberOfMales, int numberOfFemales, Color colorOne, Color colorTwo) {
        List<JellyDTO> jellies = new ArrayList<>();
        for(int i = 0; i < numberOfFemales ; i++) {
            JellyDTO jellyMale = getJellyDTO(FEMALE, colorOne);
            jellies.add(jellyMale);
        }
        for(int i = 0; i < numberOfMales ; i++) {
            JellyDTO jellyMale = getJellyDTO(MALE, colorTwo);
            jellies.add(jellyMale);
        }
        return new JellyListDTO(jellies);
    }

    public static JellyListDTO getJellyListDTO(int numberOfMales, int numberOfFemales, Color color) {
        return getJellyListDTO(numberOfMales, numberOfFemales, color, color);
    }

    public static JellyDTO getJellyDTO(Gender gender, Color color) {
        return JellyDTO.builder()
                .cageNumber(1)
                .color(color)
                .dateTimeFreed(null)
                .gender(gender)
                .id(null)
                .build();
    }

    public static CageListDTO getCageListDTO() {
        List<CageDTO> cageDTOList = new ArrayList<>();
        cageDTOList.add(new CageDTO(1, "Indoor Hotsprings"));
        CageListDTO cageResponse = new CageListDTO();
        cageResponse.setCageDTOList(cageDTOList);
        return cageResponse;
    }
}
