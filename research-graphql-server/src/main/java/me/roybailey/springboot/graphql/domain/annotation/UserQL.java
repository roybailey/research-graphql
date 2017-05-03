package me.roybailey.springboot.graphql.domain.annotation;

import com.fasterxml.jackson.annotation.JsonProperty;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.annotations.GraphQLType;
import lombok.Data;

@Data
@GraphQLType
@GraphQLName("UserDto")
public class UserQL {

    @GraphQLField
    @GraphQLName("userId")
    @JsonProperty("userId")
    private String id;

    @GraphQLField
    private String title;

    @GraphQLName("firstname")
    @GraphQLField
    private String givenName;

    @GraphQLName("lastname")
    @GraphQLField
    private String familyName;

    @GraphQLField
    private String email;

    @GraphQLField
    @GraphQLName("dob")
    @JsonProperty("dob")
    private String bday;

}
