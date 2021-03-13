package me.roybailey.springboot.graphql;

import feign.Headers;
import feign.RequestLine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class GraphQLVariablesTest extends AbstractControllerTestCase {

    public static final String QUERY = "query getFewItems($varProductId: String!, $varUserId: String!) {" +
            "  product(productId: $varProductId) {" +
            "    productId" +
            "    name" +
            "  }" +
            "  user(userId: $varUserId) {" +
            "    firstname" +
            "    lastname" +
            "    email" +
            "  }" +
            "}";

    Map<String, Object> VARIABLES  = new HashMap<String, Object>() {{
        put("varUserId", "U1");
        put("varProductId", "P2");
    }};

    @Data
    private static class ProductResult {
        String productId;
        String name;
    }

    @Data
    private static class UserResult {
        String userId;
        String firstname;
        String lastname;
        String email;
    }

    @Data
    private static class GetFewItemsWithVariables {
        ProductResult product;
        UserResult user;
    }

    @Headers("Accept: application/json")
    public interface TestApi {

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<GetFewItemsWithVariables> queryProduct(GraphQLRequest query);

    }

    private TestApi api;

    @Before
    public void apiSetup() {
        this.api = apiSetup(TestApi.class);
    }


    @Test
    public void test1_FewItemsWithVariables() {
        GraphQLRequest request = GraphQLRequest.builder().query(QUERY).variables(VARIABLES).build();
        GraphQLResponse<GetFewItemsWithVariables> response = api.queryProduct(request);
        assertThat(response.getData()).isNotNull();

        GetFewItemsWithVariables data = response.getData();
        assertThat(data).isNotNull();
        log.info("data="+data);

        softly.then(data.getUser())
                .isNotNull()
                .hasFieldOrPropertyWithValue("firstname", "user-1")
                .hasFieldOrPropertyWithValue("lastname", "test")
                .hasFieldOrPropertyWithValue("email", "user1@acme.com");

        softly.then(data.getProduct())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "product-2");
    }

}

