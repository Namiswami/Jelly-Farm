package org.meijer.jelly.jellyFarmService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.meijer.jelly.jellyFarmService.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.cage.entity.CageEntity;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.MALE;

public class ObjectHelper {
    public static JellyDTO getJellyDTO(Gender gender, Color color) {
        return JellyDTO.builder()
                .cageNumber(1)
                .color(color)
                .dateTimeFreed(null)
                .gender(gender)
                .id(null)
                .build();
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
        List<JellyEntity> list = new ArrayList<>();
        do {
            list.add(getJellyEntity(UUID.randomUUID()));
        } while (list.size() < 3);
        return list;
    }

    public static List<JellyEntity> getJellyEntityListForFullCage() {
        List<JellyEntity> entities = new ArrayList<>();
        do {
            entities.add(getJellyEntity(UUID.randomUUID()));
        } while (entities.size() < 20);
        return entities;
    }

    public static List<CageOverviewDTO> getCageOverviewList() {
        return Arrays.asList(getCageOverviewDTO(1L), getCageOverviewDTO(2L));
    }

    public static CageOverviewDTO getCageOverviewDTO(long cageNumber) {
        return new CageOverviewDTO(
                new CageEntity(cageNumber, "White Room"),
                new JellyOverviewDTO(getJellyEntityList()));
    }

    public static CageEntity getCageEntity() {
        return new CageEntity(1L, "Cloudy Bay");
    }

    public static List<CageEntity> getCageEntityList() {
        return Arrays.asList(getCageEntity(), getCageEntity(), getCageEntity());
    }

    public static AdoptionRequestDTO getAdoptionRequest() {
        return new AdoptionRequestDTO(1L, BLUE, MALE);
    }
}
