package me.roybailey.springboot.graphql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphQLRequest {

    private static Map<String, Object> EMPTY_VARIABLES = new HashMap<>();

    public static GraphQLRequest build(String query) {
        return builder()
                .query(query)
                .build();
    }

    public String query;
    public Map<String,Object> variables = Collections.emptyMap();
    public String operationName = "";

}


