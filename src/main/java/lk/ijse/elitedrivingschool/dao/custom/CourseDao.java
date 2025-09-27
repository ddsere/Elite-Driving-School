// edu.icet.elite.dao.custom.CourseDao
package lk.ijse.elitedrivingschool.dao.custom;

import lk.ijse.elitedrivingschool.entity.Course;
import java.util.List;
import java.util.Optional;

public interface CourseDao {
    void save(Course course);
    Optional<Course> findById(String id);
    List<Course> findAll();
    void update(Course course);
    void delete(Course course);
    long countAllCourses();
}