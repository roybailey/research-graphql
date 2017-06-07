package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.service.ProductAdaptor;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
public class ProductDeleteFetcher implements DataFetcher<ProductDto> {

    ProductAdaptor productAdaptor;

    public ProductDeleteFetcher(ProductAdaptor productAdaptor) {
        this.productAdaptor = productAdaptor;
    }

    @Override
    public ProductDto get(DataFetchingEnvironment environment) {
        log.info("getArguments {}", environment.getArguments());
        String productId = environment.getArgument("productId");
        ProductDto deletedProduct = productAdaptor.deleteProduct(productId);
        log.info("deleted product={}", deletedProduct);
        return deletedProduct;
    }
}
