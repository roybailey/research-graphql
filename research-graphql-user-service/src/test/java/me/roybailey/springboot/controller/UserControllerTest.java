package me.roybailey.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.UserServiceApplication;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    ObjectMapper jacksonMapper;

    @LocalServerPort
    int port;

    @Rule
    public TestName name= new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    @Headers("Accept: application/json")
    public interface UserApi {

        @RequestLine("GET /user-api/v1/user")
        List<UserDto> getAllUsers();

        @RequestLine("GET /user-api/v1/user/{id}")
        UserDto getUser(@Param("id") String id);

        @Headers("Content-Type: application/json")
        @RequestLine("POST /user-api/v1/user")
        UserDto saveUser(UserDto newUser);

        @RequestLine("DELETE /user-api/v1/user/{id}")
        Response deleteUser(@Param("id") String id);
    }

    private UserApi api;

    @Before
    public void apiSetup() {
        this.api = Feign.builder()
                .encoder(new JacksonEncoder(jacksonMapper))
                .decoder(new JacksonDecoder(jacksonMapper))
                .logLevel(Logger.Level.BASIC)
                .target(UserApi.class, "http://localhost:"+port);
    }

    @Test
    public void test1_UserApi() {

        List<UserDto> allUsers = api.getAllUsers();
        assertThat(allUsers).isNotNull();
        softly.then(allUsers).hasSize(3);

        UserDto expected = allUsers.get(1);
        UserDto actual = api.getUser(expected.getId());
        softly.then(actual)
                .isNotNull()
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    public void test2_UserApiUpdates() {

        List<UserDto> allUsers = api.getAllUsers();
        assertThat(allUsers)
                .isNotNull()
                .hasSize(3);

        UserDto newUser = UserDto.builder()
                .title("Mr")
                .givenName("Mickey")
                .familyName("Mouse")
                .email("mickeymouse@disney.com")
                .build();
        UserDto savedUser = api.saveUser(newUser);
        softly.then(savedUser)
                .isNotNull()
                .isEqualToIgnoringNullFields(newUser);
        softly.then(savedUser.getId()).isNotNull();

        allUsers = api.getAllUsers();
        softly.then(allUsers)
                .isNotNull()
                .hasSize(4);

        Response response = api.deleteUser(savedUser.getId());
        softly.then(response.status()).isEqualTo(200);

        allUsers = api.getAllUsers();
        softly.then(allUsers)
                .isNotNull()
                .hasSize(3);
    }
}
