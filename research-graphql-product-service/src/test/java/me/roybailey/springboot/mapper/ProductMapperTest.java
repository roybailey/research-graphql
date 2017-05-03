package me.roybailey.springboot.mapper;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.domain.Category;
import me.roybailey.springboot.domain.Product;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class ProductMapperTest {

    @Rule
    public TestName name = new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    ProductMapper mapper = new ProductMapper();

    @Test
    public void testProductDto2JpaMapping() {

        ProductDto productDto = ProductDto.builder()
                .id("product-1")
                .name("product-1")
                .brand("brand-1")
                .description("description-1")
                .category(ImmutableList.of("one", "two"))
                .price(BigDecimal.TEN)
                .build();

        Product productJpa = mapper.toProduct(productDto);

        softly.then(productJpa.getId()).isEqualTo(productDto.getId());
        softly.then(productJpa.getName()).isEqualTo(productDto.getName());
        softly.then(productJpa.getBrand()).isEqualTo(productDto.getBrand());
        softly.then(productJpa.getDescription()).isEqualTo(productDto.getDescription());
        softly.then(productJpa.getPrice()).isEqualTo(productDto.getPrice());
        assertThat(productJpa.getCategories()).isNotNull();
        softly.then(productJpa.getCategories()).usingElementComparatorOnFields("category");
    }

    @Test
    public void testProductJpa2DtoMapping() {

        Product productJpa = Product.builder()
                .id("product-1")
                .name("product-1")
                .brand("brand-1")
                .description("description-1")
                .categories(ImmutableList.of(
                        Category.builder().name("one").build(),
                        Category.builder().name("two").build()))
                .price(BigDecimal.TEN)
                .build();

        ProductDto productDto = mapper.toProductDto(productJpa);

        softly.then(productDto.getId()).isEqualTo(productJpa.getId());
        softly.then(productDto.getName()).isEqualTo(productJpa.getName());
        softly.then(productDto.getBrand()).isEqualTo(productJpa.getBrand());
        softly.then(productDto.getDescription()).isEqualTo(productJpa.getDescription());
        softly.then(productDto.getPrice()).isEqualTo(productJpa.getPrice());
        softly.then(productDto.getCategory()).isEqualTo(ImmutableList.of("one","two"));
    }

}
