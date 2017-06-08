package me.roybailey.springboot.configuration;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.GraphQL;
import graphql.annotations.GraphQLAnnotations;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.OrderItemDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.graphql.domain.annotation.GraphQLMutationSchema;
import me.roybailey.springboot.graphql.domain.annotation.GraphQLQuerySchema;
import me.roybailey.springboot.graphql.domain.schema.GraphQLSchemaRootResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.print.Book;
import java.util.Map;

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
    GraphQLSchemaRootResolver graphQLSchemaRootResolver;

    @Bean
    public GraphQLSchema getGraphQLSchema() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        GraphQLSchema schema = null;
        GraphQLObjectType queryObject = null;
        GraphQLObjectType mutationObject = null;
        switch (schemaType) {
            case "annotations":
                // this uses annotations over objects to define graphql schema (some limitations)
                queryObject = GraphQLAnnotations.object(GraphQLQuerySchema.class);
                mutationObject = GraphQLAnnotations.object(GraphQLMutationSchema.class);
                schema = newSchema()
                        .query(queryObject)
                        .mutation(mutationObject)
                        .build();
                break;

            case "schema":
                // this uses combination of schema file and resolvers...
                SchemaParser file = SchemaParser.newParser()
                        .file("sample-schema.graphql")
                        .resolvers(graphQLSchemaRootResolver)
                        .dictionary(
                                ProductDto.class,
                                UserDto.class,
                                OrderDto.class,
                                OrderItemDto.class
                        )
                        .build();

                file.makeExecutableSchema();
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
