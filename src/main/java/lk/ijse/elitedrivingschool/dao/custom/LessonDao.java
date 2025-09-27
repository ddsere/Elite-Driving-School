package lk.ijse.elitedrivingschool.dao.custom;

import lk.ijse.elitedrivingschool.entity.Lesson;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface LessonDao {
    void save(Lesson lesson);
    Optional<Lesson> findById(Integer id);
    List<Lesson> findAll();
    void update(Lesson lesson);
    void delete(Lesson lesson);
    boolean isInstructorBusy(Integer instructorId, LocalDateTime scheduledTime, String duration);
    boolean isInstructorBusy(Integer instructorId, LocalDateTime scheduledTime, String duration, Integer lessonId);
}