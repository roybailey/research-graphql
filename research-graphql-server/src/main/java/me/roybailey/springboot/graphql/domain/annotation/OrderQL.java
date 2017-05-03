package me.roybailey.springboot.graphql.domain.annotation;

import com.fasterxml.jackson.annotation.JsonProperty;
import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.annotations.GraphQLType;
import lombok.Data;
import me.roybailey.springboot.graphql.fetchers.OrderUserFetcher;

import java.util.List;

@Data
@GraphQLType
@GraphQLName("OrderDto")
public class OrderQL {

    @GraphQLField
    @GraphQLName("orderId")
    @JsonProperty("orderId")
    private String id;

    @GraphQLField
    private String userId;

    @GraphQLField
    @GraphQLDataFetcher(OrderUserFetcher.class)
    public UserQL user() {
        return null;
    }

    @GraphQLField
    private String status;

    @GraphQLField
    private List<OrderItemQL> items;

}
