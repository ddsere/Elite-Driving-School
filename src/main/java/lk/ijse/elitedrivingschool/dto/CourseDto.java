package lk.ijse.elitedrivingschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDto {
    private String courseId;
    private String courseName;
    private String duration;
    private Double fee;
}
