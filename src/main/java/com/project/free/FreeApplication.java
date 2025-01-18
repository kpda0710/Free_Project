package com.project.free;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreeApplication.class, args);
	}

}
