package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.ApplicationContextProvider;
import me.roybailey.springboot.service.ProductAdaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
@Component
public class ProductListFetcher implements DataFetcher<List<ProductDto>> {

    ProductAdaptor productAdaptor;

    public ProductListFetcher() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        productAdaptor = context.getBean(ProductAdaptor.class);
    }

    @Override
    public List<ProductDto> get(DataFetchingEnvironment environment) {
        log.info("getArguments {}", environment.getArguments());
        List<ProductDto> allProducts = productAdaptor.getAllProducts();
        return allProducts;
    }

}
