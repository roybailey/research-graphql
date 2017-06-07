package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.service.UserAdaptor;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
public class UserFetcher implements DataFetcher<UserDto> {

    UserAdaptor userAdaptor;

    public UserFetcher(UserAdaptor userAdaptor) {
        this.userAdaptor = userAdaptor;
    }

    @Override
    public UserDto get(DataFetchingEnvironment environment) {
        log.info("getArguments {}", environment.getArguments());
        String userId = environment.getArgument("userId");
        UserDto user = userAdaptor.getUser(userId);
        return user;
    }
}
