package me.roybailey.springboot.graphql.domain.annotation;

import com.fasterxml.jackson.annotation.JsonProperty;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.annotations.GraphQLType;
import lombok.Data;

import java.util.List;

@Data
@GraphQLType
@GraphQLName("ProductDto")
public class ProductQL {

    @GraphQLField
    @GraphQLName("productId")
    @JsonProperty("productId")
    private String id;

    @GraphQLField
    private String name;

    @GraphQLField
    private String brand;

    @GraphQLField
    private List<String> category;

    @GraphQLField
    private String description;

    @GraphQLField
    private double price;

}
