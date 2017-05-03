package me.roybailey.springboot.mapper;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.domain.User;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.time.LocalDate;


@Slf4j
public class UserMapperTest {

    @Rule
    public TestName name= new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    UserMapper mapper = new UserMapper();

    @Test
    public void testUserDtoMapping() {

        UserDto userDto = UserDto.builder()
                .id("user-1")
                .title("Mr")
                .givenName("Mickey")
                .familyName("Mouse")
                .email("mickey.mouse@disney.com")
                .bday(LocalDate.of(1928,1,1).atStartOfDay())
                .build();

        User userJpa = mapper.toUser(userDto);

        softly.then(userJpa.getId()).isEqualTo(userDto.getId());
        softly.then(userJpa.getTitle()).isEqualTo(userDto.getTitle());
        softly.then(userJpa.getFirstname()).isEqualTo(userDto.getGivenName());
        softly.then(userJpa.getLastname()).isEqualTo(userDto.getFamilyName());
        softly.then(userJpa.getEmail()).isEqualTo(userDto.getEmail());
        softly.then(userJpa.getDob()).isEqualTo(userDto.getBday().toLocalDate());
    }

    @Test
    public void testUserJpaMapping() {

        User userJpa = User.builder()
                .id("user-1")
                .title("Mr")
                .firstname("Mickey")
                .lastname("Mouse")
                .email("mickey.mouse@disney.com")
                .dob(LocalDate.of(1928,1,1))
                .build();

        UserDto userDto = mapper.toUserDto(userJpa);

        softly.then(userDto.getId()).isEqualTo(userJpa.getId());
        softly.then(userDto.getTitle()).isEqualTo(userJpa.getTitle());
        softly.then(userDto.getGivenName()).isEqualTo(userJpa.getFirstname());
        softly.then(userDto.getFamilyName()).isEqualTo(userJpa.getLastname());
        softly.then(userDto.getEmail()).isEqualTo(userJpa.getEmail());
        softly.then(userDto.getBday()).isEqualTo(userJpa.getDob().atStartOfDay());
    }

}
