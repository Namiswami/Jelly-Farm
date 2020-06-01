package org.meijer.jelly.jellyFarmBreeder.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@EmbeddedKafka(controlledShutdown = true, brokerProperties={"log.dir=./tmp/kafka/eventListenerTest" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class JellyFarmBreedingServiceTest {
    @MockBean
    private RestTemplate restTemplate;



}
