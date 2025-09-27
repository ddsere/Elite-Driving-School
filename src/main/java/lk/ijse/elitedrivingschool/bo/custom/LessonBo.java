package lk.ijse.elitedrivingschool.bo.custom;

import lk.ijse.elitedrivingschool.dto.LessonDto;
import lk.ijse.elitedrivingschool.exception.SchedulingConflictException;

import java.util.List;

public interface LessonBo {
    void scheduleLesson(LessonDto lessonDto) throws SchedulingConflictException;
    void rescheduleLesson(Integer lessonId, LessonDto lessonDto) throws SchedulingConflictException;
    void cancelLesson(Integer lessonId);
    List<LessonDto> getAllLessons();
    // getDuration method has been removed
    // String getDuration(String courseId);
}