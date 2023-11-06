package com.example.ddingjakyo_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DdingjakyoBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdingjakyoBeApplication.class, args);
	}

}
