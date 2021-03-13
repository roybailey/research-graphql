package me.roybailey.springboot.repository;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.springboot.configuration.JpaRepositoryConfiguration;
import me.roybailey.springboot.domain.Category;
import me.roybailey.springboot.domain.Product;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaRepositoryConfiguration.class)
public class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Rule
    public TestName name= new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();


    private List<Category> loadAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    private List<Product> loadAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Test
    public void testImportedProductData(){

        List<Category> allCategories = loadAllCategories();
        log.info(name+" loaded Category data = " + allCategories);
        assertThat(allCategories).isNotNull().hasSize(4);

        List<Product> allProducts = loadAllProducts();
        log.info(name+" loaded Product data = " + allProducts);
        assertThat(allProducts).isNotNull().hasSize(3);

        Category furniture = categoryRepository.findByName("Furniture").orElse(null);
        assertThat(furniture).isNotNull().hasNoNullFieldsOrProperties();
    }

    @Test
    public void testCreateUpdateDeleteProduct(){

        List<Category> allCategories = loadAllCategories();
        assertThat(allCategories).isNotNull().hasSize(4);

        int count = loadAllProducts().size();

        Product newProduct = Product.builder()
                .name("new product")
                .brand("new brand")
                .description("test product")
                .price(BigDecimal.TEN)
                .categories(ImmutableList.of(
                        allCategories.get(0),
                        allCategories.get(allCategories.size()-1)))
                .build();
        Product savedProduct = productRepository.save(newProduct);
        log.info("saved Product {}", savedProduct);
        assertThat(savedProduct)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
        assertThat(savedProduct.getCategories())
                .isNotNull()
                .hasSize(2)
                .contains(allCategories.get(0))
                .contains(allCategories.get(allCategories.size()-1));

        savedProduct.setDescription("updated description");
        Product updatedProduct = productRepository.save(savedProduct);
        assertThat(updatedProduct)
                .isNotNull()
                .hasFieldOrPropertyWithValue("description", savedProduct.getDescription());

        softly.then(loadAllProducts()).hasSize(count+1);
        productRepository.deleteById(updatedProduct.getId());
        softly.then(loadAllProducts()).hasSize(count);
    }

}
