package lk.ijse.elitedrivingschool.bo.custom.impl;

import lk.ijse.elitedrivingschool.bo.custom.LessonBo;
import lk.ijse.elitedrivingschool.dao.custom.CourseDao;
import lk.ijse.elitedrivingschool.dao.custom.InstructorDao;
import lk.ijse.elitedrivingschool.dao.custom.LessonDao;
import lk.ijse.elitedrivingschool.dao.custom.StudentDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.CourseDaoImpl;
import lk.ijse.elitedrivingschool.dao.custom.impl.InstructorDaoImpl;
import lk.ijse.elitedrivingschool.dao.custom.impl.LessonDaoImpl;
import lk.ijse.elitedrivingschool.dao.custom.impl.StudentDaoImpl;
import lk.ijse.elitedrivingschool.dto.LessonDto;
import lk.ijse.elitedrivingschool.entity.Course;
import lk.ijse.elitedrivingschool.entity.Instructor;
import lk.ijse.elitedrivingschool.entity.Lesson;
import lk.ijse.elitedrivingschool.entity.Student;
import lk.ijse.elitedrivingschool.exception.SchedulingConflictException;

import java.util.List;
import java.util.stream.Collectors;

public class LessonBoImpl implements LessonBo {

    private final LessonDao lessonDao = new LessonDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final InstructorDao instructorDao = new InstructorDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();

    @Override
    public void scheduleLesson(LessonDto lessonDto) throws SchedulingConflictException {
        // Find entities from DTO IDs
        Student student = studentDao.findById(lessonDto.getStudentId())
                .orElseThrow(() -> new SchedulingConflictException("Student not found."));
        Instructor instructor = instructorDao.findById(lessonDto.getInstructorId())
                .orElseThrow(() -> new SchedulingConflictException("Instructor not found."));
        Course course = courseDao.findById(lessonDto.getCourseId())
                .orElseThrow(() -> new SchedulingConflictException("Course not found."));

        // Check for scheduling conflicts
        // Removed reference to course.getDuration()
        if (lessonDao.isInstructorBusy(instructor.getInstructorId(), lessonDto.getScheduledTime(), course.getDuration())) {
            throw new SchedulingConflictException("The selected instructor is busy at the scheduled time.");
        }

        // Create and save the new lesson
        Lesson lesson = new Lesson();
        lesson.setStudent(student);
        lesson.setInstructor(instructor);
        lesson.setCourse(course);
        lesson.setScheduledTime(lessonDto.getScheduledTime());
        lesson.setStatus("SCHEDULED");

        lessonDao.save(lesson);
    }

    @Override
    public void rescheduleLesson(Integer lessonId, LessonDto lessonDto) throws SchedulingConflictException {
        Lesson lesson = lessonDao.findById(lessonId)
                .orElseThrow(() -> new SchedulingConflictException("Lesson not found to reschedule."));

        // Find entities from DTO IDs
        Student student = studentDao.findById(lessonDto.getStudentId())
                .orElseThrow(() -> new SchedulingConflictException("Student not found."));
        Instructor instructor = instructorDao.findById(lessonDto.getInstructorId())
                .orElseThrow(() -> new SchedulingConflictException("Instructor not found."));
        Course course = courseDao.findById(lessonDto.getCourseId())
                .orElseThrow(() -> new SchedulingConflictException("Course not found."));

        // Check for scheduling conflicts, excluding the current lesson being updated
        // Removed reference to course.getDuration()
        if (lessonDao.isInstructorBusy(instructor.getInstructorId(), lessonDto.getScheduledTime(), course.getDuration(), lessonId)) {
            throw new SchedulingConflictException("The selected instructor is busy at the new scheduled time.");
        }

        // Update the existing lesson entity
        lesson.setStudent(student);
        lesson.setInstructor(instructor);
        lesson.setCourse(course);
        lesson.setScheduledTime(lessonDto.getScheduledTime());
        lesson.setStatus("SCHEDULED");

        lessonDao.update(lesson);
    }

    @Override
    public void cancelLesson(Integer lessonId) {
        lessonDao.findById(lessonId).ifPresent(lesson -> {
            lesson.setStatus("CANCELED");
            lessonDao.update(lesson);
        });
    }

    @Override
    public List<LessonDto> getAllLessons() {
        return lessonDao.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // getDuration method has been removed
    // @Override
    // public String getDuration(String courseId) {
    //     return courseDao.findById(courseId).map(Course::getDuration).orElse("N/A");
    // }

    // Helper method to map Entity to DTO
    private LessonDto mapToDto(Lesson lesson) {
        return new LessonDto(
                lesson.getLessonId(),
                lesson.getStudent().getStudentId(),
                lesson.getInstructor().getInstructorId(),
                lesson.getCourse().getCourseId(),
                lesson.getScheduledTime(),
                lesson.getStatus()
        );
    }
}