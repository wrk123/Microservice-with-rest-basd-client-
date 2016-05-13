package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class Application {

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
