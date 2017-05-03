package me.roybailey.springboot.graphql;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class GraphQLResponse<T> {

    public T data;
    public List errors;
}


