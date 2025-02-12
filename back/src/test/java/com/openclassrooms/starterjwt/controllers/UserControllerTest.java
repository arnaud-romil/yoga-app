package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("UserController should")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("return unauthorized status if user is unauthenticated")
    void shouldReturnUnauthorizedStatusIfUserIsUnauthenticated() throws Exception {

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("find user by id")
    void shouldFindUserById() throws Exception {

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("yoga@studio.com"))
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("Admin"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("return not found status when user id does not exist")
    void shouldReturnNotFoundStatusWhenUserIdDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/user/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("return bad request status when user id is invalid")
    void shouldReturnBadRequestWhenUserIdIsInvalid() throws Exception {

        mockMvc.perform(get("/api/user/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DisplayName("return not found when user to delete does not exist")
    void shouldReturnNotFoundWhenUserToDeleteDoesNotExist() throws Exception {

        mockMvc.perform(delete("/api/user/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DisplayName("not allow to delete another user")
    void shouldNotAllowToDeleteAnotherUser() throws Exception {
        final long userId = 1L;
        Optional<User> user = userRepository.findById(userId);
        assertTrue(user.isPresent());

        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isUnauthorized());

        user = userRepository.findById(userId);
        assertTrue(user.isPresent());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DisplayName("return bad request when user to delete id is invalid")
    void shouldRetunBadRequestWhenUserToDeleteIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/user/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user@test.com")
    @DisplayName("delete user by id")
    void shouldDeleteUserById() throws Exception {
        final long userId = 2L;

        Optional<User> user = userRepository.findById(userId);
        assertTrue(user.isPresent());

        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isOk());

        user = userRepository.findById(userId);
        assertFalse(user.isPresent());
    }

}
