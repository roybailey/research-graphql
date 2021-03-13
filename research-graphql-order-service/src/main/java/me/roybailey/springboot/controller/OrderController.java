package me.roybailey.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.springboot.domain.OrderForm;
import me.roybailey.springboot.mapper.OrderMapper;
import me.roybailey.springboot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequestMapping(value = "/order-api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    private OrderRepository OrderRepository;

    @Autowired
    private OrderMapper orderMapper;


    @ResponseBody
    @GetMapping(path = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDto> getAllOrders() {
        List<OrderForm> orders = StreamSupport.stream(OrderRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        log.info("getAllOrders() : {}", orders);
        return orderMapper.toOrderDtoList(orders);
    }


    @ResponseBody
    @GetMapping(path = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto getOrder(@PathVariable(name = "id") String id) {
        OrderForm order = OrderRepository.findById(id).orElse(null);
        log.info("getOrder({}) : {}", id, order);
        return orderMapper.toOrderDto(order);
    }


    @ResponseBody
    @PostMapping(path = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto upsertOrder(@RequestBody OrderDto updatedOrder) {
        //updatedOrder.getItems().stream().filter((item)->item.getOrder() == null).forEach((item)-> item.setOrder(updatedOrder));
        OrderForm order = orderMapper.toOrderForm(updatedOrder);
        OrderDto result = orderMapper.toOrderDto(OrderRepository.save(order));
        log.info("upsertOrder({}) : {}", updatedOrder, result);
        return result;
    }


    @ResponseBody
    @DeleteMapping(path = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto deleteOrder(@PathVariable(name = "id") String id) {
        OrderForm order = OrderRepository.findById(id).orElse(null);
        OrderRepository.deleteById(id);
        log.info("deleteOrder({}) : {}", id, order);
        return orderMapper.toOrderDto(order);
    }
}
