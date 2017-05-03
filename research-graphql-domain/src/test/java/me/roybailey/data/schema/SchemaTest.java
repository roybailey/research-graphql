package me.roybailey.data.schema;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.time.LocalDate;

@Slf4j
public class SchemaTest {

    @Rule
    public TestName name = new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    @Before
    public void banner() {
        log.info("------------------------------------------------------------");
        log.info(this.getClass().getSimpleName()+"."+name.getMethodName()+"()");
        log.info("------------------------------------------------------------");
    }


    @Test
    public void testDtoBuilders() {

        UserDto userDto = UserDto.builder()
                .title("Mr")
                .email("fred@acme.com")
                .givenName("Given")
                .familyName("Family")
                .bday(LocalDate.of(2000,1,1).atStartOfDay())
                .build();

        softly.then(userDto)
                .hasNoNullFieldsOrPropertiesExcept("id");
    }
}
