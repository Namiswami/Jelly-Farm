package org.meijer.jelly.jellyFarmBreeder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JellyFarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(JellyFarmApplication.class, args);
	}

}