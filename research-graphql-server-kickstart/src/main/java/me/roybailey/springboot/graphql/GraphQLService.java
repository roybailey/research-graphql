package me.roybailey.springboot.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class GraphQLService {

    @Autowired
    private GraphQL graphQL;

    @Autowired
    private ObjectMapper argumentsMapper;


    private static final Map<String, Object> EMPTY_VARIABLES = new HashMap<>();

    public Map<String, Object> getArgumentsFromVariableString(String variables) {
        try {
            return (StringUtils.isEmpty(variables)? EMPTY_VARIABLES :
                    argumentsMapper.readValue(variables, EMPTY_VARIABLES.getClass()));
        } catch (Exception err) {
            throw new RuntimeException("Failed to parse arguments map from variables: " + variables, err);
        }
    }


    public GraphQLResponse<?> execute(GraphQLRequest request, Object context) {
        GraphQLResponse.GraphQLResponseBuilder<?> qlResponseBuilder = GraphQLResponse.builder();
        ExecutionResult executionResult = graphQL.execute(
                request.getQuery(),
                context,
                (request.getVariables() == null)? Collections.emptyMap() : request.getVariables()
        );
        if (executionResult.getErrors().size() > 0) {
            log.error("GraphQL query error: " + executionResult.getErrors());
            // trim errors to one-liners as returning the execution results sends the entire stack trace back
            List<Map<String, ?>> errors = executionResult.getErrors().stream()
                    .map((err) -> ImmutableMap.of("errorType", err.getErrorType(), "message", err.getMessage()))
                    .collect(Collectors.toList());
            qlResponseBuilder.errors(errors);
        }
        qlResponseBuilder.data(executionResult.getData());
        return qlResponseBuilder.build();
    }
}
