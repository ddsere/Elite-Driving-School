package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.CourseBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.CourseBoImpl;
import lk.ijse.elitedrivingschool.dto.CourseDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageCourseController implements Initializable {

    @FXML
    private Button addBtn;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField courseSearchTxt;
    @FXML
    private TableView<CourseDto> courseTable;
    @FXML
    private TableColumn<CourseDto, String> colCourseId;
    @FXML
    private TableColumn<CourseDto, String> colCourseName;
    @FXML
    private TableColumn<CourseDto, String> colDuration;
    @FXML
    private TableColumn<CourseDto, Double> colFee;
    @FXML
    private Button deleteBtn;
    @FXML
    private Label totalCoursesLbl;
    @FXML
    private Button updateBtn;
    @FXML
    private Label lblStatus;

    private final CourseBo courseBo = new CourseBoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadTable();
        updateTotalCoursesLabel();
    }

    private void setupTable() {
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    private void loadTable() {
        try {
            List<CourseDto> courses = courseBo.getAllCourses();
            ObservableList<CourseDto> observableList = FXCollections.observableArrayList(courses);
            courseTable.setItems(observableList);
            lblStatus.setText("Status: Table loaded successfully.");
        } catch (Exception e) {
            lblStatus.setText("Status: Error loading courses.");
            new Alert(Alert.AlertType.ERROR, "Failed to load courses: " + e.getMessage()).show();
        }
    }

    private void updateTotalCoursesLabel() {
        long total = courseBo.countAllCourses();
        totalCoursesLbl.setText("Total Courses: " + total);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String query = courseSearchTxt.getText().trim();
        if (query.isEmpty()) {
            loadTable();
            return;
        }

        try {
            List<CourseDto> searchResults = courseBo.searchCourses(query);
            ObservableList<CourseDto> observableList = FXCollections.observableArrayList(searchResults);
            courseTable.setItems(observableList);
            if (searchResults.isEmpty()) {
                lblStatus.setText("Status: No courses found.");
            } else {
                lblStatus.setText("Status: Search complete. " + searchResults.size() + " courses found.");
            }
        } catch (Exception e) {
            lblStatus.setText("Status: Search failed.");
            new Alert(Alert.AlertType.ERROR, "Search failed: " + e.getMessage()).show();
        }
    }

    @FXML
    void addBtnOnAction(ActionEvent event) {
        // Here you would open the "Add Course" dialog/window.
        // For this example, I'll simulate the action with an alert.
        showAlert("Add Course", "Opening 'Add Course' window...", Alert.AlertType.INFORMATION);
        // You can add logic here to open a new FXML file, similar to the RegisterStudentController.
    }

    @FXML
    void updateBtnOnAction(ActionEvent event) {
        CourseDto selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("Selection Error", "Please select a course to update.", Alert.AlertType.ERROR);
            return;
        }
        // Open the update dialog/window with selectedCourse details
        showAlert("Update Course", "Opening 'Update Course' window for " + selectedCourse.getCourseName() + "...", Alert.AlertType.INFORMATION);
    }

    @FXML
    void deleteBtnOnAction(ActionEvent event) {
        CourseDto selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert("Selection Error", "Please select a course to delete.", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete '" + selectedCourse.getCourseName() + "'?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                courseBo.deleteCourse(selectedCourse.getCourseId());
                showAlert("Success", "Course deleted successfully.", Alert.AlertType.INFORMATION);
                loadTable();
                updateTotalCoursesLabel();
                lblStatus.setText("Status: Course deleted.");
            } catch (Exception e) {
                showAlert("Deletion Failed", "Failed to delete the course: " + e.getMessage(), Alert.AlertType.ERROR);
                lblStatus.setText("Status: Deletion failed.");
            }
        }
    }

    @FXML
    void courseTableOnMouseClick(MouseEvent event) {
        // This method can be used to handle clicks, e.g., to populate a form for updating, but since the FXML has no form, we'll keep it as is.
        lblStatus.setText("Status: Course selected.");
    }

    // Hover effects for buttons
    @FXML
    void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.getId().equals("addBtn")) {
            button.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("updateBtn")) {
            button.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ffb74d; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("deleteBtn")) {
            button.setStyle("-fx-background-color: #ff8a8a; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ff8a8a; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("btnSearch")) {
            button.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 16;");
        }
    }

    @FXML
    void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.getId().equals("addBtn")) {
            button.setStyle("-fx-background-color: #2e4d7d; -fx-text-fill: #a6c4ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("updateBtn")) {
            button.setStyle("-fx-background-color: #2e4d7d; -fx-text-fill: #ffb74d; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ffb74d; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("deleteBtn")) {
            button.setStyle("-fx-background-color: #2e4d7d; -fx-text-fill: #ff8a8a; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ff8a8a; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("btnSearch")) {
            button.setStyle("-fx-background-color: #2e4d7d; -fx-text-fill: #a6c4ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 16;");
        }
    }
}