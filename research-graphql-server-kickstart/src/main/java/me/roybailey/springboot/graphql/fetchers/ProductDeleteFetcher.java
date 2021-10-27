package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.service.ProductAdaptor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
public class ProductDeleteFetcher implements DataFetcher<ProductDto>, ApplicationListener<ContextRefreshedEvent> {

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

    // only needed for annotations schema as it instantiates fetchers outside spring
    public ProductDeleteFetcher() {
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        productAdaptor = contextRefreshedEvent.getApplicationContext().getBean(ProductAdaptor.class);
    }
}
