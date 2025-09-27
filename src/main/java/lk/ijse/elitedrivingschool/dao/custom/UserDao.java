package lk.ijse.elitedrivingschool.dao.custom;

import lk.ijse.elitedrivingschool.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User user);
    Optional<User> findByUsername(String username);
    void update(User user);
    void delete(User user);
    List<User> findAllUsers();
}