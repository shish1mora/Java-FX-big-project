package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.dao.DoctorDao;
import com.example.labadeniska.dao.PatientDao;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.models.Patient;
import com.example.labadeniska.service.PatientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class PatientController {

    @FXML
    private TableView<Patient> patientsTable;

    @FXML
    private TableColumn<Patient, String> columnPatientFirstName;

    @FXML
    private TableColumn<Patient, String> columnPatientLastName;

    @FXML
    private TableColumn<Patient, String> columnPatientPatronymic;

    @FXML
    private TableColumn<Patient, String> columnPatientBirthDate;

    @FXML
    private TableColumn<Patient, String> columnPatientPassSeries;

    @FXML
    private TableColumn<Patient, String> columnPatientPassNumber;

    @FXML
    private Label Text;

    @FXML
    private TextField searchField;
    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    private PatientService patientService = new PatientService(new PatientDao());

    private Stage dialogStage;

    private PatientDao patientDao;

    private ObservableList<Patient> patients = FXCollections.observableArrayList();

    public PatientController() {
        patientDao = new PatientDao();
    }

    @FXML
    protected void initialize() {
        patients.addAll(this.patientService.getAll());

        columnPatientFirstName.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFirstName()));
        columnPatientLastName.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getLastName()));
        columnPatientPatronymic.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getPatronymic()));
        columnPatientBirthDate.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDateOfBirth().toString()));
        columnPatientPassSeries.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getPassSeries()));
        columnPatientPassNumber.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getPassNumber()));

        patientsTable.setItems(patients);
        patientsTable.getSortOrder().add(columnPatientLastName);

        logger.info("Таблица врачей заполнена и отсортирована по фио доктора");
    }

    static Properties property = new Properties();

    {
        try (InputStream input = this.getClass().getResourceAsStream("/statements.properties")) {
            property.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    @FXML
    void onAdd(ActionEvent event) {
        logger.info("Была нажата кнопка onAdd");
        Patient patient = new Patient();

        try {
            showDialog(patient);
            String patientLastName = patient.getLastName();
            String patientFirstName = patient.getFirstName();
            String patientPatronymic = patient.getPatronymic();
            LocalDate patientBirthDate = patient.getDateOfBirth();
            String patientPassSeries = patient.getPassSeries();
            String patientPassNumber = patient.getPassNumber();

            logger.info("Диалоговое окно - показано");

            patients.add(patient);

            this.patientService.createPatient(patientFirstName, patientLastName, patientPatronymic, patientBirthDate, patientPassSeries, patientPassNumber);
            patientsTable.sort();

            logger.info("Пациент добавлен");
        } catch (NumberFormatException var9) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка!");
            alert.setContentText("Пожалуйста, проверьте введенные данные и попробуйте снова.");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void onDelete(ActionEvent event) {
        logger.info("Была нажата кнопка onDelete");
        Patient patient = (Patient) this.patientsTable.getSelectionModel().getSelectedItem();
        if (patient != null) {
            this.patientService.deletePatient(patient);
            this.patientsTable.getItems().remove(patient);
            this.patientsTable.refresh();

            logger.info("Данные были удалены");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка!");
            alert.setContentText("Пожалуйста, выберите водителя из таблицы для удаления.");
            alert.showAndWait();
        }
    }


    @FXML
    void onEdit(ActionEvent event) {
        logger.info("Была нажата кнопка onEdit");
        Patient selectedPatient = (Patient) this.patientsTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка!");
            alert.setContentText("Пожалуйста, выберите водителя для редактирования.");
            alert.showAndWait();
        } else {
            try {
                logger.info("Диалоговое окно было открыто");
                showDialog(selectedPatient);
                logger.info("Диалоговое окно было закрыто");

                this.patientService.updatePatient(selectedPatient);
                this.patientsTable.refresh();

                logger.debug("Данные обновлены");
            } catch (NumberFormatException var9) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка!");
                alert.setContentText("Пожалуйста, проверьте введенные данные и попробуйте снова.");
                alert.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onExit(ActionEvent event) {
        logger.info("Была нажата кнопка onExit");
        dialogStage.close();
        logger.info("Окно закрыто");
    }

    @FXML
    void onSearch(ActionEvent event) {
        logger.info("Была нажата кнопка onSearch");
        String fiopatient = searchField.getText();
        if (!fiopatient.isEmpty()) {
            try {
                List<Patient> list = this.patientService.findAllByLastName(fiopatient);
                patientsTable.getItems().clear();
                patientsTable.getItems().addAll(list);

                logger.debug("Данные выведены в таблицу");
            } catch (Exception e) {
                System.out.println(e.getMessage());

                logger.debug("Ошибка вывода данных в таблицу");
            }
        }
    }

    private void showDialog(Patient patient) throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle(property.getProperty("lg.languages"), Locale.getDefault());

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("add-patient.fxml"), bundle);
        Parent root = loader.load();

        Stage addStage = new Stage();
        addStage.setTitle("Информация о врачах");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(MainApplication.getMainStage());
        Scene scene = new Scene(root);
        addStage.setScene(scene);

        AddPatientController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setPatient(patient);

        addStage.showAndWait();
    }
}