module lk.ijse.elitedrivingschool {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.elitedrivingschool to javafx.fxml;
    exports lk.ijse.elitedrivingschool;
}