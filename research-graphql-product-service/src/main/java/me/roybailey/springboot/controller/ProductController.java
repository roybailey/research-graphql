package me.roybailey.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.domain.Category;
import me.roybailey.springboot.domain.Product;
import me.roybailey.springboot.mapper.ProductMapper;
import me.roybailey.springboot.repository.CategoryRepository;
import me.roybailey.springboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@Controller
@RequestMapping(value = "/product-api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;


    @ResponseBody
    @GetMapping(path = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductDto> getAllProducts() {
        List<Product> products = StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        log.info("getAllProducts() : {}", products);
        return productMapper.toProductDtoList(products);
    }


    @ResponseBody
    @GetMapping(path = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto getProduct(@PathVariable(name = "id") String id) {
        Product product = productRepository.findById(id).orElse(null);
        log.info("getProduct({}) : {}", id, product);
        return productMapper.toProductDto(product);
    }


    @ResponseBody
    @PostMapping(path = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto upsertProduct(@RequestBody ProductDto updatedProduct) {
        Product product = productMapper.toProduct(updatedProduct);
        if (product.getCategories() != null) {
            // since categories are passed though DTO as 'name' only
            // we need to map these into existing or newly created Category JPA objects
            List<Category> savedCategories = product.getCategories().stream()
                    .map((category) -> {
                        Category savedCategory;
                        Optional<Category> found = categoryRepository.findByName(category.getName());
                        if (found.isPresent()) {
                            savedCategory = found.get();
                            log.info("Mapping existing category {}", category.getName());
                        } else {
                            savedCategory = categoryRepository.save(category);
                            log.info("Mapping created category {}", category.getName());
                        }
                        return savedCategory;
                    })
                    .collect(Collectors.toList());
            product.setCategories(savedCategories);
        }
        Product savedProduct = productRepository.save(product);
        log.info("upsertProduct({}) : {}", updatedProduct, savedProduct);
        return productMapper.toProductDto(savedProduct);
    }


    @ResponseBody
    @DeleteMapping(path = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto deleteProduct(@PathVariable(name = "id") String id) {
        Product product = productRepository.findById(id).orElse(null);
        productRepository.deleteById(id);
        log.info("deleteProduct({}) : {}", id, product);
        return productMapper.toProductDto(product);
    }
}
