package me.roybailey.springboot.repository;

import me.roybailey.springboot.domain.OrderForm;
import org.springframework.data.repository.CrudRepository;


public interface OrderRepository extends CrudRepository<OrderForm, String> {

}
