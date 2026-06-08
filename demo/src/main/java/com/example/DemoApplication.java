package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.base.DummyJSpecify;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
//	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicación arrancada...");
		log.warn("Aplicación arrancada...");
	}

	@PreDestroy
	void Cierre() {
		System.err.println("Aplicación cerrada...");
	}
	
	@Bean
	CommandLineRunner nulos() {
		return arg -> {
			try {
				var dummy = new DummyJSpecify("Hola mundo");
//				System.out.println(dummy.getCadena().toUpperCase());
				if(dummy.getCadenaSegura().isPresent())
				System.out.println(dummy.getCadenaSegura().get().toUpperCase());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
}
