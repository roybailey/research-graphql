package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.service.UserAdaptor;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
public class OrderUserFetcher implements DataFetcher<UserDto> {

    UserAdaptor userAdaptor;

    public OrderUserFetcher(UserAdaptor userAdaptor) {
        this.userAdaptor = userAdaptor;
    }

    @Override
    public UserDto get(DataFetchingEnvironment environment) {
        log.info("getSource {}", (Object)environment.getSource());
        OrderDto order = environment.getSource();
        UserDto user = userAdaptor.getUser(order.getUserId());
        return user;
    }
}
