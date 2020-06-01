package org.meijer.jelly.jellyFarmService.listener;

import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
    public void listen(@Payload JellyDTO newBornJelly) {
        log.info("Received message that a new {} {} jelly has been born in cage {}",
                newBornJelly.getGender(),
                newBornJelly.getColor(),
                newBornJelly.getCageNumber());
        jellyService.save(newBornJelly);
    }
}
