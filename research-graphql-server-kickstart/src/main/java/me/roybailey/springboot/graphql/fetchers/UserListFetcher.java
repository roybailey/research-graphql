package me.roybailey.springboot.graphql.fetchers;

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.service.UserAdaptor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpHeaders;

import java.util.Enumeration;
import java.util.List;


/**
 * Instantiated by graphql-java library so we need to hook into Spring to get other beans.
 */
@Slf4j
public class UserListFetcher implements DataFetcher<List<UserDto>>, ApplicationListener<ContextRefreshedEvent> {

    UserAdaptor userAdaptor;

    public UserListFetcher(UserAdaptor userAdaptor) {
        this.userAdaptor = userAdaptor;
    }

    @Override
    public List<UserDto> get(DataFetchingEnvironment environment) {
        DefaultGraphQLServletContext context = environment.getContext();
        Enumeration<String> headers = context.getHttpServletRequest().getHeaders("");
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String value = context.getHttpServletRequest().getHeader(header);
            log.info("HttpHeader {} {}", header, value);
        }
        log.info("getArguments {}", environment.getArguments());
        List<UserDto> allUsers = userAdaptor.getAllUsers();
        return allUsers;
    }

    // only needed for annotations schema as it instantiates fetchers outside spring
    public UserListFetcher() {}

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        userAdaptor = contextRefreshedEvent.getApplicationContext().getBean(UserAdaptor.class);
    }
}
