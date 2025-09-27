module lk.ijse.elitedrivingschool {
    // JavaFX dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Hibernate and persistence dependencies
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    // Other dependencies
    requires jbcrypt;
    requires static lombok;
    requires java.desktop;
    requires modelmapper;

    opens lk.ijse.elitedrivingschool to javafx.fxml, org.hibernate.orm.core;
    opens lk.ijse.elitedrivingschool.entity to org.hibernate.orm.core, modelmapper, javafx.base;

    opens lk.ijse.elitedrivingschool.controller to javafx.fxml;
    opens lk.ijse.elitedrivingschool.dto to javafx.base;
    opens lk.ijse.elitedrivingschool.tm to javafx.base;

    exports lk.ijse.elitedrivingschool;
    exports lk.ijse.elitedrivingschool.controller;
    exports lk.ijse.elitedrivingschool.dto;
    exports lk.ijse.elitedrivingschool.bo;
    exports lk.ijse.elitedrivingschool.db;
    exports lk.ijse.elitedrivingschool.exception;
    exports lk.ijse.elitedrivingschool.entity;
    exports lk.ijse.elitedrivingschool.bo.custom;
    exports lk.ijse.elitedrivingschool.bo.custom.impl;
}