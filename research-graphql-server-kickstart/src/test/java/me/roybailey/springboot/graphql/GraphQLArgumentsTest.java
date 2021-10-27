package me.roybailey.springboot.graphql;

import feign.Headers;
import feign.RequestLine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class GraphQLArgumentsTest extends AbstractControllerTestCase {

    public static final String QUERY_PRODUCT_BY_ID = "{product(productId:\"P3\") {name}}";

    public static final String QUERY_USER_BY_ID = "{user(userId:\"U3\") {email}}";


    @Data
    private static class ProductResult {
        String name;
    }

    @Data
    private static class UserResult {
        String email;
    }

    @Data
    private static class ProductByIdResult {
        ProductResult product;
    }

    @Data
    private static class UserByIdResult {
        UserResult user;
    }

    @Headers("Accept: application/json")
    public interface TestApi {

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<ProductByIdResult> queryProduct(GraphQLRequest query);

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<UserByIdResult> queryUser(GraphQLRequest query);
    }

    private TestApi api;

    @Before
    public void apiSetup() {
        this.api = apiSetup(TestApi.class);
    }


    @Test
    public void test1_ProductById() {
        GraphQLResponse<ProductByIdResult> response = api.queryProduct(GraphQLRequest.build(QUERY_PRODUCT_BY_ID));
        assertThat(response.getData()).isNotNull();

        ProductByIdResult data = response.getData();
        assertThat(data).isNotNull();
        log.info("data="+data);

        softly.then(data.getProduct())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "product-3");
    }


    @Test
    public void test2_UserById() {
        GraphQLResponse<UserByIdResult> response = api.queryUser(GraphQLRequest.build(QUERY_USER_BY_ID));
        assertThat(response.getData()).isNotNull();

        UserByIdResult data = response.getData();
        assertThat(data).isNotNull();
        log.info("data="+data);

        softly.then(data.getUser())
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", "user3@acme.com");
    }
}

