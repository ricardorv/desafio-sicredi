package com.github.ricardorv.desafiosicredi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class DesafioSicrediApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioSicrediApplication.class, args);
	}

}
