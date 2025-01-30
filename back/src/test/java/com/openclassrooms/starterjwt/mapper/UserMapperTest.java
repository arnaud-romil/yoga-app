package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private List<UserDto> fakeUserDtos;
    private List<User> fakeUsers;

    @BeforeEach
    void init() {
        fakeUserDtos = getFakeUserDtos();
        fakeUsers = getFakeUsers();
    }

    @Test
    void shouldMapUserToUserDto() {
        User userNullCase = null;
        assertNull(userMapper.toDto(userNullCase));
    }

    @Test
    void shouldMapUserDtoToUserEntity() {

        UserDto userDtoNullCase = null;

        assertNull(userMapper.toEntity(userDtoNullCase));

        UserDto userDto = fakeUserDtos.get(0);
        User user = userMapper.toEntity(userDto);

        assertAll(
                () -> assertEquals(userDto.getEmail(), user.getEmail()),
                () -> assertEquals(userDto.getFirstName(), user.getFirstName()),
                () -> assertEquals(userDto.getLastName(), user.getLastName()),
                () -> assertEquals(userDto.isAdmin(), user.isAdmin()));
    }

    @Test
    void shouldMapUserDtoListToUserEntityList() {

        List<UserDto> userDtosNullCase = null;
        assertNull(userMapper.toEntity(userDtosNullCase));

        List<User> users = userMapper.toEntity(fakeUserDtos);
        assertAll(
                () -> assertEquals(fakeUserDtos.size(), users.size()),
                () -> assertEquals(fakeUserDtos.get(0).getFirstName(), users.get(0).getFirstName()),
                () -> assertEquals(fakeUserDtos.get(0).getLastName(), users.get(0).getLastName()),
                () -> assertEquals(fakeUserDtos.get(0).isAdmin(), users.get(0).isAdmin()),
                () -> assertEquals(fakeUserDtos.get(1).getFirstName(), users.get(1).getFirstName()),
                () -> assertEquals(fakeUserDtos.get(1).getLastName(), users.get(1).getLastName()),
                () -> assertEquals(fakeUserDtos.get(1).isAdmin(), users.get(1).isAdmin()));
    }

    @Test
    void shouldMapUserListToUserDtoList() {

        List<User> userNullCase = null;
        assertNull(userMapper.toDto(userNullCase));

        List<UserDto> userDtos = userMapper.toDto(fakeUsers);
        assertAll(
                () -> assertEquals(fakeUsers.size(), userDtos.size()),
                () -> assertEquals(fakeUsers.get(0).getFirstName(), userDtos.get(0).getFirstName()),
                () -> assertEquals(fakeUsers.get(0).getLastName(), userDtos.get(0).getLastName()),
                () -> assertEquals(fakeUsers.get(0).isAdmin(), userDtos.get(0).isAdmin()),
                () -> assertEquals(fakeUsers.get(1).getFirstName(), userDtos.get(1).getFirstName()),
                () -> assertEquals(fakeUsers.get(1).getLastName(), userDtos.get(1).getLastName()),
                () -> assertEquals(fakeUsers.get(1).isAdmin(), userDtos.get(1).isAdmin()));
    }

    private List<UserDto> getFakeUserDtos() {

        UserDto userDto = new UserDto();
        userDto.setEmail("user@email.com");
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setAdmin(false);
        userDto.setPassword("");

        UserDto userDto2 = new UserDto();
        userDto2.setEmail("user2@email.com");
        userDto2.setFirstName("firstName2");
        userDto2.setLastName("lastName2");
        userDto2.setAdmin(false);
        userDto2.setPassword("");

        return Arrays.asList(userDto, userDto2);

    }

    private List<User> getFakeUsers() {

        User user = new User("user@email.com", "lastName", "firstName", "password", false);
        User user2 = new User("user2@email.com", "lastName2", "firstName2", "password2", false);

        return Arrays.asList(user, user2);
    }

}
