package lk.ijse.elitedrivingschool.dao.custom;

import lk.ijse.elitedrivingschool.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentDao {
    void save(Student student);
    Optional<Student> findById(Integer id);
    List<Student> findAll();
    void update(Student student);
    void delete(Student student);
    List<Student> findAllWithCourses();
    List<Student> findStudentsEnrolledInAllCourses();
}