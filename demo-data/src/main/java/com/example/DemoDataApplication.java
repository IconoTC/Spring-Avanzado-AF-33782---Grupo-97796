package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.ActoresRepository;
import com.example.domain.entities.Actor;

@SpringBootApplication
public class DemoDataApplication {
	@Autowired @Lazy DemoDataApplication self;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoDataApplication.class, args);
	}

//	@Bean
	CommandLineRunner crud(ActoresRepository dao) {
		return arg -> {
			var actor = dao.save(new Actor("Pepito", "Grillo"));
			var id = actor.getActorId();
			var leido = dao.findById(id);
			if(leido.isEmpty()) {
				System.err.println("Actor no encontrado");
				return;
			}
			actor = leido.get();
			actor.setFirstName(actor.getFirstName().toUpperCase());
			dao.save(actor);
			dao.findAll().forEach(System.out::println);
			dao.deleteById(id);
			if(!dao.existsById(id))
				System.err.println("Actor %d no encontrado".formatted(id));
			dao.deleteById(id);
		};
	}

	@Bean
	CommandLineRunner consultas(ActoresRepository dao) {
		return arg -> {
//			dao.findTop5ByFirstNameStartingWithOrderByLastNameDesc("P").forEach(System.out::println);
//			dao.findTop5ByFirstNameStartingWith("P", Sort.by("FirstName").descending()).forEach(System.out::println);
			
//			dao.findByActorIdGreaterThanEqual(195).forEach(System.out::println);
//			dao.findNovedadesJPQL(195).forEach(System.out::println);
//			dao.findNovedadesSQL(195).forEach(System.out::println);
//			dao.findAll((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("actorId"), 195)).forEach(System.out::println);
			dao.findAll((root, query, builder) -> builder.lessThan(root.get("actorId"), 4)).forEach(System.out::println);
//			self.navegacion(dao);
			try {
//				self.modifica(dao);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dao.findNovedadesJPQL(200).forEach(System.out::println);
		};
	}
	
	@Transactional
	void navegacion(ActoresRepository dao) {
		dao.findNovedadesJPQL(199).forEach(a -> {
			System.out.println(a.toString());
			a.getFilmActors().forEach(p -> System.out.println("\t%s".formatted(p.getFilm().getTitle())));
		});
	}

	@Transactional(rollbackFor = Exception.class)
	void modifica(ActoresRepository dao) {
		dao.save(new Actor("Pepito", "Grillo"));
		dao.save(new Actor("Carmelo", "Coton"));
//		dao.deleteById(1);
	}

	
}
