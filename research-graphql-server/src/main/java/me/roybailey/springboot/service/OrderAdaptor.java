package me.roybailey.springboot.service;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.springboot.configuration.DataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Slf4j
@Service()
public class OrderAdaptor {

    @Autowired
    DataSourceProperties properties;

    @Headers("Accept: application/json")
    private interface OrderApi {

        @RequestLine("GET /order-api/v1/order")
        List<OrderDto> getAllOrders();

        @RequestLine("GET /order-api/v1/order/{id}")
        OrderDto getOrderById(@Param("id") String id);

        @Headers("Content-Type: application/json")
        @RequestLine("POST /order-api/v1/order")
        OrderDto createOrder(OrderDto order);
    }

    private OrderApi api;

    @PostConstruct
    public void apiSetup() {
        log.info("Connecting {} to {}", OrderApi.class.getSimpleName(), properties.getUrlOrderService());
        this.api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.BASIC)
                .target(OrderApi.class, properties.getUrlOrderService());
    }


    public List<OrderDto> getAllOrders() {
        List<OrderDto> allOrders = api.getAllOrders();
        log.info("getAllOrders() : {}", allOrders);
        return allOrders;
    }

    public OrderDto getOrder(String orderId) {
        OrderDto order = api.getOrderById(orderId);
        log.info("getOrder({}) : {}", orderId, order);
        return order;
    }

    public OrderDto createOrder(OrderDto order) {
        OrderDto newOrder = api.createOrder(order);
        log.info("createOrder({}) : {}", order, newOrder);
        return newOrder;
    }

}

