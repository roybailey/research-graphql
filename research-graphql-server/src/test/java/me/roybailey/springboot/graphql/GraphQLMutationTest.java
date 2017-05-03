package me.roybailey.springboot.graphql;

import feign.Headers;
import feign.RequestLine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class GraphQLMutationTest extends AbstractControllerTestCase {

    public static final String MUTATION_CREATE_PRODUCT_WITH_ARGUMENTS = "mutation { " +
            "  product: createProduct(name: \"test1\", price: 5.50) { " +
            "    productId, name, price " +
            "  } " +
            "}";

    public static final String MUTATION_CREATE_PRODUCT_WITH_OBJECT = "mutation { " +
            "  product: createProductObject(product: {name: \"test2\", price: 10.99}) { " +
            "    productId, name, price " +
            "  } " +
            "}";

    public static final Function<String, String> MUTATION_DELETE =
            (String productId) -> String.format("mutation { " +
            "  product: deleteProduct(productId: \"%s\") { " +
            "    productId, name, price " +
            "  } " +
            "}", productId);

    @Data
    private static class ProductResult {
        String productId;
        String name;
        BigDecimal price;
    }

    @Data
    private static class MutationResult {
        ProductResult product;
    }

    @Headers("Accept: application/json")
    public interface TestApi {

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<MutationResult> createProductWithArguments(GraphQLRequest query);

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<MutationResult> createProductWithObject(GraphQLRequest query);

        @RequestLine("POST /graphql")
        @Headers("Content-Type: application/json")
        GraphQLResponse<MutationResult> deleteProduct(GraphQLRequest query);

    }

    private TestApi api;

    @Before
    public void apiSetup() {
        this.api = apiSetup(TestApi.class);
    }


    @Test
    public void test1_ProductCreationWithArguments() {

        // create a product using arguments for field values...
        GraphQLResponse<MutationResult> createResponse =
                api.createProductWithArguments(GraphQLRequest.build(MUTATION_CREATE_PRODUCT_WITH_ARGUMENTS));
        assertThat(createResponse.getData()).isNotNull();

        MutationResult createdData = createResponse.getData();
        assertThat(createdData).isNotNull();
        log.info("createdData=" + createdData);

        ProductResult createdProduct = createdData.getProduct();
        softly.then(createdProduct)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("brand", "description", "category")
                .hasFieldOrPropertyWithValue("name", "test1")
                .hasFieldOrPropertyWithValue("price", BigDecimal.valueOf(5.50));

        // delete our created product using productId returned from creation...
        GraphQLResponse<MutationResult> deleteResponse =
                api.deleteProduct(GraphQLRequest.build(MUTATION_DELETE.apply(createdProduct.getProductId())));
        assertThat(deleteResponse.getData()).isNotNull();

        MutationResult deleteData = createResponse.getData();
        assertThat(deleteData).isNotNull();
        log.info("deleteData=" + deleteData);

    }

    @Test
    public void test1_ProductCreationWithObject() {

        // create a product using template product object values...
        GraphQLResponse<MutationResult> createResponse =
                api.createProductWithObject(GraphQLRequest.build(MUTATION_CREATE_PRODUCT_WITH_OBJECT));
        assertThat(createResponse.getData()).isNotNull();

        MutationResult createdData = createResponse.getData();
        assertThat(createdData).isNotNull();
        log.info("createdData=" + createdData);

        ProductResult createdProduct = createdData.getProduct();
        softly.then(createdProduct)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("brand", "description", "category")
                .hasFieldOrPropertyWithValue("name", "test2")
                .hasFieldOrPropertyWithValue("price", BigDecimal.valueOf(10.99));

        // delete our created product using productId returned from creation...
        GraphQLResponse<MutationResult> deleteResponse =
                api.deleteProduct(GraphQLRequest.build(MUTATION_DELETE.apply(createdProduct.getProductId())));
        assertThat(deleteResponse.getData()).isNotNull();

        MutationResult deletedData = createResponse.getData();
        assertThat(deletedData).isNotNull();
        log.info("deletedData=" + deletedData);

    }

}

