package me.roybailey.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.configuration.DataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Slf4j
@Service
public class UserAdaptor {

    @Autowired
    DataSourceProperties properties;

    @Autowired
    ObjectMapper jacksonMapper;

    @Headers("Accept: application/json")
    private interface UserApi {

        @RequestLine("GET /user-api/v1/user")
        List<UserDto> getAllUsers();

        @RequestLine("GET /user-api/v1/user/{id}")
        UserDto getUserById(@Param("id") String id);

        @RequestLine("POST /user-api/v1/user")
        UserDto createUser(UserDto user);
    }

    private UserApi api;

    @PostConstruct
    public void apiSetup() {
        log.info("Connecting {} to {}", UserApi.class.getSimpleName(), properties.getUrlUserService());
        this.api = Feign.builder()
                .encoder(new JacksonEncoder(jacksonMapper))
                .decoder(new JacksonDecoder(jacksonMapper))
                .logLevel(Logger.Level.BASIC)
                .target(UserApi.class, properties.getUrlUserService());
    }


    public List<UserDto> getAllUsers() {
        List<UserDto> allUsers = api.getAllUsers();
        log.info("getAllUsers() : {}", allUsers);
        return allUsers;
    }

    public UserDto getUser(String userId) {
        UserDto user = api.getUserById(userId);
        log.info("getUser({}) : {}", userId, user);
        return user;
    }


    public UserDto createUser(UserDto user) {
        UserDto newUser = api.createUser(user);
        log.info("createUser({}) : {}", user, newUser);
        return newUser;
    }
}


