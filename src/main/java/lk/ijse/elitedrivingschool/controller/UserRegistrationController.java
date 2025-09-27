package lk.ijse.elitedrivingschool.controller;

import lk.ijse.elitedrivingschool.bo.custom.UserBo;
import lk.ijse.elitedrivingschool.bo.custom.impl.UserBoImpl;
import lk.ijse.elitedrivingschool.dto.UserDto;
import lk.ijse.elitedrivingschool.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserRegistrationController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private TableView<User> userTbl;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TableColumn<User, String> passwordCol;

    @FXML
    private TableColumn<User, String> roleCol;

    private final UserBo userBo = new UserBoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the role combo box
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Receptionist"));
        loadAllUsers();
        setupTableSelection();
    }

    private void loadAllUsers() {
        // Clear existing data
        userTbl.getItems().clear();

        // Get all users from the database
        List<UserDto> userList = userBo.getAllUsers();
        if (userList != null && !userList.isEmpty()) {
            ObservableList<User> users = FXCollections.observableArrayList();

            // Convert UserDto to User for the TableView
            for (UserDto dto : userList) {
                users.add(new User(dto.getUsername(), dto.getPassword(), dto.getRole()));
            }

            // Set cell value factories
            usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
            passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
            roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

            // Add data to the table
            userTbl.setItems(users);
        } else {
            // Handle the case where no users are returned
            System.out.println("No users found in the database.");
        }
    }

    private void setupTableSelection() {
        userTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate form fields with selected user data
                txtUsername.setText(newSelection.getUsername());
                cmbRole.setValue(newSelection.getRole());

                // Disable username field for updates and deletes
                txtUsername.setDisable(true);
            }
        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String role = cmbRole.getValue();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields to add a user.").show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match. Please re-enter.").show();
            return;
        }

        UserDto userDto = new UserDto(username, password, role);

        try {
            userBo.registerUser(userDto);
            new Alert(Alert.AlertType.INFORMATION, "User added successfully!").show();
            clearFields();
            loadAllUsers(); // Refresh the table
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error adding user: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String role = cmbRole.getValue();

        // Check if a user is selected
        if (username.isEmpty() || role == null) {
            new Alert(Alert.AlertType.ERROR, "Please select a user to update and provide a role.").show();
            return;
        }

        // Check if password needs to be changed
        String newPassword = txtPassword.getText();
        String newConfirmPassword = txtConfirmPassword.getText();

        if (newPassword.isEmpty()) {
            // Only update the role
            UserDto userDto = new UserDto(username, null, role);
            userBo.updateUser(userDto);
            new Alert(Alert.AlertType.INFORMATION, "User role updated successfully!").show();
        } else {
            // Update role and password
            if (!newPassword.equals(newConfirmPassword)) {
                new Alert(Alert.AlertType.ERROR, "New passwords do not match.").show();
                return;
            }
            // Pass the new password to the BO for hashing
            userBo.changePassword(username, newPassword, role);
            new Alert(Alert.AlertType.INFORMATION, "User updated successfully!").show();
        }

        clearFields();
        loadAllUsers(); // Refresh the table
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String username = txtUsername.getText();

        if (username.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select a user to delete.").show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait();

        if (confirmAlert.getResult() == ButtonType.YES) {
            userBo.deleteUser(username);
            new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!").show();
            clearFields();
            loadAllUsers(); // Refresh the table
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void userTblOnMouseClicked(MouseEvent event) {
        // This is handled by the listener in initialize()
    }

    // Mouse hover effects (same as before)
    @FXML
    void handleMouseEntered(MouseEvent event) {
        Button sourceButton = (Button) event.getSource();
        if (sourceButton.equals(btnAdd)) {
            sourceButton.setStyle("-fx-background-color: #64ffda; -fx-text-fill: #112240; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #64ffda; -fx-border-width: 1; -fx-cursor: hand;");
        } else if (sourceButton.equals(btnUpdate)) {
            sourceButton.setStyle("-fx-background-color: #a6c4ff; -fx-text-fill: #112240; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-cursor: hand;");
        } else if (sourceButton.equals(btnDelete)) {
            sourceButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: #112240; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #e74c3c; -fx-border-width: 1; -fx-cursor: hand;");
        } else if (sourceButton.equals(btnClear)) {
            sourceButton.setStyle("-fx-background-color: #8892b0; -fx-text-fill: #112240; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #8892b0; -fx-border-width: 1; -fx-cursor: hand;");
        }
    }

    @FXML
    void handleMouseExited(MouseEvent event) {
        Button sourceButton = (Button) event.getSource();
        if (sourceButton.equals(btnAdd)) {
            sourceButton.setStyle("-fx-background-color: #112240; -fx-text-fill: #64ffda; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #64ffda; -fx-border-width: 1; -fx-cursor: hand;");
        } else if (sourceButton.equals(btnUpdate)) {
            sourceButton.setStyle("-fx-background-color: #112240; -fx-text-fill: #a6c4ff; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #a6c4ff; -fx-border-width: 1; -fx-cursor: hand;");
        } else if (sourceButton.equals(btnDelete)) {
            sourceButton.setStyle("-fx-background-color: #112240; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #e74c3c; -fx-border-width: 1; -fx-cursor: hand;");
        } else if (sourceButton.equals(btnClear)) {
            sourceButton.setStyle("-fx-background-color: #112240; -fx-text-fill: #8892b0; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #8892b0; -fx-border-width: 1; -fx-cursor: hand;");
        }
    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
        txtUsername.setDisable(false); // Enable username field again
        userTbl.getSelectionModel().clearSelection(); // Clear table selection
    }
}