package org.meijer.jelly.jellyFarmService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConfiguration {
    private KafkaProperties kafkaProperties;

    @Value("${kafka.topic.breeding}")
    private String breedingTopic;

    @Autowired
    public KafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, JellyDTO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                new JsonDeserializer<>(JellyDTO.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, JellyDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, JellyDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public NewTopic breedingTopic() {
        return new NewTopic(breedingTopic, 3, (short) 1);
    }
}
