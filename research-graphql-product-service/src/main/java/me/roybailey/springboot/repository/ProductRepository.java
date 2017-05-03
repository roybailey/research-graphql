package me.roybailey.springboot.repository;

import me.roybailey.springboot.domain.Product;
import org.springframework.data.repository.CrudRepository;


public interface ProductRepository extends CrudRepository<Product, String> {

}
