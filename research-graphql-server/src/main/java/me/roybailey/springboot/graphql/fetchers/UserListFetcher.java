package me.roybailey.springboot.graphql.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.ApplicationContextProvider;
import me.roybailey.springboot.service.UserAdaptor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
@Component
public class UserListFetcher implements DataFetcher<List<UserDto>> {

    UserAdaptor userAdaptor;

    public UserListFetcher() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        userAdaptor= context.getBean(UserAdaptor.class);
    }

    @Override
    public List<UserDto> get(DataFetchingEnvironment environment) {
        HttpHeaders headers = environment.getContext();
        log.info("getContext() {}", headers);
        log.info("getArguments {}", environment.getArguments());
        List<UserDto> allUsers = userAdaptor.getAllUsers();
        return allUsers;
    }

}
