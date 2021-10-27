package me.roybailey.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;

    private String name;

    private String brand;

    private String description;

    private BigDecimal price;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="PRODUCT_CATEGORY",
            joinColumns=@JoinColumn(name="PRODUCT_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="CATEGORY_ID", referencedColumnName="ID"))
    private List<Category> categories;
}
