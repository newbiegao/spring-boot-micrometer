package com.gaolk.micrometer.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@Configuration
public class MicrometerApplication {


	public static void main(String[] args) {
		SpringApplication.run(MicrometerApplication.class, args);

	}


}
