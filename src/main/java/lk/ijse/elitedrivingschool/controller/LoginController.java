package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.UserBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.UserBoImpl;
import lk.ijse.elitedrivingschool.dto.UserDto;
import lk.ijse.elitedrivingschool.exception.InvalidCredentialsException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField TxtPassword;

    @FXML
    private Button btnButton;

    private final UserBo userBo = new UserBoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Receptionist"));
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String role = cmbRole.getValue();
        String username = txtUserName.getText();
        String password = TxtPassword.getText();

        if (role == null || username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required.").show();
            return;
        }

        UserDto userDto = new UserDto(username, password, role);

        try {
            UserDto loggedInUser = userBo.login(userDto);
            new Alert(Alert.AlertType.INFORMATION, "Login Successful!").show();
            navigateToDashboard(loggedInUser.getRole());

        } catch (InvalidCredentialsException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void navigateToDashboard(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBoardView.fxml"));
            Parent root = loader.load();

            // Get the controller for the dashboard and pass the role
            DashBoardController dashBoardController = loader.getController();
            dashBoardController.setLoggedInUserRole(role); // Pass the role to control access

            Scene scene = new Scene(root);
            Stage stage = (Stage) btnButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Elite Driving School - Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load the dashboard.").show();
        }
    }

    public void handleMouseEnter(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #64ffda; " +
                "-fx-text-fill: #0a192f; " +
                "-fx-background-radius: 6; " +
                "-fx-border-radius: 6; " +
                "-fx-border-color: #64ffda; " +
                "-fx-border-width: 1; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-font-size: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(100, 255, 218, 0.6), 15, 0, 0, 0);");
        button.setScaleX(1.05);
        button.setScaleY(1.05);
    }

    public void handleMouseExit(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #0a192f; " +
                "-fx-text-fill: #64ffda; " +
                "-fx-background-radius: 6; " +
                "-fx-border-radius: 6; " +
                "-fx-border-color: #64ffda; " +
                "-fx-border-width: 1; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-font-size: 16;");
        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }
}