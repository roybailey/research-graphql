package me.roybailey.springboot.graphql.domain.annotation;

import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLType;
import lombok.Data;

import java.util.List;

@Data
@GraphQLType
public class InputProductQL {

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
