package me.roybailey.springboot.controller;


import com.google.common.collect.ImmutableList;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.OrderItemDto;
import me.roybailey.springboot.OrderServiceApplication;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = OrderServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @LocalServerPort
    int port;

    @Rule
    public TestName name = new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    @Headers("Accept: application/json")
    public interface OrderApi {

        @RequestLine("GET /order-api/v1/order")
        List<OrderDto> getAllOrders();

        @RequestLine("GET /order-api/v1/order/{id}")
        OrderDto getOrder(@Param("id") String id);

        @Headers("Content-Type: application/json")
        @RequestLine("POST /order-api/v1/order")
        OrderDto saveOrder(OrderDto order);

        @RequestLine("DELETE /order-api/v1/order/{id}")
        Response deleteOrder(@Param("id") String id);
    }

    private OrderApi api;

    @Before
    public void apiSetup() {
        this.api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.BASIC)
                .target(OrderApi.class, "http://localhost:" + port);
    }

    @Test
    public void test1_OrderApi() {

        List<OrderDto> allOrders = api.getAllOrders();
        assertThat(allOrders).isNotNull();
        softly.then(allOrders).hasSize(2);

        OrderDto expected = allOrders.get(1);
        OrderDto actual = api.getOrder(expected.getId());
        softly.then(actual)
                .isNotNull()
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    public void test2_OrderApiUpdates() {

        List<OrderDto> allOrders = api.getAllOrders();
        assertThat(allOrders)
                .isNotNull()
                .hasSize(2);

        OrderDto newOrder = OrderDto.builder()
                .status(OrderDto.Status.SUBMITTED)
                .userId("U01")
                .build();
        OrderDto savedOrder = api.saveOrder(newOrder);
        softly.then(savedOrder)
                .isNotNull()
                .isEqualToIgnoringNullFields(newOrder);
        softly.then(savedOrder.getId()).isNotNull();

        savedOrder.setItems(ImmutableList.of(
                OrderItemDto.builder()
                        .productId("P01")
                        .quantity(5)
                        .build()
        ));
        OrderDto updatedOrder = api.saveOrder(savedOrder);
        softly.then(updatedOrder)
                .isNotNull()
                .isEqualToIgnoringNullFields(savedOrder);
        softly.then(updatedOrder.getItems())
                .isNotNull()
                .hasSize(1);

        allOrders = api.getAllOrders();
        softly.then(allOrders)
                .isNotNull()
                .hasSize(3);

        Response response = api.deleteOrder(savedOrder.getId());
        softly.then(response.status()).isEqualTo(200);

        allOrders = api.getAllOrders();
        softly.then(allOrders)
                .isNotNull()
                .hasSize(2);
    }
}
