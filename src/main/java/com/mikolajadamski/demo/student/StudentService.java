package com.mikolajadamski.demo.student;

import com.mikolajadamski.demo.exception.ApiRequestException;
import com.mikolajadamski.demo.utils.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final EmailValidator emailValidator;

    @Autowired
    public StudentService(StudentRepository studentRepository, EmailValidator emailValidator) {
        this.studentRepository = studentRepository;
        this.emailValidator = emailValidator;
    }

    List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    void addNewStudent(Student student) {
        studentRepository.save(student);
    }

}
