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
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
@ActiveProfiles("test")
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    private List<TeacherDto> fakeTeacherDtos;

    @BeforeEach
    void init() {
        fakeTeacherDtos = getFakeTeacherDtos();
    }

    @Test
    void shouldMapTeacherDtoToTeacherEntity() {
        TeacherDto teacherDtoNullCase = null;
        assertNull(teacherMapper.toEntity(teacherDtoNullCase));

        TeacherDto teacherDto = fakeTeacherDtos.get(0);
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        assertAll(
                () -> assertEquals(teacherDto.getFirstName(), teacher.getFirstName()),
                () -> assertEquals(teacherDto.getLastName(), teacher.getLastName()));
    }

    @Test
    void shouldMapTeacherToTeacherDto() {
        Teacher teacherNullCase = null;
        assertNull(teacherMapper.toDto(teacherNullCase));
    }

    @Test
    void shouldMapTeacherListToTeacherDtoList() {
        List<Teacher> teachersNullCase = null;
        assertNull(teacherMapper.toDto(teachersNullCase));
    }

    @Test
    void shouldMapTeacherDtoListToTeacherList() {
        List<TeacherDto> teacherDtosNullCase = null;
        assertNull(teacherMapper.toEntity(teacherDtosNullCase));

        List<Teacher> teachers = teacherMapper.toEntity(fakeTeacherDtos);
        assertAll(
                () -> assertEquals(fakeTeacherDtos.size(), teachers.size()),
                () -> assertEquals(fakeTeacherDtos.get(0).getId(), teachers.get(0).getId()),
                () -> assertEquals(fakeTeacherDtos.get(0).getFirstName(), teachers.get(0).getFirstName()),
                () -> assertEquals(fakeTeacherDtos.get(0).getLastName(), teachers.get(0).getLastName()));
    }

    private List<TeacherDto> getFakeTeacherDtos() {

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setFirstName("teacherFirstName");
        teacherDto.setLastName("teacherLastName");

        return Arrays.asList(teacherDto);
    }
}
