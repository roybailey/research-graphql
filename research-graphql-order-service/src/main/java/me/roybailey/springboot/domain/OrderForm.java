package me.roybailey.springboot.domain;

import lombok.*;
import me.roybailey.data.schema.OrderDto;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;


@Data
@Builder
@Entity
@ToString(exclude = "items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderForm {

    @Id
    @GeneratedValue
    private String id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private OrderDto.Status status;

    @OneToMany(cascade = ALL, mappedBy = "order")
    List<OrderItem> items;

}
