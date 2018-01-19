package com.blogen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SpringBootBlogenApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpringBootBlogenApplication.class, args);
	}
}
