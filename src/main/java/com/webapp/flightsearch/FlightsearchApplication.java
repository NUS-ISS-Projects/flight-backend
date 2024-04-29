package com.webapp.flightsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.webapp.flightsearch.entity")
@EnableJpaRepositories("com.webapp.flightsearch.repository")
public class FlightsearchApplication {

	public static void main(String[] args) {
		System.exit(0);
		SpringApplication.run(FlightsearchApplication.class, args);
	}

}