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
public class GraphQLAliasTest extends AbstractControllerTestCase {

    public static final String QUERY_PRODUCT_ALIASES = "{" +
            "firstProduct: product(productId:\"P3\") {productId, name}, " +
            "secondProduct: product(productId:\"P1\") {productId, name}" +
            "}";

    public static final String QUERY_PRODUCT_ASSIGNMENT = "{" +
            "primaryUser: user(userId:\"U3\") {userId, email}," +
            "secondaryUser: user(userId:\"U1\") {userId, email}" +
            "}";

    @Data
    private static class ProductResult {
        String productId;
        String name;
        BigDecimal price;
    }

    @Data
    private static class ProductPack {
        ProductResult firstProduct;
        ProductResult secondProduct;
    }

    @Data
    private static class UserResult {
        String userId;
        String email;
    }

    @Data
    private static class UserAssignment {
        UserResult primaryUser;
        UserResult secondaryUser;
    }

    @Headers("Accept: application/json")
    public interface TestApi {

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<ProductPack> queryProductPack(GraphQLRequest query);

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<UserAssignment> queryUserAssignment(GraphQLRequest query);
    }

    private TestApi api;

    @Before
    public void apiSetup() {
        this.api = apiSetup(TestApi.class);
    }


    @Test
    public void test1_ProductPackAliases() {
        GraphQLResponse<ProductPack> response =
                api.queryProductPack(GraphQLRequest.build(QUERY_PRODUCT_ALIASES));
        assertThat(response.getData()).isNotNull();

        ProductPack data = response.getData();
        assertThat(data).isNotNull();
        log.info("data=" + data);

        softly.then(data.getFirstProduct()).isNotNull();
        softly.then(data.getSecondProduct()).isNotNull();
    }


    @Test
    public void test2_UserAssignmentAliases() {
        GraphQLResponse<UserAssignment> response =
                api.queryUserAssignment(GraphQLRequest.build(QUERY_PRODUCT_ASSIGNMENT));
        assertThat(response.getData()).isNotNull();

        UserAssignment data = response.getData();
        assertThat(data).isNotNull();
        log.info("data=" + data);

        softly.then(data.getPrimaryUser()).isNotNull();
        softly.then(data.getSecondaryUser()).isNotNull();
    }
}

