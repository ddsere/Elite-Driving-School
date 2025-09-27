package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.DashBoardBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.DashBoardBoImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private Label lbDateTime;

    @FXML
    private Label lblStudentCount;

    @FXML
    private Label lblCourseCount;

    @FXML
    private Label lblInstructorCount;

    @FXML
    private VBox vboxButtons;

    // FXML fields for the buttons we want to hide
    @FXML
    private Button btnManageUsers;
    @FXML
    private Button btnCourse;
    @FXML
    private Button btnLesson;
    @FXML
    private Button btnInstructor;

    // Existing FXML fields
    @FXML
    private Button btnStudentOnAction;
    @FXML
    private Button btnEnrollCourseOnAction;
    @FXML
    private Button btnPaymentDetailsOnAction;
    @FXML
    private Button btnCourseDetailsOnAction;
    @FXML
    private Button btnDashBoardOnAction;
    @FXML
    private Button btnLogOutOnAction;

    // New label to display user role
    @FXML
    private Label lblUserRole;

    private final DashBoardBo dashBoardBo = new DashBoardBoImpl();
    private String loggedInUserRole;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCounts();
        startClock();
    }

    public void setLoggedInUserRole(String role) {
        this.loggedInUserRole = role;
        if ("Receptionist".equals(role)) {
            hideAdminOptions();
        }
        // Set the user role label
        if (lblUserRole != null) {
            lblUserRole.setText(role);
        }
    }

    private void hideAdminOptions() {
        // These buttons are in the VBox, so remove them from the children list
        if (vboxButtons != null) {
            if (btnCourse != null) vboxButtons.getChildren().remove(btnCourse);
            if (btnLesson != null) vboxButtons.getChildren().remove(btnLesson);
            if (btnInstructor != null) vboxButtons.getChildren().remove(btnInstructor);
        }

        // The btnManageUsers is in the HBox, so hide it by setting visibility and managed properties
        if (btnManageUsers != null) {
            btnManageUsers.setVisible(false);
            btnManageUsers.setManaged(false); // This ensures it doesn't take up space in the layout
        }
    }

    private void loadCounts() {
        lblStudentCount.setText(String.valueOf(dashBoardBo.getStudentCount()));
        lblCourseCount.setText(String.valueOf(dashBoardBo.getCourseCount()));
        lblInstructorCount.setText(String.valueOf(dashBoardBo.getInstructorCount()));
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");
            lbDateTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    void btnDashBoardOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(true);
        ancMainContainer.getChildren().clear();
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/student/ManageStudent.fxml");
    }

    @FXML
    void btnEnrollCourseOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/CourseEnroll.fxml");
    }

    @FXML
    void btnCourseOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/ManageCourse.fxml");
    }

    @FXML
    void btnLessonOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/lesson.fxml");
    }

    @FXML
    void btnInstructorOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/instructor/ManageInstructor.fxml");
    }

    @FXML
    void btnPaymentDetailsOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/payment.fxml");
    }

    @FXML
    void btnCourseDetailsOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/CourseDetails.fxml");
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Logout Confirmation");
        confirmation.setHeaderText("Logging Out");
        confirmation.setContentText("Are you sure you want to log out?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/loginView/LoginController.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ancMainContainer.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Elite Driving School - Login");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load login page.").show();
            }
        }
    }

    @FXML
    void btnUserRegistrationOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/UserRegistration.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            ancMainContainer.getChildren().clear();
            ancMainContainer.getChildren().add(root);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load page: " + fxmlPath).show();
        }
    }
}