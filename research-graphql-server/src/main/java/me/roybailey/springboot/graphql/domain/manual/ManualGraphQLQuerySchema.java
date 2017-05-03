package me.roybailey.springboot.graphql.domain.manual;

import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLObjectType;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.graphql.fetchers.*;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLObjectType.newObject;


public class ManualGraphQLQuerySchema {


    public static GraphQLObjectType UserType = newObject()
            .name("UserDto")
            .field(newFieldDefinition()
                    .name("userId")
                    .type(GraphQLID)
                    .dataFetcher(dfe -> ((UserDto) dfe.getSource()).getId()))
            .field(newFieldDefinition()
                    .name("title")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("firstname")
                    .type(GraphQLString)
                    .dataFetcher(dfe -> ((UserDto) dfe.getSource()).getGivenName()))
            .field(newFieldDefinition()
                    .name("lastname")
                    .type(GraphQLString)
                    .dataFetcher(dfe -> ((UserDto) dfe.getSource()).getFamilyName()))
            .field(newFieldDefinition()
                    .name("email")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("dob")
                    .type(GraphQLString)
                    .dataFetcher(dfe -> ((UserDto) dfe.getSource()).getBday().toLocalDate().toString()))
            .build();


    public static GraphQLObjectType ProductType = newObject()
            .name("ProductDto")
            .field(newFieldDefinition()
                    .name("productId")
                    .type(GraphQLID)
                    .dataFetcher(dfe -> ((ProductDto) dfe.getSource()).getId()))
            .field(newFieldDefinition()
                    .name("name")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("brand")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("description")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("category")
                    .type(list(GraphQLString)))
            .field(newFieldDefinition()
                    .name("price")
                    .type(GraphQLBigDecimal))
            .build();


    public static GraphQLObjectType OrderItemType = newObject()
            .name("OrderItemDto")
            .field(newFieldDefinition()
                    .name("productId")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("quantity")
                    .type(GraphQLInt))
            .field(newFieldDefinition()
                    .name("product")
                    .type(ProductType)
                    .dataFetcher(new OrderItemProductFetcher()))
            .build();

    public static GraphQLObjectType OrderType = newObject()
            .name("OrderDto")
            .field(newFieldDefinition()
                    .name("orderId")
                    .type(GraphQLID)
                    .dataFetcher(dfe -> ((OrderDto) dfe.getSource()).getId()))
            .field(newFieldDefinition()
                    .name("productId")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("userId")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("user")
                    .type(UserType)
                    .dataFetcher(new OrderUserFetcher()))
            .field(newFieldDefinition()
                    .name("status")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("items")
                    .type(list(OrderItemType))
                    .build())
            .build();


    public static GraphQLObjectType queryType = newObject()
            .name("QueryType")
            .field(newFieldDefinition()
                    .name("users")
                    .type(list(UserType))
                    .dataFetcher(new UserListFetcher())
                    .build())
            .field(newFieldDefinition()
                    .name("products")
                    .type(list(ProductType))
                    .dataFetcher(new ProductListFetcher())
                    .build())
            .field(newFieldDefinition()
                    .name("orders")
                    .type(list(OrderType))
                    .dataFetcher(new OrderListFetcher())
                    .build())
            .field(newFieldDefinition()
                    .name("user")
                    .argument(GraphQLArgument.newArgument()
                            .name("userId")
                            .type(GraphQLString)
                            .build())
                    .type(UserType)
                    .dataFetcher(new UserFetcher())
                    .build())
            .field(newFieldDefinition()
                    .name("product")
                    .argument(GraphQLArgument.newArgument()
                            .name("productId")
                            .type(GraphQLString)
                            .build())
                    .type(ProductType)
                    .dataFetcher(new ProductFetcher())
                    .build())
            .build();

}
