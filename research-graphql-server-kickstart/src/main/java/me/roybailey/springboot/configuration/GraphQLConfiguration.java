package me.roybailey.springboot.configuration;

import graphql.GraphQL;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.OrderItemDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.graphql.domain.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static graphql.schema.GraphQLSchema.newSchema;


@Slf4j
@Configuration
public class GraphQLConfiguration {

    @Value("${graphql.schema:manual}")
    String schemaType;

    @Autowired
    @Qualifier("QueryType")
    GraphQLObjectType queryType;

    @Autowired
    @Qualifier("MutationType")
    GraphQLObjectType mutationType;

    @Autowired
    Query queryRootResolver;

    @Autowired
    Mutation mutationRootResolver;

    @Autowired
    ProductResolver productResolver;

    @Autowired
    UserResolver userResolver;

    @Autowired
    OrderResolver orderResolver;

    @Autowired
    OrderItemResolver orderItemResolver;

    @Bean
    public GraphQLSchema getGraphQLSchema() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        GraphQLSchema schema = null;
        GraphQLObjectType queryObject = null;
        GraphQLObjectType mutationObject = null;
        schemaType = System.getProperty("graphql.schema", schemaType);
        log.info("Loading GraphQL schema type " + schemaType.toUpperCase());
        switch (schemaType) {

            case "schema":
                // this uses combination of schema file and resolvers...
                SchemaParser file = SchemaParser.newParser()
                        .file("schema.graphqls")
                        .resolvers(
                                queryRootResolver,
                                mutationRootResolver,
                                productResolver,
                                userResolver,
                                orderResolver,
                                orderItemResolver
                        )
                        .dictionary(
                                ProductDtoInput.class,
                                ProductDto.class,
                                UserDto.class,
                                OrderDto.class,
                                OrderItemDto.class
                        )
                        .build();

                schema = file.makeExecutableSchema();
                break;

            case "manual":
            default:
                // this uses manual schema definition (boilerplate heavy)
                queryObject = queryType;
                mutationObject = mutationType;
                schema = newSchema()
                        .query(queryObject)
                        .mutation(mutationObject)
                        .build();
                break;
        }
        log.info("schema={}", schema);
        return schema;
    }

    @Bean
    public GraphQL createGraphQLSchema() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        return GraphQL.newGraphQL(getGraphQLSchema()).build();
    }
}
