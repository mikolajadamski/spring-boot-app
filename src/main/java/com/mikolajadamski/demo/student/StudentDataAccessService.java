package com.mikolajadamski.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> selectAllStudents() {
        String sql = "" +
                "SELECT " +
                "student_id, " +
                "first_name, " +
                "last_name, " +
                "email, " +
                "gender " +
                "FROM student";
        return jdbcTemplate.query(sql, mapStudentFromDb());
    }

    private RowMapper<Student> mapStudentFromDb() {
        return (resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id");
            UUID studentId = UUID.fromString(studentIdStr);

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");

            String genderStr = resultSet.getString("gender").toUpperCase();
            Gender gender = Gender.valueOf(genderStr);
            return new Student(
                    studentId,
                    firstName,
                    lastName,
                    email,
                    gender
            );
        };
    }

    public int insertStudent(UUID studentId, Student student) {
        String sql = "" +
                "INSERT INTO student (student_id, first_name, last_name, email, gender) " +
                "VALUES (?, ?, ?, ?, ?::gender)";
        return jdbcTemplate.update(sql,
                studentId,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().name().toUpperCase()
        );
    }

    /** @noinspection ConstantConditions*/
    boolean isEmailTaken(String email) {
        String sql = "SELECT EXISTS (" +
                " SELECT 1" +
                " FROM student" +
                " WHERE email = ?" +
                " )";
        boolean val =  jdbcTemplate.queryForObject(
                sql,
                new Object[] {email},
                (resultSet, i)-> resultSet.getBoolean(1)
        );
        return val;
    }
//    UUID studentId,
//    UUID courseId,
//    String name,
//    String description,
//    String department,
//    String teacherName,
//    LocalDate startDate,
//    LocalDate endDate,
//    Integer grade
    public List<StudentCourse> selectAllCoursesForStudent(UUID studentId) {
        String sql = "" +
                "SELECT student_id, course_id, name, description, department, teacher_name, start_date, end_date, grade " +
                "FROM student " +
                "JOIN student_course USING (student_id) " +
                "JOIN course USING (course_id) " +
                "WHERE student.student_id = ?";

        return jdbcTemplate.query(sql, new Object[]{studentId}, mapStudentCourseFromDb());
    }

    private RowMapper<StudentCourse> mapStudentCourseFromDb() {
        return (resultSet, i) -> {
            UUID studentId = UUID.fromString(resultSet.getString("student_id"));
            UUID courseId = UUID.fromString(resultSet.getString("course_id"));
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String department = resultSet.getString("department");
            String teacherName = resultSet.getString("teacher_name");
            LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
            LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
            Integer grade = Optional.ofNullable(resultSet.getString("grade"))
                    .map(Integer::parseInt)
                    .orElse(null);

            return new StudentCourse(studentId,
                    courseId,
                    name,
                    description,
                    department,
                    teacherName,
                    startDate,
                    endDate,
                    grade
            );
        };
    }
}
