// edu.icet.elite.bo.custom.impl.CourseBoImpl
package lk.ijse.elitedrivingschool.bo.custom.impl;

import lk.ijse.elitedrivingschool.bo.custom.CourseBo;
import lk.ijse.elitedrivingschool.dao.custom.CourseDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.CourseDaoImpl;
import lk.ijse.elitedrivingschool.dto.CourseDto;
import lk.ijse.elitedrivingschool.entity.Course;

import java.util.List;
import java.util.stream.Collectors;

public class CourseBoImpl implements CourseBo {

    private final CourseDao courseDao = new CourseDaoImpl();

    @Override
    public void addCourse(CourseDto courseDto) {
        Course course = new Course(
                courseDto.getCourseId(),
                courseDto.getCourseName(),
                courseDto.getDuration(),
                courseDto.getFee()
        );
        courseDao.save(course);
    }

    @Override
    public CourseDto getCourseById(String id) {
        return courseDao.findById(id)
                .map(course -> new CourseDto(
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getDuration(),
                        course.getFee()))
                .orElse(null);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseDao.findAll().stream()
                .map(course -> new CourseDto(
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getDuration(),
                        course.getFee()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCourse(CourseDto courseDto) {
        Course course = new Course(
                courseDto.getCourseId(),
                courseDto.getCourseName(),
                courseDto.getDuration(),
                courseDto.getFee()
        );
        courseDao.update(course);
    }

    @Override
    public void deleteCourse(String id) {
        courseDao.findById(id).ifPresent(courseDao::delete);
    }

    @Override
    public List<String> getAllCourseNames() {
        return courseDao.findAll().stream()
                .map(Course::getCourseName)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseByName(String name) {
        return courseDao.findAll().stream()
                .filter(c -> c.getCourseName().equalsIgnoreCase(name))
                .findFirst()
                .map(course -> new CourseDto(
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getDuration(),
                        course.getFee()))
                .orElse(null);
    }

    // New method added to count courses
    @Override
    public long countAllCourses() {
        return courseDao.countAllCourses();
    }

    @Override
    public List<CourseDto> searchCourses(String query) {
        return List.of();
    }
}