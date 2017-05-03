package me.roybailey.springboot.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.OrderItemDto;
import me.roybailey.springboot.AbstractTestCase;
import me.roybailey.springboot.domain.OrderForm;
import me.roybailey.springboot.domain.OrderItem;
import org.junit.Test;

import java.io.IOException;


@Slf4j
public class OrderMapperTest extends AbstractTestCase {

    OrderMapper mapper = new OrderMapper();

    @Test
    public void testEmptyOrderDto() {

        OrderDto orderDto = OrderDto.builder()
                .id("order-1")
                .userId("user-1")
                .status(OrderDto.Status.SUBMITTED)
                .build();

        OrderForm orderJpa = mapper.toOrderForm(orderDto);

        softly.then(orderJpa.getId()).isEqualTo(orderDto.getId());
        softly.then(orderJpa.getUserId()).isEqualTo(orderDto.getUserId());
        softly.then(orderJpa.getStatus()).isEqualTo(orderDto.getStatus());
    }

    @Test
    public void testOrderDtoWithOrderItemDto() {

        OrderDto orderDto = OrderDto.builder()
                .id("order-1")
                .userId("user-1")
                .status(OrderDto.Status.SUBMITTED)
                .items(ImmutableList.of(
                        OrderItemDto.builder()
                        .productId("product-1")
                        .quantity(10)
                        .build()
                ))
                .build();

        OrderForm orderJpa = mapper.toOrderForm(orderDto);

        softly.then(orderJpa.getId()).isEqualTo(orderDto.getId());
        softly.then(orderJpa.getUserId()).isEqualTo(orderDto.getUserId());
        softly.then(orderJpa.getStatus()).isEqualTo(orderDto.getStatus());
        softly.then(orderJpa.getItems())
                .isNotNull()
                .hasSize(1);
        softly.then(orderJpa.getItems().get(0).getId()).isNull();
        softly.then(orderJpa.getItems().get(0).getOrder())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", "order-1");
        softly.then(orderJpa.getItems().get(0).getProductId()).isEqualTo("product-1");
        softly.then(orderJpa.getItems().get(0).getQuantity()).isEqualTo(10);
    }

    @Test
    public void testOrderJpaToOrderDto() throws IOException {

        OrderForm orderJpa = OrderForm.builder()
                .id("orderJpa-1")
                .userId("user-1")
                .status(OrderDto.Status.SUBMITTED)
                .items(ImmutableList.of(
                        OrderItem.builder()
                                .productId("product-1")
                                .quantity(10)
                                .build()
                ))
                .build();

        OrderDto orderDto = mapper.toOrderDto(orderJpa);

        softly.then(orderDto.getId()).isEqualTo(orderJpa.getId());
        softly.then(orderDto.getUserId()).isEqualTo(orderJpa.getUserId());
        softly.then(orderDto.getStatus()).isEqualTo(orderJpa.getStatus());
        softly.then(orderDto.getItems())
                .isNotNull()
                .hasSize(1);
        softly.then(orderDto.getItems().get(0).getProductId()).isEqualTo("product-1");
        softly.then(orderDto.getItems().get(0).getQuantity()).isEqualTo(10);

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonOrderDto = jsonMapper.writeValueAsString(orderDto);
        OrderDto pojoOrderDto = jsonMapper.readValue(jsonOrderDto, OrderDto.class);

        softly.then(pojoOrderDto.getId()).isEqualTo(orderJpa.getId());
        softly.then(pojoOrderDto.getUserId()).isEqualTo(orderJpa.getUserId());
        softly.then(pojoOrderDto.getStatus()).isEqualTo(orderJpa.getStatus());
        softly.then(pojoOrderDto.getItems())
                .isNotNull()
                .hasSize(1);
        softly.then(pojoOrderDto.getItems().get(0).getProductId()).isEqualTo("product-1");
        softly.then(pojoOrderDto.getItems().get(0).getQuantity()).isEqualTo(10);

    }

}
