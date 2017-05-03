package me.roybailey.springboot.mapper;

import me.roybailey.data.schema.UserDto;
import me.roybailey.springboot.domain.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class UserMapper {

    private ModelMapper mapper = new ModelMapper();

    public UserMapper() {
        // Converters for complex field conversions
        Converter<LocalDate, LocalDateTime> convertLocalDate2LocalDateTime = context -> context.getSource() == null ? null :
                context.getSource().atStartOfDay();
        Converter<LocalDateTime, LocalDate> convertLocalDateTime2LocalDate = context -> context.getSource() == null ? null :
                context.getSource().toLocalDate();
        // add PropertyMap<source,target> for unnatural field mappings
        // map().set<Target>(source.get<Source>());
        // map(source.get<Source>(), destination.get<Target>());
        mapper.addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map().setGivenName(source.getFirstname());
                map().setFamilyName(source.getLastname());
                using(convertLocalDate2LocalDateTime).map(source.getDob(), destination.getBday());
            }
        });
        mapper.addMappings(new PropertyMap<UserDto, User>() {
            @Override
            protected void configure() {
                map().setFirstname(source.getGivenName());
                map().setLastname(source.getFamilyName());
                using(convertLocalDateTime2LocalDate).map(source.getBday(), destination.getDob());
            }
        });
        // add mapper.getTypeMap(<source>, <target>) pre/post conversion logic
    }

    public UserDto toUserDto(User user) {
        return this.mapper.map(user, UserDto.class);
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return this.mapper.map(users, new TypeToken<List<UserDto>>() {
        }.getType());
    }

    public User toUser(UserDto user) {
        return this.mapper.map(user, User.class);
    }
}
