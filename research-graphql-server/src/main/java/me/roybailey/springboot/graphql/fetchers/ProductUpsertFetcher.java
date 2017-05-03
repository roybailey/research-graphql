package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.ApplicationContextProvider;
import me.roybailey.springboot.service.ProductAdaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
@Component
public class ProductUpsertFetcher implements DataFetcher<ProductDto> {

    ProductAdaptor productAdaptor;

    public ProductUpsertFetcher() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        productAdaptor = context.getBean(ProductAdaptor.class);
    }

    public static final List<String> EMPTY_CATEGORIES = new ArrayList<>();

    @Override
    public ProductDto get(DataFetchingEnvironment environment) {
        log.info("getArguments {}", environment.getArguments());
        Object inputObject = environment.getArgument("product");
        ProductDto newProduct = ProductDto.builder().build();
        // we map both 'product' as input type and separate primitive field arguments to this method for illustration
        Function<Object, String> safeString = (o) -> o == null ? null : o.toString();
        Function<Object, BigDecimal> safeBigDecimal = (o) -> (o instanceof Number) ? BigDecimal.valueOf((Double) o) : null;
        Function<Object, List<String>> safeCategories = (o) -> (EMPTY_CATEGORIES.getClass().isInstance(o)) ? (List<String>) o : null;
        if (inputObject == null) {
            newProduct.setName(environment.getArgument("name"));
            newProduct.setBrand(environment.getArgument("brand"));
            newProduct.setDescription(environment.getArgument("description"));
            newProduct.setPrice(safeBigDecimal.apply(environment.getArgument("price")));
            newProduct.setCategory(environment.getArgument("category"));
        } else {
            // input type arguments not converted to java types
//            InputProductQL inputProduct = (InputProductQL) inputObject;
//            newProduct.setName(inputProduct.getName());
//            newProduct.setBrand(inputProduct.getBrand());
//            newProduct.setDescription(inputProduct.getDescription());
//            newProduct.setPrice(BigDecimal.valueOf(inputProduct.getPrice()));
            Map<String, Object> inputMap = (Map<String, Object>) inputObject;
            newProduct.setName(safeString.apply(inputMap.get("name")));
            newProduct.setBrand(safeString.apply(inputMap.get("brand")));
            newProduct.setDescription(safeString.apply(inputMap.get("description")));
            newProduct.setPrice(safeBigDecimal.apply(inputMap.get("price")));
            newProduct.setCategory(safeCategories.apply(inputMap.get("category")));
        }
        log.info("saving new product={}", newProduct);
        ProductDto savedProduct = productAdaptor.upsertProduct(newProduct);
        log.info("saved new product={}", savedProduct);
        return savedProduct;
    }
}
