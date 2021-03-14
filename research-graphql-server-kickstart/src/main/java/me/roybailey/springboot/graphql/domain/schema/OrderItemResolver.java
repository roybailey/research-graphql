package me.roybailey.springboot.graphql.domain.schema;


import graphql.kickstart.tools.GraphQLResolver;
import me.roybailey.data.schema.OrderItemDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.service.ProductAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class contains resolver methods for the "Data Class" type
 */
@Service
public class OrderItemResolver implements GraphQLResolver<OrderItemDto> {

    @Autowired
    ProductAdaptor productAdaptor;

    public ProductDto getProduct(OrderItemDto item) {
        return productAdaptor.getProduct(item.getProductId());
    }
}
