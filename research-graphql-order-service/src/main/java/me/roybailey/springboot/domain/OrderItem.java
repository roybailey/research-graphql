package me.roybailey.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "ORDERFORM_ID", nullable = false)
    private OrderForm order;

    private String productId;

    private Integer quantity;

}
