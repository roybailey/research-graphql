package me.roybailey.springboot;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.service.ProductAdaptor;
import me.roybailey.springboot.service.UserAdaptor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Slf4j
@Configuration
public class ApplicationTestConfiguration {

    static AtomicInteger PID = new AtomicInteger();

    static Map<String, ProductDto> mapProducts = new HashMap<>(ImmutableMap.of(
            String.valueOf("P" + PID.incrementAndGet()), ProductDto.builder()
                    .id("P" + PID.get())
                    .name("product-" + PID.get())
                    .brand("acme")
                    .price(BigDecimal.TEN)
                    .description("test product")
                    .category(ImmutableList.of("one", "two"))
                    .build(),
            String.valueOf("P" + PID.incrementAndGet()), ProductDto.builder()
                    .id("P" + PID.get())
                    .name("product-" + PID.get())
                    .brand("acme")
                    .price(BigDecimal.valueOf(5.50))
                    .description("test product")
                    .category(ImmutableList.of("two", "three"))
                    .build(),
            String.valueOf("P" + PID.incrementAndGet()), ProductDto.builder()
                    .id("P" + PID.get())
                    .name("product-" + PID.get())
                    .brand("bigg")
                    .price(BigDecimal.valueOf(14.95))
                    .description("test product")
                    .category(ImmutableList.of("one", "three"))
                    .build()
    ));

    static AtomicInteger UID = new AtomicInteger();

    static Map<String, UserDto> mapUsers = new HashMap<>(ImmutableMap.of(
            String.valueOf("U" + UID.incrementAndGet()), UserDto.builder()
                    .id("U" + UID.get())
                    .title("Mr")
                    .givenName("user-" + UID.get())
                    .familyName("test")
                    .email("user" + UID.get() + "@acme.com")
                    .build(),
            String.valueOf("U" + UID.incrementAndGet()), UserDto.builder()
                    .id("U" + UID.get())
                    .title("Ms")
                    .givenName("user-" + UID.get())
                    .familyName("test")
                    .email("user" + UID.get() + "@acme.com")
                    .build(),
            String.valueOf("U" + UID.incrementAndGet()), UserDto.builder()
                    .id("U" + UID.get())
                    .title("Dr")
                    .givenName("user-" + UID.get())
                    .familyName("test")
                    .email("user" + UID.get() + "@acme.com")
                    .build()
    ));

    @Bean
    @Primary
    ProductAdaptor getProductAdaptor() {
        ProductAdaptor mock = mock(ProductAdaptor.class);
        when(mock.getAllProducts())
                .thenReturn(new ArrayList<>(mapProducts.values()));
        when(mock.getProduct(anyString()))
                .then((inv) -> mapProducts.get(inv.getArgument(0, String.class)));
        when(mock.upsertProduct(anyObject()))
                .then((inv) -> {
                    ProductDto product = inv.getArgument(0, ProductDto.class);
                    ProductDto newProduct = new ModelMapper().map(product, ProductDto.class);
                    if(newProduct.getId()==null)
                    newProduct.setId(UUID.randomUUID().toString());
                    mapProducts.put(newProduct.getId(),newProduct);
                    log.info("upsertProduct({}) : {}", product, newProduct);
                    return newProduct;
                });
        when(mock.deleteProduct(anyString()))
                .then((inv) -> {
                    String productId = inv.getArgument(0, String.class);
                    ProductDto removedProduct = mapProducts.remove(productId);
                    log.info("deleteProduct({}) : {}", productId, removedProduct);
                    return removedProduct;
                });
        return mock;
    }

    @Bean
    @Primary
    UserAdaptor getUserAdaptor() {
        UserAdaptor mock = mock(UserAdaptor.class);
        when(mock.getAllUsers())
                .thenReturn(new ArrayList<>(mapUsers.values()));
        when(mock.getUser(anyString()))
                .then((inv) -> mapUsers.get(inv.getArgument(0, String.class)));
        return mock;
    }
}
