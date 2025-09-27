package lk.ijse.elitedrivingschool.bo.custom;

import lk.ijse.elitedrivingschool.dto.UserDto;
import lk.ijse.elitedrivingschool.exception.InvalidCredentialsException;

import java.util.List;

public interface UserBo {
    UserDto login(UserDto userDto) throws InvalidCredentialsException;
    void registerUser(UserDto userDto);
    void updateUser(UserDto userDto);
    void changePassword(String username, String newPassword, String role);
    void deleteUser(String username);
    List<UserDto> getAllUsers();
}