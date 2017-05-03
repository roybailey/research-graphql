package me.roybailey.springboot.controller;


import com.google.common.collect.ImmutableList;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.ProductServiceApplication;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = ProductServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    int port;

    @Rule
    public TestName name = new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    @Headers("Accept: application/json")
    public interface ProductApi {

        @RequestLine("GET /product-api/v1/product")
        List<ProductDto> getAllProducts();

        @RequestLine("GET /product-api/v1/product/{id}")
        ProductDto getProduct(@Param("id") String id);

        @Headers("Content-Type: application/json")
        @RequestLine("POST /product-api/v1/product")
        ProductDto saveProduct(ProductDto newProduct);

        @RequestLine("DELETE /product-api/v1/product/{id}")
        Response deleteProduct(@Param("id") String id);
    }

    private ProductApi api;

    @Before
    public void apiSetup() {
        this.api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.BASIC)
                .target(ProductApi.class, "http://localhost:" + port);
    }

    @Test
    public void test1_ProductApi() {

        List<ProductDto> allProducts = api.getAllProducts();
        assertThat(allProducts).isNotNull();
        softly.then(allProducts).hasSize(3);

        ProductDto expected = allProducts.get(1);
        ProductDto actual = api.getProduct(expected.getId());
        softly.then(actual)
                .isNotNull()
                .isEqualToComparingFieldByField(expected);
        softly.then(actual.getCategory()).hasSize(2);
    }

    @Test
    public void test2_ProductApiUpdates() {

        List<ProductDto> allProducts = api.getAllProducts();
        assertThat(allProducts)
                .isNotNull()
                .hasSize(3);

        ProductDto newProduct = ProductDto.builder()
                .name("extra")
                .price(BigDecimal.TEN)
                .description("test product description")
                .category(ImmutableList.of("Discounted", "Featured"))
                .build();
        ProductDto savedProduct = api.saveProduct(newProduct);
        softly.then(savedProduct)
                .isNotNull()
                .isEqualToComparingOnlyGivenFields(newProduct, "name", "price", "description");
        softly.then(savedProduct.getId()).isNotNull();
        softly.then(savedProduct.getCategory())
                .isNotNull()
                .hasSize(2)
                .contains("Discounted", "Featured");

        allProducts = api.getAllProducts();
        softly.then(allProducts)
                .isNotNull()
                .hasSize(4);

        Response response = api.deleteProduct(savedProduct.getId());
        softly.then(response.status()).isEqualTo(200);

        allProducts = api.getAllProducts();
        softly.then(allProducts)
                .isNotNull()
                .hasSize(3);
    }
}
