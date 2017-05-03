package me.roybailey.springboot.repository;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.springboot.configuration.JpaRepositoryConfiguration;
import me.roybailey.springboot.domain.OrderForm;
import me.roybailey.springboot.domain.OrderItem;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaRepositoryConfiguration.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Rule
    public TestName name= new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();


    private List<OrderForm> loadAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Test
    public void testLoadedOrders(){

        List<OrderForm> allOrders = loadAllOrders();
        log.info(name+" loaded OrderForm data = " + allOrders);
        assertThat(allOrders).isNotNull().hasSize(2);
    }

    @Test
    public void testCreateUpdateDeleteOrder(){

        int count = loadAllOrders().size();

        OrderForm newOrder = OrderForm.builder()
                .status(OrderDto.Status.SUBMITTED)
                .userId("U01")
                .build();
        OrderForm savedOrder = orderRepository.save(newOrder);
        log.info("saved OrderForm {}", savedOrder);
        assertThat(savedOrder)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("items");

        savedOrder.setItems(ImmutableList.of(
                OrderItem.builder()
                        .order(savedOrder)
                        .productId("P01")
                        .quantity(5)
                        .build()
        ));
        OrderForm updatedOrder = orderRepository.save(savedOrder);
        assertThat(updatedOrder.getItems())
                .isNotNull()
                .hasSize(1);
        softly.then(updatedOrder.getItems().get(0).getQuantity())
                .isEqualTo(5);

        softly.then(loadAllOrders()).hasSize(count+1);
        orderRepository.delete(updatedOrder.getId());
        softly.then(loadAllOrders()).hasSize(count);
    }

}
