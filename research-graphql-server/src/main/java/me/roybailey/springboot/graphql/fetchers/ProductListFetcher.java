package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.service.ProductAdaptor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
public class ProductListFetcher implements DataFetcher<List<ProductDto>>, ApplicationListener<ContextRefreshedEvent> {

    ProductAdaptor productAdaptor;

    public ProductListFetcher(ProductAdaptor productAdaptor) {
        this.productAdaptor = productAdaptor;
    }

    @Override
    public List<ProductDto> get(DataFetchingEnvironment environment) {
        log.info("getArguments {}", environment.getArguments());
        List<ProductDto> allProducts = productAdaptor.getAllProducts();
        return allProducts;
    }

    // only needed for annotations schema as it instantiates fetchers outside spring
    public ProductListFetcher() {}

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        productAdaptor = contextRefreshedEvent.getApplicationContext().getBean(ProductAdaptor.class);
    }
}
