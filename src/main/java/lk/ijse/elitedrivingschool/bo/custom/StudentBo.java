package lk.ijse.elitedrivingschool.bo.custom;

import lk.ijse.elitedrivingschool.dto.StudentDto;
import lk.ijse.elitedrivingschool.exception.RegistrationException;

import java.util.List;

public interface StudentBo {
    void registerStudent(StudentDto studentDto) throws RegistrationException;
    List<StudentDto> getAllStudents();
    boolean deleteStudent(Integer studentId);
    void updateStudent(StudentDto studentDto);
    StudentDto getStudentById(Integer studentId);
    List<StudentDto> getAllStudentsWithCourses();
    String getCourseNameById(String courseId);
}