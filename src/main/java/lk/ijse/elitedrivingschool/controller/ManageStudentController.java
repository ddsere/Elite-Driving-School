package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.StudentBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.StudentBoImpl;
import lk.ijse.elitedrivingschool.bo.custom.StudentBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.StudentBoImpl;
import lk.ijse.elitedrivingschool.dto.StudentDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageStudentController implements Initializable {

    @FXML
    private TableView<StudentDto> tblStudents;

    @FXML
    private TableColumn<StudentDto, Integer> colStudentId;

    @FXML
    private TableColumn<StudentDto, String> colName;

    @FXML
    private TableColumn<StudentDto, String> colAddress;

    @FXML
    private TableColumn<StudentDto, String> colContact;

    @FXML
    private TableColumn<StudentDto, Date> colRegDate;

    @FXML
    private TextField txtSearch;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTotalStudents;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSearch;

    private final lk.ijse.elitedrivingschool.bo.custom.StudentBo studentBo = new lk.ijse.elitedrivingschool.bo.custom.impl.StudentBoImpl();
    private final ObservableList<StudentDto> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up cell value factories for the table columns
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        loadStudents();
        updateStatusBar();

        // Add a listener to change the selected row's style
        tblStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (oldSelection != null) {
                // Clear the style from the previously selected row
                tblStudents.lookupAll(".table-row-cell").forEach(node -> {
                    if (node instanceof TableRow) {
                        TableRow<StudentDto> row = (TableRow<StudentDto>) node;
                        if (row.getItem() == oldSelection) {
                            row.setStyle(""); // Reset to default
                        }
                    }
                });
            }
            if (newSelection != null) {
                // Apply the new style to the selected row for better visibility
                tblStudents.lookupAll(".table-row-cell").forEach(node -> {
                    if (node instanceof TableRow) {
                        TableRow<StudentDto> row = (TableRow<StudentDto>) node;
                        if (row.getItem() == newSelection) {
                            row.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #1a2a43;");
                        }
                    }
                });
            }
        });
    }

    private void loadStudents() {
        studentList.clear();
        List<StudentDto> students = studentBo.getAllStudents();
        studentList.addAll(students);
        tblStudents.setItems(studentList);
        updateStatusBar();
    }

    private void updateStatusBar() {
        lblTotalStudents.setText("Total Students: " + studentList.size());
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student/RegisterStudent.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Register New Student");
            stage.setScene(new Scene(root));

            RegisterStudentController registerController = loader.getController();
            registerController.setManageStudentController(this);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load the registration form.").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        StudentDto selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a student to update.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student/UpdateStudent.fxml"));
            Parent root = loader.load();

            UpdateStudentController updateController = loader.getController();
            updateController.setManageStudentController(this);
            updateController.initData(selectedStudent);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Student Details");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load the update form.").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        StudentDto selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a student to delete.").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this student?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                studentBo.deleteStudent(selectedStudent.getStudentId());
                new Alert(Alert.AlertType.INFORMATION, "Student deleted successfully!").show();
                loadStudents();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Failed to delete student: " + e.getMessage()).show();
            }
        }
    }

    public void refreshTable() {
        loadStudents();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadStudents();
            lblStatus.setText("Status: Showing all students");
            return;
        }

        ObservableList<StudentDto> filteredList = FXCollections.observableArrayList();
        for (StudentDto student : studentList) {
            if (String.valueOf(student.getStudentId()).contains(searchText) ||
                    student.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    student.getContactNumber().contains(searchText)) {
                filteredList.add(student);
            }
        }

        tblStudents.setItems(filteredList);
        lblStatus.setText("Status: Found " + filteredList.size() + " matching students");
    }

    // Hover effect methods
    @FXML
    public void handleMouseEnter(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        String id = button.getId();

        if (id.equals("btnRegister") || id.equals("btnSearch")) {
            button.setStyle("-fx-background-color: #a6c4ff; " +
                    "-fx-text-fill: #1a2a43; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-radius: 8; " +
                    "-fx-border-color: #a6c4ff; " +
                    "-fx-border-width: 1; " +
                    "-fx-font-weight: bold; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-size: 16; " +
                    "-fx-effect: dropshadow(gaussian, rgba(166, 196, 255, 0.6), 15, 0, 0, 0);");
        } else if (id.equals("btnUpdate")) {
            button.setStyle("-fx-background-color: #ffb74d; " +
                    "-fx-text-fill: #1a2a43; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-radius: 8; " +
                    "-fx-border-color: #ffb74d; " +
                    "-fx-border-width: 1; " +
                    "-fx-font-weight: bold; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-size: 16; " +
                    "-fx-effect: dropshadow(gaussian, rgba(255, 183, 77, 0.6), 15, 0, 0, 0);");
        } else if (id.equals("btnDelete")) {
            button.setStyle("-fx-background-color: #ff8a8a; " +
                    "-fx-text-fill: #1a2a43; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-radius: 8; " +
                    "-fx-border-color: #ff8a8a; " +
                    "-fx-border-width: 1; " +
                    "-fx-font-weight: bold; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-size: 16; " +
                    "-fx-effect: dropshadow(gaussian, rgba(255, 138, 138, 0.6), 15, 0, 0, 0);");
        }

        button.setScaleX(1.05);
        button.setScaleY(1.05);
    }

    @FXML
    public void handleMouseExit(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        String id = button.getId();

        if (id.equals("btnRegister") || id.equals("btnSearch")) {
            button.setStyle("-fx-background-color: #2e4d7d; " +
                    "-fx-text-fill: #a6c4ff; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-radius: 8; " +
                    "-fx-border-color: #a6c4ff; " +
                    "-fx-border-width: 1; " +
                    "-fx-font-weight: bold; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-size: 16;");
        } else if (id.equals("btnUpdate")) {
            button.setStyle("-fx-background-color: #2e4d7d; " +
                    "-fx-text-fill: #ffb74d; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-radius: 8; " +
                    "-fx-border-color: #ffb74d; " +
                    "-fx-border-width: 1; " +
                    "-fx-font-weight: bold; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-size: 16;");
        } else if (id.equals("btnDelete")) {
            button.setStyle("-fx-background-color: #2e4d7d; " +
                    "-fx-text-fill: #ff8a8a; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-radius: 8; " +
                    "-fx-border-color: #ff8a8a; " +
                    "-fx-border-width: 1; " +
                    "-fx-font-weight: bold; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-size: 16;");
        }

        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }
}