package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@DisplayName("UserService should")
class UserServiceTest {

    private UserService userService;
    private UserRepository userRepositoryMock = mock(UserRepository.class);
    private User fakeUser;

    @BeforeEach
    void init() {
        userService = new UserService(userRepositoryMock);
        fakeUser = new User()
                .setId(1L)
                .setFirstName("Admin")
                .setLastName("Admin")
                .setAdmin(true)
                .setEmail("yoga@studio.com")
                .setPassword("password123!");
    }

    @Test
    @DisplayName("find an existing user by id")
    void shouldfindUserById() {
        when(userRepositoryMock.findById(fakeUser.getId())).thenReturn(Optional.of(fakeUser));
        User user = userService.findById(fakeUser.getId());
        assertNotNull(user);
        assertEquals(fakeUser, user);
    }

    @Test
    @DisplayName("return null when user cannot be found by id")
    void shouldReturnNullWhenUserCannotBeFoundById() {
        final long userId = 23L;
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());
        User user = userService.findById(userId);
        assertNull(user);
    }

    @Test
    @DisplayName("delete user by id")
    void shouldDeleteUserById() {
        final long userId = 23L;
        userService.delete(userId);
        verify(userRepositoryMock).deleteById(userId);
    }

}
