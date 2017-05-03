package me.roybailey.springboot.graphql;

import feign.Headers;
import feign.RequestLine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;


@Slf4j
@SuppressWarnings("unchecked")
public class GraphQLUntypedTest extends AbstractControllerTestCase {

    @Headers("Accept: application/json")
    public interface UntypedApi {

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse query(GraphQLRequest query);
    }

    private UntypedApi untypedApi;

    @Before
    public void apiSetup() {
        this.untypedApi = apiSetup(UntypedApi.class);
    }


    @Test
    public void test1_ProductNames_UserEmails() {
        GraphQLResponse response = untypedApi.query(GraphQLRequest.build("{products {name}, users {email}}"));
        softly.then(response.getData()).isNotNull();

        Map<String,Object> data = (Map<String, Object>) response.getData();
        softly.then(data).isNotNull();
        log.info("data="+data);

        List<Map<String,Object>> products = (List<Map<String, Object>>) data.get("products");
        softly.then(products).isNotNull().hasSize(3);

        List<Map<String,Object>> users = (List<Map<String, Object>>) data.get("users");
        softly.then(users).isNotNull().hasSize(3);
    }


    @Test
    public void test2_ProductById() {
        GraphQLResponse response = untypedApi.query(GraphQLRequest.build("{product(productId:\"P3\") {name}}"));
        softly.then(response.getData()).isNotNull();

        Map<String,Object> data = (Map<String, Object>) response.getData();
        softly.then(data).isNotNull();
        log.info("data="+data);
    }
}


