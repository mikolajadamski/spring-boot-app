package com.mikolajadamski.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindStudentByEmail(){
        String email = "tjohns@gmail.com";
        Student student = new Student(
                "Tom",
                "Johns",
                email,
                Gender.MALE
        );
        underTest.save(student);

        boolean emailExists = underTest.existsByEmail(email);

        assertThat(emailExists).isTrue();
    }

    @Test
    void itShouldNotFindStudentByEmail(){
        String email = "tjohns@gmail.com";

        boolean emailExists = underTest.existsByEmail(email);

        assertThat(emailExists).isFalse();
    }
}
