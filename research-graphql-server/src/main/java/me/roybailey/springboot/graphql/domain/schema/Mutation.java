package me.roybailey.springboot.graphql.domain.schema;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.service.ProductAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class Mutation implements GraphQLRootResolver {

    @Autowired
    ProductAdaptor productAdaptor;

    public ProductDto createProduct(String name, BigDecimal price) {
        ProductDto product = ProductDto.builder().name(name).price(price).build();
        return productAdaptor.upsertProduct(product);
    }

    public ProductDto createProductObject(ProductDtoInput product) {
        return productAdaptor.upsertProduct(product);
    }

    public ProductDto deleteProduct(String productId) {
        return productAdaptor.deleteProduct(productId);
    }

}
