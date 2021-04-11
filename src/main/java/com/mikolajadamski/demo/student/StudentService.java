package com.mikolajadamski.demo.student;

import com.mikolajadamski.demo.exception.BadRequestException;
import com.mikolajadamski.demo.exception.StudentNotFoundException;
import com.mikolajadamski.demo.utils.EmailValidator;
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

            if(!studentRepository.existsByEmail(student.getEmail())){
                studentRepository.save(student);
            }
            else {
                throw new BadRequestException("Email already taken");
            }
        }
        else {
            throw new BadRequestException("Email is incorrect");
        }

    }

    public void deleteStudent(Long id) {
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
        }
        else {
            throw new StudentNotFoundException("Student " + id + " does not exist");
        }
    }
}
