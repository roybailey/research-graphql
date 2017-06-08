package me.roybailey.springboot.graphql.domain.manual;

import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLObjectType;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.graphql.fetchers.*;
import me.roybailey.springboot.service.OrderAdaptor;
import me.roybailey.springboot.service.ProductAdaptor;
import me.roybailey.springboot.service.UserAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLObjectType.newObject;


@Configuration
public class ManualGraphQLQuerySchema {

    @Autowired
    ProductAdaptor productAdaptor;

    @Autowired
    OrderAdaptor orderAdaptor;

    @Autowired
    UserAdaptor userAdaptor;


    @Bean
    @Qualifier("UserType")
    public GraphQLObjectType getUserType() {
        return newObject()
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
    }

    @Bean
    @Qualifier("ProductType")
    public GraphQLObjectType getProductType() {
        return newObject()
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
    }

    @Bean
    @Qualifier("OrderItemType")
    public GraphQLObjectType getOrderItemType() {
        return newObject()
                .name("OrderItemDto")
                .field(newFieldDefinition()
                        .name("productId")
                        .type(GraphQLString))
                .field(newFieldDefinition()
                        .name("quantity")
                        .type(GraphQLInt))
                .field(newFieldDefinition()
                        .name("product")
                        .type(getProductType())
                        .dataFetcher(new OrderItemProductFetcher(productAdaptor)))
                .build();
    }

    @Bean
    @Qualifier("OrderType")
    public GraphQLObjectType getOrderType() {
        return newObject()
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
                        .type(getUserType())
                        .dataFetcher(new OrderUserFetcher(userAdaptor)))
                .field(newFieldDefinition()
                        .name("status")
                        .type(GraphQLString))
                .field(newFieldDefinition()
                        .name("items")
                        .type(list(getOrderItemType()))
                        .build())
                .build();
    }

    @Bean
    @Qualifier("QueryType")
    public GraphQLObjectType getQueryType() {
        return newObject()
                .name("QueryType")
                .field(newFieldDefinition()
                        .name("users")
                        .type(list(getUserType()))
                        .dataFetcher(new UserListFetcher(userAdaptor))
                        .build())
                .field(newFieldDefinition()
                        .name("products")
                        .type(list(getProductType()))
                        .dataFetcher(new ProductListFetcher(productAdaptor))
                        .build())
                .field(newFieldDefinition()
                        .name("orders")
                        .type(list(getOrderType()))
                        .dataFetcher(new OrderListFetcher(orderAdaptor))
                        .build())
                .field(newFieldDefinition()
                        .name("user")
                        .argument(GraphQLArgument.newArgument()
                                .name("userId")
                                .type(GraphQLString)
                                .build())
                        .type(getUserType())
                        .dataFetcher(new UserFetcher(userAdaptor))
                        .build())
                .field(newFieldDefinition()
                        .name("product")
                        .argument(GraphQLArgument.newArgument()
                                .name("productId")
                                .type(GraphQLString)
                                .build())
                        .type(getProductType())
                        .dataFetcher(new ProductFetcher(productAdaptor))
                        .build())
                .build();
    }
}
