package org.meijer.jelly.jellyFarmService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.cage.entity.CageEntity;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyListDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.FEMALE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.MALE;

public class ObjectHelper {public static JellyListDTO getJellyListDTO(int numberOfMales, int numberOfFemales, Color colorOne, Color colorTwo) {
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
        return new CageListDTO(cageDTOList);
    }

    public static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static JellyEntity getJellyEntity(UUID uuid) {
        return JellyEntity.builder()
            .cageNumber(1L)
            .color(BLUE)
            .dateTimeFreed(null)
            .gender(MALE)
            .id(uuid)
            .build();
    }

    public static List<JellyEntity> getJellyEntityList() {
        return Arrays.asList(getJellyEntity(UUID.randomUUID()),
                getJellyEntity(UUID.randomUUID()),
                getJellyEntity(UUID.randomUUID()));
    }

    public static CageListDTO getCageDTOList() {
        return new CageListDTO(Arrays.asList(getCageDTO(1L), getCageDTO(2L)));
    }

    public static CageDTO getCageDTO(long cageNumber) {
        return new CageDTO(cageNumber, "White Room");
    }

    public static List<CageOverviewDTO> getCageOverviewList() {
        return Arrays.asList(getCageOverviewDTO(1L), getCageOverviewDTO(2L));
    }

    private static CageOverviewDTO getCageOverviewDTO(long cageNumber) {
        return new CageOverviewDTO(
                new CageEntity(cageNumber, "White Room"),
                new JellyOverviewDTO(getJellyEntityList()));
    }
}
