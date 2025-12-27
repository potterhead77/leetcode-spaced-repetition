package com.nandan.spaced_repetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class SpacedRepetitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpacedRepetitionApplication.class, args);
	}

}
