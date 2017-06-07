package me.roybailey.springboot.configuration;

import graphql.GraphQL;
import graphql.annotations.GraphQLAnnotations;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.springboot.graphql.domain.annotation.GraphQLMutationSchema;
import me.roybailey.springboot.graphql.domain.annotation.GraphQLQuerySchema;
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

    @Bean
    public GraphQLSchema getGraphQLSchema() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        GraphQLObjectType queryObject = null;
        GraphQLObjectType mutationObject = null;
        switch (schemaType) {
            case "annotations":
                // this uses annotations over objects to define graphql schema (some limitations)
                queryObject = GraphQLAnnotations.object(GraphQLQuerySchema.class);
                mutationObject = GraphQLAnnotations.object(GraphQLMutationSchema.class);
                break;

            case "manual":
            default:
                // this uses manual schema definition (boilerplate heavy)
                queryObject = queryType;
                mutationObject = mutationType;
                break;
        }
        log.info("queryObject={}", queryObject);
        log.info("mutationObject={}", mutationObject);
        return newSchema()
                .query(queryObject)
                .mutation(mutationObject)
                .build();
    }

    @Bean
    public GraphQL createGraphQLSchema() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        return GraphQL.newGraphQL(getGraphQLSchema()).build();
    }
}
