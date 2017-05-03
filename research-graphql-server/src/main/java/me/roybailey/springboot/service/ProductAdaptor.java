package me.roybailey.springboot.service;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.configuration.DataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Slf4j
@Service()
public class ProductAdaptor {

    @Autowired
    DataSourceProperties properties;

    @Headers("Accept: application/json")
    private interface ProductApi {

        @RequestLine("GET /product-api/v1/product")
        List<ProductDto> getAllProducts();

        @RequestLine("GET /product-api/v1/product/{id}")
        ProductDto getProductById(@Param("id") String id);

        @Headers("Content-Type: application/json")
        @RequestLine("POST /product-api/v1/product")
        ProductDto upsertProduct(ProductDto product);

        @RequestLine("DELETE /product-api/v1/product/{id}")
        ProductDto deleteProductById(@Param("id") String id);

    }

    private ProductApi api;

    @PostConstruct
    public void apiSetup() {
        log.info("Connecting {} to {}", ProductApi.class.getSimpleName(), properties.getUrlProductService());
        this.api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.BASIC)
                .target(ProductApi.class, properties.getUrlProductService());
    }


    public List<ProductDto> getAllProducts() {
        List<ProductDto> allProducts = api.getAllProducts();
        log.info("getAllProducts() : {}", allProducts);
        return allProducts;
    }

    public ProductDto getProduct(String productId) {
        ProductDto product = api.getProductById(productId);
        log.info("getProduct({}) : {}", productId, product);
        return product;
    }

    public ProductDto upsertProduct(ProductDto product) {
        ProductDto updatedProduct = api.upsertProduct(product);
        log.info("createProduct({}) : {}", product, updatedProduct);
        return updatedProduct;
    }

    public ProductDto deleteProduct(String productId) {
        ProductDto deletedProduct = api.deleteProductById(productId);
        log.info("deleteProduct({}) : {}", productId, deletedProduct);
        return deletedProduct;
    }

}

