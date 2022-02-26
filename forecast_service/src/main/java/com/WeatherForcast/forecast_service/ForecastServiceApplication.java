package com.WeatherForcast.forecast_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class ForecastServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForecastServiceApplication.class, args);
	}

}
