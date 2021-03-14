package me.roybailey.springboot.graphql.domain.schema;

import graphql.kickstart.tools.GraphQLQueryResolver;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.service.OrderAdaptor;
import me.roybailey.springboot.service.ProductAdaptor;
import me.roybailey.springboot.service.UserAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Query implements GraphQLQueryResolver {

    @Autowired
    ProductAdaptor productAdaptor;

    @Autowired
    UserAdaptor userAdaptor;

    @Autowired
    OrderAdaptor orderAdaptor;

    public List<ProductDto> products() {
        return productAdaptor.getAllProducts();
    }

    public List<UserDto> users() {
        return userAdaptor.getAllUsers();
    }

    public List<OrderDto> orders() {
        return orderAdaptor.getAllOrders();
    }

    public ProductDto product(String productId) {
        return productAdaptor.getProduct(productId);
    }

    public UserDto user(String userId) {
        return userAdaptor.getUser(userId);
    }

}
