package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderItemDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.service.ProductAdaptor;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
public class OrderItemProductFetcher implements DataFetcher<ProductDto> {

    final ProductAdaptor productAdaptor;

    public OrderItemProductFetcher(ProductAdaptor productAdaptor) {
        this.productAdaptor = productAdaptor;
    }

    @Override
    public ProductDto get(DataFetchingEnvironment environment) {
        log.info("getSource {}", (Object)environment.getSource());
        OrderItemDto orderItemDto = environment.getSource();
        ProductDto product = productAdaptor.getProduct(orderItemDto.getProductId());
        return product;
    }
}
