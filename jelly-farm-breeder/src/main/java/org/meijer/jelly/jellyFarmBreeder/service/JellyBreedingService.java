package org.meijer.jelly.jellyFarmBreeder.service;


import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmBreeder.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.JellyCouple;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JellyBreedingService {
    private final String breedingTopic;
    private final Integer cageLimit;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final JellyDataService jellyDataService;

    @Autowired
    public JellyBreedingService(@Value("${kafka.topic.breeding}") String breedingTopic,
                                @Value("${cage.limit}") Integer cageLimit,
                                KafkaTemplate kafkaTemplate,
                                JellyDataService jellyDataService) {
        this.breedingTopic = breedingTopic;
        this.cageLimit = cageLimit;
        this.kafkaTemplate = kafkaTemplate;
        this.jellyDataService = jellyDataService;
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void breedAllCages() {
        List<CageDTO> cageDTOS = jellyDataService.getCages();
        for (CageDTO cageDTO : cageDTOS) {
            breed(cageDTO);
        }

    }

    private void breed(CageDTO cageDTO) {
        List<JellyDTO> jellies = jellyDataService.getUnsoldJellies(cageDTO.getCageNumber());
        List<JellyDTO> availableFemales = filterByGender(Gender.FEMALE, jellies);
        List<JellyDTO> males = filterByGender(Gender.MALE, jellies);
        long numberOfNewBorns = 0;

        for (JellyDTO male : males) {
            if (isEnoughRoomInCage(jellies, numberOfNewBorns)) {
                JellyCouple couple = male.formCouple(availableFemales);

                if (couple != null) {
                    availableFemales.remove(couple.getMother());
                    log.info("A new jelly was born, producing kafka message");
                    kafkaTemplate.send(breedingTopic, new AdoptionRequestDTO(couple.mate()));
                    numberOfNewBorns++;
                }
            } else {
                log.info("Cage {} is full, breeding was stopped", cageDTO.getCageNumber());
                break;
            }
        }

        log.info("{} new jellies have been born in {} cage: {}", numberOfNewBorns, cageDTO.getHabitatName(), cageDTO.getCageNumber());
    }

    private boolean isEnoughRoomInCage(List<JellyDTO> jellies, long numberOfNewBorns) {
        return jellies.size() + numberOfNewBorns < cageLimit;
    }

    private List<JellyDTO> filterByGender(Gender gender, List<JellyDTO> jellies) {
        return jellies.stream()
                .filter(j -> j.getGender() == gender)
                .collect(Collectors.toList());
    }
}
