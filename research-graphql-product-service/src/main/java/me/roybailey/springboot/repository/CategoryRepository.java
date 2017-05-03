package me.roybailey.springboot.repository;

import me.roybailey.springboot.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface CategoryRepository extends CrudRepository<Category, String>  {

    Optional<Category> findByName(String name);
}
