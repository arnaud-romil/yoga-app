package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@DisplayName("SessionService should")
class SessionServiceTest {

    private SessionService sessionService;
    private SessionRepository sessionRepositoryMock = mock(SessionRepository.class);
    private UserRepository userRepositoryMock = mock(UserRepository.class);
    private List<Session> fakSessions;
    private List<User> fakeUsers;

    @BeforeEach
    void init() {
        sessionService = new SessionService(sessionRepositoryMock, userRepositoryMock);
        fakSessions = getFakeSessions();
        fakeUsers = getFakeUsers();
    }

    @Test
    @DisplayName("create a session")
    void shouldCreateSession() {
        when(sessionRepositoryMock.save(fakSessions.get(1)))
                .thenReturn(fakSessions.get(1).setId(100L));
        Session sessionCreated = sessionService.create(fakSessions.get(1));
        assertNotNull(sessionCreated);
        assertEquals(fakSessions.get(1), sessionCreated);
    }

    @Test
    @DisplayName("delete session by id")
    void shouldDeleteSessionById() {
        final long sessionId = 95L;
        sessionService.delete(sessionId);
        verify(sessionRepositoryMock).deleteById(sessionId);
    }

    @Test
    @DisplayName("find all sessions")
    void shouldFindAllSessions() {
        when(sessionRepositoryMock.findAll()).thenReturn(fakSessions);
        List<Session> sessions = sessionService.findAll();
        assertIterableEquals(fakSessions, sessions);
    }

    @Test
    @DisplayName("find session by id")
    void shouldFindSessionById() {
        final long sessionId = 45L;
        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(fakSessions.get(0)));
        Session session = sessionService.getById(sessionId);
        assertNotNull(session);
        assertEquals(fakSessions.get(0), session);
    }

    @Test
    @DisplayName("return null when session cannot be found by id")
    void shouldReturnNullWhenSessionCannotBeFoundById() {
        final long sessionId = 86L;
        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.empty());
        Session session = sessionService.getById(sessionId);
        assertNull(session);
    }

    @Test
    @DisplayName("update a session")
    void shouldUpdateSession() {
        final long sessionId = fakSessions.get(0).getId();
        Session toBeUpdated = fakSessions.get(0)
                .setId(null)
                .setDescription("description updated");
        assertNull(toBeUpdated.getId());
        when(sessionRepositoryMock.save(toBeUpdated.setId(sessionId))).thenReturn(toBeUpdated);
        Session updated = sessionService.update(sessionId, toBeUpdated);
        assertEquals(toBeUpdated, updated);
    }

    @Test
    @DisplayName("throw NotFoundException when session is not found for user participation")
    void shouldThrowNotFoundExceptionWhenSessionIsNotFoundForUserParticipation() {
        final long sessionId = 99L;
        final long userid = fakeUsers.get(0).getId();
        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepositoryMock.findById(userid)).thenReturn(Optional.of(fakeUsers.get(0)));
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userid);
        });
    }

    @Test
    @DisplayName("throw NotFoundException when user is not found for user participation")
    void shouldThrowNotFoundExceptionWhenUserIsNotFoundForUserParticipation() {
        final long sessionId = fakSessions.get(0).getId();
        final long userid = 99L;
        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(fakSessions.get(0)));
        when(userRepositoryMock.findById(userid)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userid);
        });
    }

    @Test
    @DisplayName("throw BadRequestException when user is already participating in session")
    void shouldThrowBadRequestExceptionWhenUserIsAlreadyParticipatingInSession() {
        final Session session = fakSessions.get(0)
                .setUsers(Arrays.asList(fakeUsers.get(0)));
        final User user = fakeUsers.get(0);
        final long sessionId = session.getId();
        final long userid = user.getId();

        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepositoryMock.findById(userid)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, userid);
        });
    }

    @Test
    @DisplayName("allow user to participate in session")
    void shouldAllowUserToParticipateInSession() {
        final Session session = fakSessions.get(0)
                .setUsers(new ArrayList<>());
        final User user = fakeUsers.get(0);
        final long sessionId = session.getId();
        final long userId = user.getId();

        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
        sessionService.participate(sessionId, userId);
        session.setUsers(Arrays.asList(user));
        verify(sessionRepositoryMock).save(session);
    }

    @Test
    @DisplayName("throw NotFoundException when user cancels participation to non existing session")
    void shouldThrowNotFoundExceptionWhenUserCancelsParticipationToNonExistingSession() {
        final long sessionId = 99L;
        final long userId = fakeUsers.get(0).getId();
        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });
    }

    @Test
    @DisplayName("throw BadRequestException when user cancels participation to wrong session")
    void shouldThrowBadRequestExceptionWhenUserCancelsParticipationToWrongSession() {
        final Session session = fakSessions.get(0).setUsers(Collections.emptyList());
        final User user = fakeUsers.get(0);
        final long sessionId = session.getId();
        final long userId = user.getId();
        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(session));
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });
    }

    @Test
    @DisplayName("allow user to cancel participation to session")
    void shouldAllowUserToCancelParticipationToSession() {
        final User user = fakeUsers.get(0);
        final Session session = fakSessions.get(0).setUsers(Arrays.asList(user));
        final long sessionId = session.getId();
        final long userId = user.getId();
        when(sessionRepositoryMock.findById(sessionId)).thenReturn(Optional.of(session));
        sessionService.noLongerParticipate(sessionId, userId);
        verify(sessionRepositoryMock).save(session.setUsers(Collections.emptyList()));
    }

    private List<Session> getFakeSessions() {

        Teacher margot = new Teacher()
                .setId(1L)
                .setFirstName("Margot")
                .setLastName("DELAHAYE");

        Session sessionInDB = new Session()
                .setId(45L)
                .setName("Session from DB")
                .setDate(new Date())
                .setDescription("Session description")
                .setTeacher(margot)
                .setUsers(Collections.emptyList())
                .setCreatedAt(LocalDateTime.now().minusDays(45L))
                .setUpdatedAt(LocalDateTime.now().minusDays(45L));

        Session sessionToCreate = new Session()
                .setName("New Session")
                .setDate(new Date())
                .setDescription("Session description")
                .setTeacher(margot)
                .setUsers(Collections.emptyList());

        return Arrays.asList(sessionInDB, sessionToCreate);
    }

    private List<User> getFakeUsers() {
        User user = new User()
                .setId(66L)
                .setEmail("user@test.com")
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setPassword("user-password!")
                .setAdmin(false)
                .setCreatedAt(LocalDateTime.now().minusDays(20L))
                .setUpdatedAt(LocalDateTime.now().minusDays(20l));
        return Arrays.asList(user);
    }

}
