package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("TeacherController should")
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("find teacher by id")
    void shouldFindTeacherById() throws Exception {

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("DELAHAYE"))
                .andExpect(jsonPath("$.firstName").value("Margot"));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("return not found when teacher does not exist")
    void shouldReturnNotFoundWhenTeacherDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/teacher/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("return bad request when teacher id is invalid")
    void shouldReturnBadRequestWhenTeacherIdIsInvalid() throws Exception {

        mockMvc.perform(get("/api/teacher/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    @DisplayName("find all teachers")
    void shouldFindAllTeachers() throws Exception {

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[0].lastName").value("DELAHAYE"))
                .andExpect(jsonPath("[0].firstName").value("Margot"));
    }

}
