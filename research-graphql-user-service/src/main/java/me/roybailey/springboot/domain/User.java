package me.roybailey.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    String id;
    String title;
    String firstname;
    String lastname;
    String email;
    LocalDate dob;

}
