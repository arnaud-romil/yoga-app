package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("TeacherController should")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("find session by id")
    void shouldFindSessionById() throws Exception {
        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("My Session"))
                .andExpect(jsonPath("$.description").value("Session description"))
                .andExpect(jsonPath("$.teacher_id").value(1));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("should return not found status when session does not exist")
    void shouldReturnNotFoundStatusWhenSessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("should return bad request when session id is invalid")
    void shouldReturnBadRequestWhenSessionIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/session/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("find all sessions")
    void shouldFindAllSessions() throws Exception {

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[0].name").value("My Session"))
                .andExpect(jsonPath("[0].description").value("Session description"))
                .andExpect(jsonPath("[0].teacher_id").value(1));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DirtiesContext
    @DisplayName("create a new session")
    void shouldCreateSession() throws Exception {

        SessionDto newSession = new SessionDto();
        newSession.setName("New Session");
        newSession.setDate(new Date());
        newSession.setDescription("New Session description");
        newSession.setTeacher_id(1L);
        newSession.setUsers(Collections.emptyList());

        String sessionAsJson = new ObjectMapper().writeValueAsString(newSession);

        mockMvc.perform(post("/api/session")
                .content(sessionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(newSession.getName()))
                .andExpect(jsonPath("$.description").value(newSession.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(newSession.getTeacher_id()));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DirtiesContext
    @DisplayName("update session")
    void shouldUpdateSession() throws Exception {

        SessionDto session = new SessionDto();
        session.setName("My Session");
        session.setDate(new Date());
        session.setDescription("Session description updated!");
        session.setTeacher_id(1L);

        String sessionAsJson = new ObjectMapper().writeValueAsString(session);

        mockMvc.perform(put("/api/session/1")
                .content(sessionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(session.getName()))
                .andExpect(jsonPath("$.description").value(session.getDescription()))
                .andExpect(jsonPath("$.teacher_id").value(session.getTeacher_id()));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DirtiesContext
    @DisplayName("should return bad request when updating session with invalid id")
    void shouldReturnBadRequestWhenUpdatingSessionWithInvalidId() throws Exception {

        SessionDto session = new SessionDto();
        session.setName("My Session");
        session.setDate(new Date());
        session.setDescription("Session description updated!");
        session.setTeacher_id(1L);

        String sessionAsJson = new ObjectMapper().writeValueAsString(session);

        mockMvc.perform(put("/api/session/invalid-id")
                .content(sessionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DirtiesContext
    @DisplayName("delete a session")
    void shouldDeleteSession() throws Exception {

        final long sessionId = 2L;
        Optional<Session> session = sessionRepository.findById(sessionId);
        assertTrue(session.isPresent());

        mockMvc.perform(delete("/api/session/" + sessionId))
                .andExpect(status().isOk());

        session = sessionRepository.findById(sessionId);
        assertFalse(session.isPresent());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("return not found when session to delete does not exist")
    void shouldReturnNotFoundWhenSessionToDeleteDoesNotExist() throws Exception {
        final long sessionId = 99L;

        mockMvc.perform(delete("/api/session/" + sessionId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("return bad request when session to delete id is invalid")
    void shouldReturnNotFoundWhenSessionToDeleteIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/session/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DirtiesContext
    @DisplayName("allow user to participate in session")
    void shouldAllowUserToParticipateInSession() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DisplayName("return bad request when user participates in session with invalid id")
    void shouldReturnBadRequestWhenUserParticipatesInSessionWithInvalidId() throws Exception {
        mockMvc.perform(post("/api/session/invalid/participate/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DirtiesContext
    @DisplayName("allow user to cancel participation in session")
    void shouldAllowUserToCancelParticipationInSession() throws Exception {

        // User 2 participates in session 1
        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        // User 2 cancels participation in session 1
        mockMvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    @DirtiesContext
    @DisplayName("return bad request when user cancels participation with invalid session id")
    void shouldReturnBadRequestWhenUserCancelsParticipationWithInvalidSessionId() throws Exception {

        mockMvc.perform(delete("/api/session/invalid-id/participate/2"))
                .andExpect(status().isBadRequest());
    }

}
