package me.roybailey.springboot.graphql.domain.annotation;

import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLMutation;
import graphql.annotations.GraphQLName;
import me.roybailey.springboot.graphql.fetchers.ProductDeleteFetcher;
import me.roybailey.springboot.graphql.fetchers.ProductUpsertFetcher;

import java.util.List;


@GraphQLMutation
public class GraphQLMutationSchema {

    @GraphQLField
    @GraphQLDataFetcher(ProductUpsertFetcher.class)
    public ProductQL createProduct(
            @GraphQLName("name") String name,
            @GraphQLName("brand") String brand,
            @GraphQLName("category") List<String> category,
            @GraphQLName("description") String description,
            @GraphQLName("price") double price
    ) {
        return null;
    }

//    @GraphQLField
//    @GraphQLDataFetcher(ProductUpsertFetcher.class)
//    public ProductQL createProductObject(@GraphQLName("product") InputProductQL product) {
//        return null;
//    }

    @GraphQLField
    @GraphQLDataFetcher(ProductUpsertFetcher.class)
    public ProductQL updateProductObject(@GraphQLName("product") InputProductQL product) {
        return null;
    }

    @GraphQLField
    @GraphQLDataFetcher(ProductDeleteFetcher.class)
    public ProductQL deleteProduct(@GraphQLName("productId") String productId) { return null; }

}


