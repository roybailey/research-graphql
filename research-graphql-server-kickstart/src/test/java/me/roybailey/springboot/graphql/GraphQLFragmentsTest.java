package me.roybailey.springboot.graphql;

import feign.Headers;
import feign.RequestLine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class GraphQLFragmentsTest extends AbstractControllerTestCase {

    public static final String QUERY = "{" +
            "  desk: product(productId: \"P1\") {" +
            "    ...productInfo" +
            "    category" +
            "  }," +
            "  chair: product(productId: \"P2\") {" +
            "    ...productInfo" +
            "  }," +
            "  table: product(productId: \"P3\") {" +
            "    ...productInfo" +
            "  }" +
            "}" +
            "" +
            "fragment productInfo on ProductDto {" +
            "  productId," +
            "  name," +
            "  price" +
            "}";

    @Data
    private static class ProductResult {
        String productId;
        String name;
        BigDecimal price;
        List<String> category;
    }

    @Data
    private static class GetFewItemsWithFragments {
        ProductResult desk;
        ProductResult chair;
        ProductResult table;
    }

    @Headers("Accept: application/json")
    public interface TestApi {

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<GetFewItemsWithFragments> queryProduct(GraphQLRequest query);

    }

    private TestApi api;

    @Before
    public void apiSetup() {
        this.api = apiSetup(TestApi.class);
    }


    @Test
    public void test1_FewItemsWithVariables() {
        GraphQLRequest request = GraphQLRequest.builder().query(QUERY).build();
        GraphQLResponse<GetFewItemsWithFragments> response = api.queryProduct(request);
        assertThat(response.getData()).isNotNull();

        GetFewItemsWithFragments data = response.getData();
        assertThat(data).isNotNull();
        log.info("data="+data);

        softly.then(data.getDesk())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "product-1");

        softly.then(data.getChair())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "product-2");

        softly.then(data.getTable())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "product-3");
    }

}

