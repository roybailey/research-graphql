package me.roybailey.springboot.graphql.domain.manual;

import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLObjectType;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.graphql.fetchers.ProductDeleteFetcher;
import me.roybailey.springboot.graphql.fetchers.ProductUpsertFetcher;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;


public class ManualGraphQLMutationSchema {


    public static GraphQLInputType InputProductType = newInputObject()
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


    public static GraphQLObjectType mutationType = newObject()
            .name("MutationType")
            .field(newFieldDefinition()
                    .name("createProduct")
                    .type(ManualGraphQLQuerySchema.ProductType)
                    .argument(GraphQLArgument.newArgument()
                            .name("name")
                            .type(GraphQLString)
                            .build())
                    .argument(GraphQLArgument.newArgument()
                            .name("price")
                            .type(GraphQLFloat)
                            .build())
                    .dataFetcher(new ProductUpsertFetcher())
                    .build())
            .field(newFieldDefinition()
                    .name("createProductObject")
                    .type(ManualGraphQLQuerySchema.ProductType)
                    .argument(GraphQLArgument.newArgument()
                            .name("product")
                            .type(InputProductType)
                            .build())
                    .dataFetcher(new ProductUpsertFetcher())
                    .build())
            .field(newFieldDefinition()
                    .name("deleteProduct")
                    .type(ManualGraphQLQuerySchema.ProductType)
                    .argument(GraphQLArgument.newArgument()
                            .name("productId")
                            .type(GraphQLID)
                            .build())
                    .dataFetcher(new ProductDeleteFetcher())
                    .build())
            .build();

}
