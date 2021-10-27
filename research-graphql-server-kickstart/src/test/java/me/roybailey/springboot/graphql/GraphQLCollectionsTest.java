package me.roybailey.springboot.graphql;

import feign.Headers;
import feign.RequestLine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class GraphQLCollectionsTest extends AbstractControllerTestCase {

    private static final String QUERY_ALL_PRODUCTS = "{products {name}}";
    public static final String QUERY_ALL_USERS = "{users {email}}";
    public static final String QUERY_ALL_PRODUCTS_AND_USERS = "{products {name}, users {email}}";

    @Data
    private static class ProductResult {
        String name;
    }

    @Data
    private static class UserResult {
        String email;
    }

    @Data
    private static class CollectionResult {
        List<ProductResult> products;
        List<UserResult> users;
    }

    @Headers("Accept: application/json")
    private interface TestApi {

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<CollectionResult> query(GraphQLRequest query);
    }

    private TestApi api;

    @Before
    public void apiSetup() {
        this.api = apiSetup(TestApi.class);
    }


    @Test
    public void test1_ProductNames() {
        GraphQLResponse<CollectionResult> response = api.query(GraphQLRequest.build(QUERY_ALL_PRODUCTS));
        assertThat(response.getData()).isNotNull();

        CollectionResult data = response.getData();
        assertThat(data).isNotNull();

        List<ProductResult> products = data.getProducts();
        softly.then(products).isNotNull().hasSize(3);
    }


    @Test
    public void test2_UserEmails() {
        GraphQLResponse<CollectionResult> response = api.query(GraphQLRequest.build(QUERY_ALL_USERS));
        assertThat(response.getData()).isNotNull();

        CollectionResult data = response.getData();
        assertThat(data).isNotNull();

        List<UserResult> users = data.getUsers();
        softly.then(users).isNotNull().hasSize(3);
    }


    @Test
    public void test3_ProductNames_UserEmails() {
        GraphQLResponse<CollectionResult> response = api.query(GraphQLRequest.build(QUERY_ALL_PRODUCTS_AND_USERS));
        assertThat(response.getData()).isNotNull();

        CollectionResult data = response.getData();
        assertThat(data).isNotNull();

        List<ProductResult> products = data.getProducts();
        softly.then(products).isNotNull().hasSize(3);

        List<UserResult> users = data.getUsers();
        softly.then(users).isNotNull().hasSize(3);
    }
}

