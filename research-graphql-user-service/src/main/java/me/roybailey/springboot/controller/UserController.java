package me.roybailey.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.domain.User;
import me.roybailey.springboot.mapper.UserMapper;
import me.roybailey.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/user-api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @ResponseBody
    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("getAllUsers() : {}", users);
        return userMapper.toUserDtoList(users);
    }

    @ResponseBody
    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@PathVariable(name = "id") String id) {
        User user = userRepository.findOne(id).orElseThrow(IllegalArgumentException::new);
        log.info("getUser({}) : {}", id, user);
        return userMapper.toUserDto(user);
    }

    @ResponseBody
    @PostMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto upsertUser(@RequestBody UserDto user) {
        User savedUser = userRepository.save(userMapper.toUser(user));
        log.info("upsertUser({}) : {}", user, savedUser);
        return userMapper.toUserDto(savedUser);
    }

    @ResponseBody
    @DeleteMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto deleteUser(@PathVariable(name = "id") String id) {
        User user = userRepository.remove(id).orElseThrow(IllegalArgumentException::new);
        log.info("getUser({}) : {}", id, user);
        return userMapper.toUserDto(user);
    }
}
