package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.CourseBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.CourseBoImpl;
import lk.ijse.elitedrivingschool.bo.custom.StudentBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.StudentBoImpl;
import lk.ijse.elitedrivingschool.dto.CourseDto;
import lk.ijse.elitedrivingschool.dto.StudentDto;
import lk.ijse.elitedrivingschool.tm.EnrollmentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CourseEnrollController implements Initializable {

    // --- FXML UI Components ---
    @FXML
    private ComboBox<CourseDto> selectCourseCmb;
    @FXML
    private TextField feeTxt;
    @FXML
    private TextField studentIdTxt;
    @FXML
    private TextField studentNameTxt;
    @FXML
    private TextField durationTxt;
    @FXML
    private DatePicker enrollmentDateDp; // Added this component
    @FXML
    private Label enrollmentCountLbl;
    @FXML
    private Button enrollNowBtn;
    @FXML
    private Button clearFormBtn;
    @FXML
    private Button findStudentBtn; // Added this component

    // --- Data and Dependencies ---
    private final CourseBo courseBo = new CourseBoImpl();
    private final StudentBo studentBo = new StudentBoImpl();
    private final ObservableList<EnrollmentTm> enrollmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCourses();
        loadAllEnrolledStudents();
    }

    private void loadCourses() {
        List<CourseDto> courses = courseBo.getAllCourses();
        selectCourseCmb.setConverter(new StringConverter<CourseDto>() {
            @Override
            public String toString(CourseDto courseDto) {
                return courseDto != null ? courseDto.getCourseName() : "";
            }

            @Override
            public CourseDto fromString(String s) {
                return null;
            }
        });
        selectCourseCmb.getItems().addAll(courses);
    }

    private void loadAllEnrolledStudents() {
        enrollmentList.clear();
        List<StudentDto> allStudents = studentBo.getAllStudentsWithCourses();

        for (StudentDto student : allStudents) {
            if (student.getCourseIds() != null) {
                for (String courseId : student.getCourseIds()) {
                    CourseDto course = courseBo.getCourseById(courseId);
                    if (course != null) {
                        enrollmentList.add(new EnrollmentTm(
                                student.getStudentId(),
                                student.getName(),
                                course.getCourseId(),
                                course.getCourseName()
                        ));
                    }
                }
            }
        }
        updateEnrollmentCount();
    }

    private void updateEnrollmentCount() {
        enrollmentCountLbl.setText("Total Enrollments: " + enrollmentList.size());
    }

    @FXML
    void onSelectCourse() {
        CourseDto selectedCourse = selectCourseCmb.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            feeTxt.setText(String.format("%.2f", selectedCourse.getFee()));
            durationTxt.setText(selectedCourse.getDuration());
        } else {
            feeTxt.clear();
            durationTxt.clear();
        }
    }

    @FXML
    void onStudentIdTyped() {
        try {
            Integer studentId = Integer.parseInt(studentIdTxt.getText());
            StudentDto studentDto = studentBo.getStudentById(studentId);
            if (studentDto != null) {
                studentNameTxt.setText(studentDto.getName());
            } else {
                studentNameTxt.clear();
            }
        } catch (NumberFormatException e) {
            studentNameTxt.clear();
        }
    }

    @FXML
    void onEnrollNowBtn() {
        try {
            Integer studentId = Integer.parseInt(studentIdTxt.getText());
            CourseDto selectedCourse = selectCourseCmb.getSelectionModel().getSelectedItem();

            if (selectedCourse == null || studentIdTxt.getText().isEmpty() || studentNameTxt.getText().isEmpty() || enrollmentDateDp.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
                return;
            }

            StudentDto studentDto = studentBo.getStudentById(studentId);
            if (studentDto == null) {
                new Alert(Alert.AlertType.ERROR, "Student not found with this ID.").show();
                return;
            }

            if (studentDto.getCourseIds() != null && studentDto.getCourseIds().contains(selectedCourse.getCourseId())) {
                new Alert(Alert.AlertType.WARNING, "Student is already enrolled in this course.").show();
                return;
            }

            if (studentDto.getCourseIds() == null) {
                studentDto.setCourseIds(new java.util.HashSet<>());
            }
            studentDto.getCourseIds().add(selectedCourse.getCourseId());

            studentBo.updateStudent(studentDto);

            new Alert(Alert.AlertType.INFORMATION, "Enrollment successful!").show();

            loadAllEnrolledStudents();
            clearForm();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Student ID! Please enter a valid number.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to enroll: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void onClearFormBtn() {
        clearForm();
    }

    private void clearForm() {
        studentIdTxt.clear();
        studentNameTxt.clear();
        selectCourseCmb.getSelectionModel().clearSelection();
        feeTxt.clear();
        durationTxt.clear();
        enrollmentDateDp.setValue(null);
    }

    // --- Button Hover Effects ---

    @FXML
    private void handleEnrollButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        button.setScaleX(1.05);
        button.setScaleY(1.05);
    }

    @FXML
    private void handleEnrollButtonExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #1a2a43; -fx-text-fill: #a6c4ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }

    @FXML
    private void handleClearButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ffb74d; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        button.setScaleX(1.05);
        button.setScaleY(1.05);
    }

    @FXML
    private void handleClearButtonExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #1a2a43; -fx-text-fill: #d4e1f7; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #4b6a9a; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }

    @FXML
    private void handleFindStudentHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        button.setScaleX(1.05);
        button.setScaleY(1.05);
    }

    @FXML
    private void handleFindStudentExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #1a2a43; -fx-text-fill: #a6c4ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }
}