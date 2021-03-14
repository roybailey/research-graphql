package me.roybailey.springboot.graphql.domain.schema;


import graphql.kickstart.tools.GraphQLResolver;
import me.roybailey.data.schema.ProductDto;
import org.springframework.stereotype.Service;

/**
 * This class contains resolver methods for the "Data Class" type
 */
@Service
public class ProductResolver implements GraphQLResolver<ProductDto> {

    public String getProductId(ProductDto product) {
        return product.getId();
    }

}
