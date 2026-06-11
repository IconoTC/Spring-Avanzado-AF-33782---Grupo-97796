package com.example.domain.entities.models;

import org.springframework.beans.factory.annotation.Value;

//@Projection(name = "nombre-corto", types = {Actor.class})
public interface ActorShort {
	@Value("#{target.actorId}")
	int getId();
	@Value("#{target.firstName + ' ' + target.lastName}")
	String getNombre();
}
