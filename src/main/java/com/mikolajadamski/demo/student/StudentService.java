package com.mikolajadamski.demo.student;

import com.mikolajadamski.demo.utils.EmailValidator;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if(emailValidator.test(student.getEmail()))
        {
            Optional<Student> result = studentRepository.findByEmail(student.getEmail());
            if(result.isEmpty()){
                studentRepository.save(student);
            }
            else {
                throw new RuntimeException("Email already taken");
            }
        }
        else {
            throw new RuntimeException("Email is incorrect");
        }

    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
