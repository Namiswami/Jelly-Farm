package org.meijer.jelly.jellyFarmService.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.DataManager;
import org.meijer.jelly.jellyFarmService.JellyFarmServiceApplication;
import org.meijer.jelly.jellyFarmService.integration.config.KafkaTestConfiguration;
import org.meijer.jelly.jellyFarmService.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.FEMALE;

@SpringBootTest(
        classes = {JellyFarmServiceApplication.class, KafkaTestConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@Import(KafkaTestConfiguration.class)
@EmbeddedKafka(brokerProperties={"log.dir=./tmp/kafka/eventListenerTest", "port=9092", "listeners=PLAINTEXT://:9092"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BreederListenerTest {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private JellyStockRepository jellyStockRepository;

    @Value("${kafka.topic.breeding}")
    private String breedingTopic;

    @Before
    public void init() {
        dataManager.createDefaultCage();
    }

    @Test
    public void listenerCreatesNewJellyInDbWhenMessageIsReceived() throws InterruptedException {
        //when
        kafkaTemplate.send(breedingTopic, new AdoptionRequestDTO(1L, BLUE, FEMALE));

        //then
        Thread.sleep(2000); //Give the listener some time to do it's thing
        List<JellyEntity> jellies = jellyStockRepository.findAll();

        assertEquals(1, jellies.size());

        JellyEntity jelly = jellies.get(0);

        assertEquals(Long.valueOf(1), jelly.getCageNumber());
        assertEquals(BLUE, jelly.getColor());
        assertEquals(FEMALE, jelly.getGender());
        assertNull(jelly.getDateTimeFreed());
        assertNotNull(jelly.getId());
    }
}
