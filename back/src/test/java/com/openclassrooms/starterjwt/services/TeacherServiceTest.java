package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@DisplayName("TeacherService should")
class TeacherServiceTest {

    private TeacherService teacherService;
    private TeacherRepository teacherRepositoryMock = mock(TeacherRepository.class);
    private List<Teacher> fakeTeachers;

    @BeforeEach
    void init() {
        fakeTeachers = getFakeTeachers();
        teacherService = new TeacherService(teacherRepositoryMock);
    }

    @Test
    @DisplayName("find all teachers")
    void shouldFindAllTeachers() {
        when(teacherRepositoryMock.findAll()).thenReturn(fakeTeachers);
        List<Teacher> teachers = teacherService.findAll();
        assertIterableEquals(fakeTeachers, teachers);
    }

    @Test
    @DisplayName("find teacher by id")
    void shouldfindTeacherById() {
        when(teacherRepositoryMock.findById(1L)).thenReturn(Optional.of(fakeTeachers.get(0)));
        Teacher teacher = teacherService.findById(1L);
        assertNotNull(teacher);
        assertEquals(fakeTeachers.get(0), teacher);
    }

    @Test
    @DisplayName("return null when teacher cannot be found by id")
    void shouldReturnNullWhenTeacherCannotBeFoundById() {
        when(teacherRepositoryMock.findById(99L)).thenReturn(Optional.empty());
        Teacher teacher = teacherService.findById(99L);
        assertNull(teacher);
    }

    private List<Teacher> getFakeTeachers() {
        Teacher margot = new Teacher().setId(1L).setFirstName("Margot").setLastName("DELAHAYE");
        Teacher helene = new Teacher().setId(2L).setFirstName("Hélène").setLastName("THIERCELIN");

        return Arrays.asList(margot, helene);
    }

}
