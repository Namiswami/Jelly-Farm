package org.meijer.jelly.jellyFarmService.integration;

import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.JellyFarmServiceApplication;
import org.meijer.jelly.jellyFarmService.integration.config.KafkaTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
        classes = {JellyFarmServiceApplication.class, KafkaTestConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@Import(KafkaTestConfiguration.class)
@EmbeddedKafka(brokerProperties={"log.dir=./tmp/kafka/eventListenerTest", "port=9092", "listeners=PLAINTEXT://:9092"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JellyDetailsControllerTest {
}
