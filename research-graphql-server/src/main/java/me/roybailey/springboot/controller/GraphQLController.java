package me.roybailey.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.springboot.graphql.GraphQLRequest;
import me.roybailey.springboot.graphql.GraphQLResponse;
import me.roybailey.springboot.graphql.GraphQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class GraphQLController {

    @Autowired
    private GraphQLService graphQLService;

    @RequestMapping(value = "/graphql", method = RequestMethod.POST)
    public GraphQLResponse<?> graphql(@RequestHeader HttpHeaders headers, @RequestBody GraphQLRequest graphql) throws Exception {
        log.info("GraphQLRequest=" + graphql);
        GraphQLResponse<?> execute = graphQLService.execute(graphql, headers);
        return execute;
    }
}
