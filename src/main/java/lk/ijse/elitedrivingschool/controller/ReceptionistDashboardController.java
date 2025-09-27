package lk.ijse.elitedrivingschool.controller;

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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReceptionistDashboardController implements Initializable {

    @FXML
    private AnchorPane ancReceptionistDashboard;
    @FXML
    private Label lbDateTime;
    @FXML
    private Label lbUserRole; // Assuming you might want to display the role

    // Navigation Buttons
    @FXML
    private Button btnDashboardOnAction;
    @FXML
    private Button btnStudentOnAction;
    @FXML
    private Button btnEnrollCourseOnAction;
    @FXML
    private Button btnPaymentDetailsOnAction;
    @FXML
    private Button btnLogOutOnAction;

    @FXML
    private AnchorPane ancMainContainer; // To load content into

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startClock();
        // Optionally, set the user role display
        // lbUserRole.setText("Receptionist");
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            lbDateTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    // --- Navigation Actions ---

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        // Load default receptionist dashboard content or welcome message
        loadReceptionistDefaultView();
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) {
        loadPage("/view/student/ManageStudent.fxml"); // Assuming this view is accessible to Receptionists
    }

    @FXML
    void btnEnrollCourseOnAction(ActionEvent event) {
        loadPage("/view/course/CourseEnroll.fxml");
    }

    @FXML
    void btnPaymentDetailsOnAction(ActionEvent event) {
        loadPage("/view/course/payment.fxml"); // Assuming this view is accessible to Receptionists
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/loginView/LoginController.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ancReceptionistDashboard.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Elite Driving School - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load login page.").show();
        }
    }

    // --- Helper Methods ---

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

    private void loadReceptionistDefaultView() {
        try {
            // Load a default FXML that shows a welcome message or overview for receptionists
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/receptionist/ReceptionistDefaultView.fxml")); // Create this FXML
            Parent root = loader.load();
            ancMainContainer.getChildren().clear();
            ancMainContainer.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load default receptionist view.").show();
        }
    }
}