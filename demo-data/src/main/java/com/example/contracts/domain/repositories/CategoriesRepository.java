package com.example.contracts.domain.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.entities.Category;

public interface CategoriesRepository extends CrudRepository<Category, Integer> {
	List<Category> findByOrderByName();
}
