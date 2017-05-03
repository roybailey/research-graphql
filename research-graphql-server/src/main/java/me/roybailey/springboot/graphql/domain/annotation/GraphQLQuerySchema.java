package me.roybailey.springboot.graphql.domain.annotation;

import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import lombok.Builder;
import lombok.Data;
import me.roybailey.springboot.graphql.fetchers.*;

import java.util.List;


@Data
@Builder
public class GraphQLQuerySchema {

    @GraphQLField
    @GraphQLDataFetcher(ProductListFetcher.class)
    List<ProductQL> products;

    @GraphQLField
    @GraphQLDataFetcher(UserListFetcher.class)
    List<UserQL> users;

    @GraphQLField
    @GraphQLDataFetcher(ProductFetcher.class)
    public ProductQL product(@GraphQLName("productId") String productId) {
        return null;
    }

    @GraphQLField
    @GraphQLDataFetcher(UserFetcher.class)
    public UserQL user(@GraphQLName("userId") String userId) {
        return null;
    }

    @GraphQLField
    @GraphQLDataFetcher(OrderListFetcher.class)
    List<OrderQL> orders;

}


