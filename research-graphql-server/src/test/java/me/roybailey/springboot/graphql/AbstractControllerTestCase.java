package me.roybailey.springboot.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.springboot.AbstractTestCase;
import me.roybailey.springboot.ApplicationTestConfiguration;
import me.roybailey.springboot.GraphQLServerApplication;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationTestConfiguration.class, GraphQLServerApplication.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
abstract public class AbstractControllerTestCase extends AbstractTestCase {

    @Autowired
    ObjectMapper mapper;

    @LocalServerPort
    private int port;

    <T> T apiSetup(Class<T> target) {
        return Feign.builder()
                .encoder(new JacksonEncoder(mapper))
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.BASIC)
                .target(target, "http://localhost:" + port);
    }

}

