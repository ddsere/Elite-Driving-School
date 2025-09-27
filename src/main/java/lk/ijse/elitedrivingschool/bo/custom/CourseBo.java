// edu.icet.elite.bo.custom.CourseBo
package lk.ijse.elitedrivingschool.bo.custom;

import lk.ijse.elitedrivingschool.dto.CourseDto;
import java.util.List;

public interface CourseBo {
    void addCourse(CourseDto courseDto);
    CourseDto getCourseById(String id);
    List<CourseDto> getAllCourses();
    void updateCourse(CourseDto courseDto);
    void deleteCourse(String id);
    List<String> getAllCourseNames();
    CourseDto getCourseByName(String name);
    // New method added
    long countAllCourses();

    List<CourseDto> searchCourses(String query);
}