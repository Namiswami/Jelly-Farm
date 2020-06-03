package org.meijer.jelly.jellyFarmService.listener;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BreederListener {
    private final JellyService jellyService;

    @Autowired
    public BreederListener(JellyService jellyService) {
        this.jellyService = jellyService;
    }

    @KafkaListener(topics = "${kafka.topic.breeding}")
    public void listen(@Payload AdoptionRequestDTO adoptionRequestDTO) {
        log.info("Received message that a new {} {} jelly has been born in cage {}",
                adoptionRequestDTO.getGender(),
                adoptionRequestDTO.getColor(),
                adoptionRequestDTO.getCageNumber());
        jellyService.adoptJelly(adoptionRequestDTO);
    }
}