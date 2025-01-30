package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;

@SpringBootTest
class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    void shouldMapSessionToSessionDto() {
        Session sessionNullCase = null;
        assertNull(sessionMapper.toDto(sessionNullCase));
    }

    @Test
    void shouldMapSessionDtoToSession() {
        SessionDto sessionDtoNullCase = null;
        assertNull(sessionMapper.toEntity(sessionDtoNullCase));
    }

    @Test
    void shouldMapSessionDtoListToSessionList() {
        List<SessionDto> sessionDtosNullCase = null;
        assertNull(sessionMapper.toEntity(sessionDtosNullCase));

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Session name");
        sessionDto.setDescription("Session description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.emptyList());

        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(sessionDto);

        List<Session> sessions = sessionMapper.toEntity(sessionDtos);

        assertAll(
                () -> assertEquals(sessionDtos.size(), sessions.size()),
                () -> assertEquals(sessionDtos.get(0).getName(), sessions.get(0).getName()),
                () -> assertEquals(sessionDtos.get(0).getDescription(), sessions.get(0).getDescription()),
                () -> assertEquals(sessionDtos.get(0).getTeacher_id(), sessions.get(0).getTeacher().getId()));
    }

    @Test
    void shouldMapSessionListToSessionDtoList() {
        List<Session> sessionsNullCase = null;
        assertNull(sessionMapper.toDto(sessionsNullCase));
    }

}
