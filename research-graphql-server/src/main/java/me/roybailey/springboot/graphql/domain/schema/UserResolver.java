package me.roybailey.springboot.graphql.domain.schema;


import com.coxautodev.graphql.tools.GraphQLResolver;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.data.schema.UserDto;
import org.springframework.stereotype.Service;

/**
 * This class contains resolver methods for the "Data Class" type
 */
@Service
public class UserResolver implements GraphQLResolver<UserDto> {

    public String getUserId(UserDto user) {
        return user.getId();
    }

    public String getFirstname(UserDto user) {
        return user.getGivenName();
    }

    public String getLastname(UserDto user) {
        return user.getFamilyName();
    }
}