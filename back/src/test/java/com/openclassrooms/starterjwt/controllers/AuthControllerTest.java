package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("AuthController should")
class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("allow user to login")
    void shouldAllowUserToLogin() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity("/api/auth/login", loginRequest,
                JwtResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(1L, response.getBody().getId()),
                () -> assertEquals("yoga@studio.com", response.getBody().getUsername()),
                () -> assertEquals("Admin", response.getBody().getFirstName()),
                () -> assertEquals("Admin", response.getBody().getLastName()),
                () -> assertTrue(response.getBody().getAdmin()));
    }

    @Test
    @DisplayName("return 401 status when credentials are not correct")
    void shouldRetunUnauthorizedStatusWhenCredentialsAreNotCorrect() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("wrong-password");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", loginRequest,
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    @DisplayName("allow user to register")
    void shouldAllowUserToRegister() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("new.user@test.com");
        signupRequest.setFirstName("UserFirstName");
        signupRequest.setLastName("UserLastName");
        signupRequest.setPassword("User-Password");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", signupRequest,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("User registered successfully!"));
    }

    @Test
    @DisplayName("not allow user to register if email is already taken")
    void shouldNotAllowUserToRegisterIfEmailIsAlreadyTaken() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setFirstName("UserFirstName");
        signupRequest.setLastName("UserLastName");
        signupRequest.setPassword("User-Password");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", signupRequest,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Error: Email is already taken!"));
    }

}
