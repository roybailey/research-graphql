package me.roybailey.springboot.repository;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.springboot.domain.User;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserRepository.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Rule
    public TestName name= new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();


    @Test
    public void testLoadedProducts(){

        List<User> allUsers = userRepository.findAll();
        log.info(name+" loaded User data = " + allUsers);
        assertThat(allUsers).isNotNull().hasSize(3);
    }

    @Test
    public void testCreateUpdateDeleteProduct(){

        int count = userRepository.findAll().size();

        User newUser = User.builder()
                .title("Dr")
                .firstname("Do")
                .lastname("Little")
                .email("drdolittle@disney.com")
                .dob(LocalDate.of(2017,1,1))
                .build();
        User savedUser = userRepository.save(newUser);
        log.info("saved User {}", savedUser);
        assertThat(savedUser)
                .isNotNull()
                .hasNoNullFieldsOrProperties();

        savedUser.setFirstname("Who");
        User updatedUser = userRepository.save(savedUser);
        assertThat(updatedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("firstname", savedUser.getFirstname());

        softly.then(userRepository.findAll()).hasSize(count+1);
        userRepository.remove(updatedUser.getId());
        softly.then(userRepository.findAll()).hasSize(count);
    }

}
