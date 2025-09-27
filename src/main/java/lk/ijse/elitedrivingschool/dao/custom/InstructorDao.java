package lk.ijse.elitedrivingschool.dao.custom;

import lk.ijse.elitedrivingschool.entity.Instructor;
import java.util.List;
import java.util.Optional;

public interface InstructorDao {
    void save(Instructor instructor);
    Optional<Instructor> findById(Integer id);
    List<Instructor> findAll();
    void update(Instructor instructor);
    void delete(Instructor instructor);
}
