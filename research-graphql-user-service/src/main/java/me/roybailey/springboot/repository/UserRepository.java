package me.roybailey.springboot.repository;

import com.google.common.collect.ImmutableList;
import me.roybailey.springboot.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class UserRepository {

    private AtomicLong sequence = new AtomicLong();

    private String getIdentifier() {
        return String.format("U%02d", sequence.addAndGet(1));
    }

    private ConcurrentHashMap<String, User> mapUsers = new ConcurrentHashMap<>();

    public UserRepository() {
        ImmutableList.of(
                User.builder()
                        .title("Mr")
                        .firstname("Anna")
                        .lastname("Armpit")
                        .email("anna@acme.com")
                        .dob(LocalDate.of(1970, 1, 23))
                        .build(),
                User.builder()
                        .title("Ms")
                        .firstname("Burt")
                        .lastname("Burntleg")
                        .email("burt@bioware.com")
                        .dob(LocalDate.of(1975, 11, 17))
                        .build(),
                User.builder()
                        .title("Dr")
                        .firstname("Carl")
                        .lastname("Coldheart")
                        .email("carl@calmsprings.com")
                        .dob(LocalDate.of(1984, 6, 30))
                        .build()
        ).forEach(this::save);
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(mapUsers.values()));
    }

    public Optional<User> findOne(String id) {
        return Optional.ofNullable(mapUsers.get(id));
    }

    public User save(User newUser) {
        if (newUser.getId() == null)
            newUser.setId(getIdentifier());
        mapUsers.put(newUser.getId(), newUser);
        return newUser;
    }

    public Optional<User> remove(String id) {
        return Optional.ofNullable(mapUsers.remove(id));
    }

}
