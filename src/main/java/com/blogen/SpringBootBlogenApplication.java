package com.blogen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SpringBootBlogenApplication {

	//todo spring-boot-web project has security config example plus h2-console security config
    //todo /SpringProjects/sg-spring-core-advanced/spring-core-spring-mvc has example security config and H2 config

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBlogenApplication.class, args);
	}
}
