package me.roybailey.springboot.graphql.domain.manual;

import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLObjectType;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.graphql.fetchers.ProductDeleteFetcher;
import me.roybailey.springboot.graphql.fetchers.ProductUpsertFetcher;
import me.roybailey.springboot.service.OrderAdaptor;
import me.roybailey.springboot.service.ProductAdaptor;
import me.roybailey.springboot.service.UserAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;


@Configuration
public class ManualGraphQLMutationSchema {

    @Autowired
    ProductAdaptor productAdaptor;

    @Autowired
    @Qualifier("ProductType")
    GraphQLObjectType productType;


    @Bean
    @Qualifier("InputProductType")
    GraphQLInputType getInputProductType() {
        return newInputObject()
                .name("InputProductDto")
                .field(newInputObjectField()
                        .name("productId")
                        .type(GraphQLID))
                .field(newInputObjectField()
                        .name("name")
                        .type(GraphQLString))
                .field(newInputObjectField()
                        .name("brand")
                        .type(GraphQLString))
                .field(newInputObjectField()
                        .name("description")
                        .type(GraphQLString))
                .field(newInputObjectField()
                        .name("category")
                        .type(list(GraphQLString)))
                .field(newInputObjectField()
                        .name("price")
                        .type(GraphQLFloat))
                .build();
    }

    @Bean
    @Qualifier("MutationType")
    public GraphQLObjectType getMutationType() {
        return newObject()
                .name("MutationType")
                .field(newFieldDefinition()
                        .name("createProduct")
                        .type(productType)
                        .argument(GraphQLArgument.newArgument()
                                .name("name")
                                .type(GraphQLString)
                                .build())
                        .argument(GraphQLArgument.newArgument()
                                .name("price")
                                .type(GraphQLFloat)
                                .build())
                        .dataFetcher(new ProductUpsertFetcher(productAdaptor))
                        .build())
                .field(newFieldDefinition()
                        .name("createProductObject")
                        .type(productType)
                        .argument(GraphQLArgument.newArgument()
                                .name("product")
                                .type(getInputProductType())
                                .build())
                        .dataFetcher(new ProductUpsertFetcher(productAdaptor))
                        .build())
                .field(newFieldDefinition()
                        .name("deleteProduct")
                        .type(productType)
                        .argument(GraphQLArgument.newArgument()
                                .name("productId")
                                .type(GraphQLID)
                                .build())
                        .dataFetcher(new ProductDeleteFetcher(productAdaptor))
                        .build())
                .build();
    }

}
