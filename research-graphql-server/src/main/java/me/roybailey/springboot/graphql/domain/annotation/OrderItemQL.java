package me.roybailey.springboot.graphql.domain.annotation;

import com.fasterxml.jackson.annotation.JsonProperty;
import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.annotations.GraphQLType;
import lombok.Data;
import me.roybailey.springboot.graphql.fetchers.OrderItemProductFetcher;

@Data
@GraphQLType
@GraphQLName("OrderItemDto")
public class OrderItemQL {

    @GraphQLField
    @GraphQLName("orderItemId")
    @JsonProperty("orderItemId")
    private String id;

    @GraphQLField
    private String productId;

    @GraphQLField
    private Integer quantity;

    @GraphQLField
    @GraphQLDataFetcher(OrderItemProductFetcher.class)
    public ProductQL product() {
        return null;
    }

}
