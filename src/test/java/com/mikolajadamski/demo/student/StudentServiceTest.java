package com.mikolajadamski.demo.student;

import com.mikolajadamski.demo.exception.BadRequestException;
import com.mikolajadamski.demo.exception.StudentNotFoundException;
import com.mikolajadamski.demo.utils.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private EmailValidator emailValidator;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository, emailValidator);
    }

    @Test
    void canGetAllStudents() {
        underTest.getAllStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void canAddNewStudent() {
        Student student = new Student(
                "Tom",
                "Johns",
                "tjohns@gmail.com",
                Gender.MALE
        );
        given(emailValidator.test(anyString()))
                .willReturn(true);


        underTest.addNewStudent(student);


        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        Student student = new Student(
                "Tom",
                "Johns",
                "tjohns@gmail.com",
                Gender.MALE
        );

        given(emailValidator.test(anyString()))
                .willReturn(true);

        given(studentRepository.existsByEmail(anyString()))
                .willReturn(true);



        assertThatThrownBy(() -> underTest.addNewStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email already taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void willThrowWhenEmailInvalid() {
        Student student = new Student(
                "Tom",
                "Johns",
                "tjohns@gmail.com",
                Gender.MALE
        );
        given(emailValidator.test(anyString()))
                .willReturn(false);


        assertThatThrownBy(() -> underTest.addNewStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email is incorrect");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void canDeleteStudent() {
        long id = 1;

        given(studentRepository.existsById(id))
                .willReturn(true);


        underTest.deleteStudent(id);


        verify(studentRepository).deleteById(id);
    }

    @Test
    void willThrowWhenStudentNotFound() {
        long id = 1;

        given(studentRepository.existsById(id))
                .willReturn(false);


        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student " + id + " does not exist");

        verify(studentRepository, never()).deleteById(any());
    }
}