package org.meijer.jelly.jellyFarmBreeder.integration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmBreeder.JellyFarmBreederApplication;
import org.meijer.jelly.jellyFarmBreeder.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyListDTO;
import org.meijer.jelly.jellyFarmBreeder.service.JellyBreedingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.meijer.jelly.jellyFarmBreeder.ObjectHelper.getCageListDTO;
import static org.meijer.jelly.jellyFarmBreeder.ObjectHelper.getJellyListDTO;
import static org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {JellyFarmBreederApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@EmbeddedKafka(brokerProperties={"log.dir=./tmp/kafka/eventListenerTest", "port=9092", "listeners=PLAINTEXT://:9092"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JellyFarmBreedingServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Value("${kafka.topic.breeding}")
    private String breedingTopic;

    @Autowired
    private ConsumerFactory<String, AdoptionRequestDTO> consumerFactory;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private JellyBreedingService jellyBreedingService;

    private Consumer<String, AdoptionRequestDTO> consumer;

    @Before
    public void init() {
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(breedingTopic));

        when(restTemplate.getForEntity(anyString(), eq(CageListDTO.class)))
                .thenReturn(new ResponseEntity<>(getCageListDTO(), HttpStatus.OK));
    }

    @Test
    public void breedingCronSendsKafkaMessageForOneNewbornJelly() {
        //given
        when(restTemplate.getForEntity(anyString(), eq(JellyListDTO.class)))
                .thenReturn(new ResponseEntity<>(getJellyListDTO(1, 1, BLUE), HttpStatus.OK));

        //when
        jellyBreedingService.breedAllCages();

        //then
        ConsumerRecord<String, AdoptionRequestDTO> record = KafkaTestUtils.getSingleRecord(consumer, breedingTopic);
        AdoptionRequestDTO result = record.value();

        assertEquals(BLUE, result.getColor());
        assertEquals(Long.valueOf(1), result.getCageNumber());
        assertNotNull(result.getGender());
    }

    @Test
    public void breedingBlueAndRedLeadsToPurple() {
        //given


        when(restTemplate.getForEntity(anyString(), eq(JellyListDTO.class)))
                .thenReturn(new ResponseEntity<>(getJellyListDTO(1, 1, BLUE, RED), HttpStatus.OK));

        //when
        jellyBreedingService.breedAllCages();

        //then
        ConsumerRecord<String, AdoptionRequestDTO> record = KafkaTestUtils.getSingleRecord(consumer, breedingTopic);
        AdoptionRequestDTO result = record.value();

        assertEquals(PURPLE, result.getColor());
        assertEquals(Long.valueOf(1), result.getCageNumber());
        assertNotNull(result.getGender());
    }

    @Test
    public void breedingStopsWhenCageIsFull() {
        //given
        when(restTemplate.getForEntity(anyString(), eq(JellyListDTO.class)))
                .thenReturn(new ResponseEntity<>(getJellyListDTO(10, 10, BLUE), HttpStatus.OK));

        //when
        jellyBreedingService.breedAllCages();

        //then
        ConsumerRecords<String, AdoptionRequestDTO> consumerRecords =
                KafkaTestUtils.getRecords(consumer, 2000);

        assertEquals(0, consumerRecords.count());
    }

    @Test
    public void noBreedingHappensWhenNoMates() {
        //given
        when(restTemplate.getForEntity(anyString(), eq(JellyListDTO.class)))
                .thenReturn(new ResponseEntity<>(getJellyListDTO(5, 0, BLUE), HttpStatus.OK));

        //when
        jellyBreedingService.breedAllCages();

        //then
        ConsumerRecords<String, AdoptionRequestDTO> consumerRecords =
                KafkaTestUtils.getRecords(consumer, 2000);

        assertEquals(0, consumerRecords.count());
    }

}
