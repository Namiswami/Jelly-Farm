package org.meijer.jelly.jellyFarmService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
public class JellyFarmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JellyFarmServiceApplication.class, args);
	}

}
