package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.ApplicationContextProvider;
import me.roybailey.springboot.service.UserAdaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
@Component
public class UserFetcher implements DataFetcher<UserDto> {

    UserAdaptor userAdaptor;

    public UserFetcher() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        userAdaptor = context.getBean(UserAdaptor.class);
    }


    @Override
    public UserDto get(DataFetchingEnvironment environment) {
        log.info("getArguments {}", environment.getArguments());
        String userId = environment.getArgument("userId");
        UserDto user = userAdaptor.getUser(userId);
        return user;
    }
}
