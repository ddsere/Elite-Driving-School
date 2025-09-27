package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.StudentBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.StudentBoImpl;
import lk.ijse.elitedrivingschool.dto.StudentDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.ZoneId;

public class UpdateStudentController {

    @FXML
    private TextField txtRegistrationNo;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtAddress;

    @FXML
    private DatePicker dpRegistrationDate;

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnUpdate;

    private ManageStudentController manageStudentController;
    private final StudentBo studentBo = new StudentBoImpl();
    private StudentDto studentToUpdate;

    public void setManageStudentController(ManageStudentController manageStudentController) {
        this.manageStudentController = manageStudentController;
    }

    public void initData(StudentDto student) {
        this.studentToUpdate = student;
        // Split the full name into first and last name
        String[] nameParts = student.getName().split(" ", 2);
        txtFirstName.setText(nameParts.length > 0 ? nameParts[0] : "");
        txtLastName.setText(nameParts.length > 1 ? nameParts[1] : "");

        txtRegistrationNo.setText(String.valueOf(student.getStudentId()));
        txtContact.setText(student.getContactNumber());
        txtAddress.setText(student.getAddress());

        if (student.getRegistrationDate() != null) {
            dpRegistrationDate.setValue(student.getRegistrationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    @FXML
    void updateStudent(ActionEvent event) {
        if (studentToUpdate == null) {
            new Alert(Alert.AlertType.ERROR, "No student data to update.").show();
            return;
        }

        // Basic validation
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").show();
            return;
        }

        // Update the DTO with new values
        studentToUpdate.setName(txtFirstName.getText() + " " + txtLastName.getText());
        studentToUpdate.setAddress(txtAddress.getText());
        studentToUpdate.setContactNumber(txtContact.getText());

        try {
            studentBo.updateStudent(studentToUpdate);
            new Alert(Alert.AlertType.INFORMATION, "Student updated successfully!").show();
            if (manageStudentController != null) {
                manageStudentController.refreshTable();
            }
            // Close the update window
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update student: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    // Hover effects for buttons
    @FXML
    void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        String id = button.getId();

        if (id.equals("btnUpdate")) {
            button.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (id.equals("btnCancel")) {
            button.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #1a2a43; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ffb74d; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        }
        button.setScaleX(1.05);
        button.setScaleY(1.05);
    }

    @FXML
    void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        String id = button.getId();

        if (id.equals("btnUpdate")) {
            button.setStyle("-fx-background-color: #1a2a43; -fx-text-fill: #a6c4ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        } else if (id.equals("btnCancel")) {
            button.setStyle("-fx-background-color: #1a2a43; -fx-text-fill: #d4e1f7; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #4b6a9a; -fx-border-width: 1; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        }
        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }
}