package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.StudentBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.StudentBoImpl;
import lk.ijse.elitedrivingschool.dto.StudentDto;
import lk.ijse.elitedrivingschool.exception.RegistrationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RegisterStudentController {

    @FXML
    private TextField txtRegNo;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtAddress;
    @FXML
    private DatePicker datePickerRegDate;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnReset;

    private ManageStudentController manageStudentController;
    private final StudentBo studentBo = new StudentBoImpl();

    public void setManageStudentController(ManageStudentController manageStudentController) {
        this.manageStudentController = manageStudentController;
    }

    @FXML
    public void initialize() {
        // No combobox to initialize
    }

    @FXML
    void registerStudent(ActionEvent event) {
        // Basic validation for all required fields
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtAddress.getText().isEmpty() ||
                txtContact.getText().isEmpty() || datePickerRegDate.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").show();
            return;
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setName(txtFirstName.getText() + " " + txtLastName.getText());
        studentDto.setAddress(txtAddress.getText());
        studentDto.setContactNumber(txtContact.getText());
        studentDto.setRegistrationDate(java.sql.Date.valueOf(datePickerRegDate.getValue()));

        try {
            studentBo.registerStudent(studentDto);
            new Alert(Alert.AlertType.INFORMATION, "Student registered successfully!").show();

            if (manageStudentController != null) {
                manageStudentController.refreshTable();
            }

            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close();

        } catch (RegistrationException e) {
            new Alert(Alert.AlertType.ERROR, "Registration failed: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtFirstName.clear();
        txtLastName.clear();
        txtContact.clear();
        txtAddress.clear();
        datePickerRegDate.setValue(null);
    }

    // Hover effects for buttons
    @FXML
    void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.getId().equals("btnRegister")) {
            button.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("btnReset")) {
            button.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ffb74d; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        }
        button.setScaleX(1.05);
        button.setScaleY(1.05);
    }

    @FXML
    void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.getId().equals("btnRegister")) {
            button.setStyle("-fx-background-color: #1a2a43; -fx-text-fill: #a6c4ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (button.getId().equals("btnReset")) {
            button.setStyle("-fx-background-color: #1a2a43; -fx-text-fill: #d4e1f7; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #4b6a9a; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        }
        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }
}