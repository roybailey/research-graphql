package me.roybailey.springboot.graphql.domain.schema;


import com.coxautodev.graphql.tools.GraphQLResolver;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.service.OrderAdaptor;
import me.roybailey.springboot.service.ProductAdaptor;
import me.roybailey.springboot.service.UserAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class contains resolver methods for the "Data Class" type
 */
@Service
public class OrderResolver implements GraphQLResolver<OrderDto> {

    @Autowired
    UserAdaptor userAdaptor;

    public UserDto getUser(OrderDto order) {
        return userAdaptor.getUser(order.getUserId());
    }
}