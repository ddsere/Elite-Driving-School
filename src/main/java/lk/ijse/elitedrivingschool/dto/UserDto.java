package lk.ijse.elitedrivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String username;
    private String password; // Plain text from UI, hashed before storage
    private String role;
}