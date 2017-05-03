package me.roybailey.springboot.mapper;


import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.domain.Category;
import me.roybailey.springboot.domain.Product;
import me.roybailey.springboot.repository.CategoryRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ProductMapper {

    ModelMapper mapper = new ModelMapper();

    public ProductMapper() {
        // Converters for complex field conversions
        // Converter<LocalDate, LocalDateTime> convertLocalDate2LocalDateTime = context -> context.getSource() == null ? null :
        //                context.getSource().atStartOfDay();
        // Converter<LocalDateTime, LocalDate> convertLocalDateTime2LocalDate = context -> context.getSource() == null ? null :
        //                context.getSource().toLocalDate();
        Converter<List<Category>, List<String>> convertCategory2Name = context -> context.getSource() == null ? null :
                context.getSource().stream().map(Category::getName).collect(Collectors.toList());
        Converter<List<String>, List<Category>> convertCategoryName2Category = context -> context.getSource() == null ? null :
                context.getSource().stream().map((name) -> Category.builder().name(name).build()).collect(Collectors.toList());
        // create PropertyMap<source,target> here and add to mapper
        // map().set<Target>(source.get<Source>());
        // map(source.get<Source>(), destination.get<Target>());
        mapper.addMappings(new PropertyMap<Product, ProductDto>() {
            @Override
            protected void configure() {
                using(convertCategory2Name).map(source.getCategories(), destination.getCategory());
            }
        });
        mapper.addMappings(new PropertyMap<ProductDto, Product>() {
            @Override
            protected void configure() {
                using(convertCategoryName2Category).map(source.getCategory(), destination.getCategories());
            }
        });
    }

    public ProductDto toProductDto(Product product) {
        return this.mapper.map(product, ProductDto.class);
    }

    public List<ProductDto> toProductDtoList(List<Product> products) {
        return this.mapper.map(products, new TypeToken<List<ProductDto>>() {
        }.getType());
    }

    public Product toProduct(ProductDto product) {
        return this.mapper.map(product, Product.class);
    }
}
