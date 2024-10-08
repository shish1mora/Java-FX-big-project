package com.example.pacient;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleIntegerProperty;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class PatientAppController implements Initializable {

    @FXML
    private TableView<Patient> tableView;

    @FXML
    private TableColumn<Patient, String> lastNameCol;

    @FXML
    private TableColumn<Patient, String> firstNameCol;

    @FXML
    private TableColumn<Patient, String> middleNameCol;

    @FXML
    private TableColumn<Patient, String> nationalityCol;

    @FXML
    private TableColumn<Patient, Double> heightCol;

    @FXML
    private TableColumn<Patient, Double> weightCol;

    @FXML
    private TableColumn<Patient, LocalDate> birthDateCol;

    @FXML
    private TableColumn<Patient, String> medicalCardNumberCol;

    @FXML
    private TableColumn<Patient, Integer> ageCol;

    private ObservableList<Patient> patients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        patients = FXCollections.observableArrayList();
        patients.add(new Patient("Karpov", "Denis", "Ivanovich", "Russian", 180, 75, LocalDate.of(1990, 1, 1), "123456"));
        patients.add(new Patient("Petrov", "Sergey", "Petrovich", "Russian", 175, 70, LocalDate.of(1985, 2, 2), "234567"));
        patients.add(new Patient("Sidorov", "Sidor", "Sidorovich", "Russian", 170, 65, LocalDate.of(1980, 3, 3), "345678"));
        patients.add(new Patient("Smirnov", "Sergey", "Sergeevich", "Russian", 165, 60, LocalDate.of(1975, 4, 4), "456789"));
        patients.add(new Patient("Kuznetsov", "Kirill", "Kirillovich", "Russian", 160, 55, LocalDate.of(1970, 5, 5), "567890"));
        patients.add(new Patient("Popov", "Pavel", "Pavlovich", "Russian", 155, 50, LocalDate.of(1965, 6, 6), "678901"));
        patients.add(new Patient("Vasiliev", "Vasiliy", "Vasilievich", "Russian", 150, 45, LocalDate.of(1960, 7, 7), "789012"));
        patients.add(new Patient("Sokolov", "Semen", "Semenovich", "Russian", 145, 40, LocalDate.of(1955, 8, 8), "890123"));
        patients.add(new Patient("Mikhailov", "Mikhail", "Mikhailovich", "Russian", 140, 35, LocalDate.of(1950, 9, 9), "901234"));
        patients.add(new Patient("Fedorov", "Fedora", "Fedorovich", "Russian", 135, 30, LocalDate.of(1945, 10, 10), "012345"));


        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        nationalityCol.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        heightCol.setCellValueFactory(new PropertyValueFactory<>("height"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        birthDateCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        medicalCardNumberCol.setCellValueFactory(new PropertyValueFactory<>("medicalCardNumber"));
        ageCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());


        tableView.setItems(patients);
    }
}
