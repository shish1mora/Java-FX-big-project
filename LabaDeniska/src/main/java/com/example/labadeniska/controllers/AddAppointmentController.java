package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.dao.DoctorDao;
import com.example.labadeniska.dao.PatientDao;
import com.example.labadeniska.models.Appointment;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.models.Patient;
import com.example.labadeniska.service.DoctorService;
import com.example.labadeniska.service.PatientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddAppointmentController {
    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_ok;

    @FXML
    private TableView<Doctor> doctorsTable;

    @FXML
    private TableColumn<Doctor, String> columnDoctorFIO;

    @FXML
    private TableView<Patient> patientsTable;

    @FXML
    private TableColumn<Patient, String> columnPatientFIO;

    @FXML
    private DatePicker dpAppointmentDate;


    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    private Stage dialogStage;
    private Appointment appointment;

    private final DoctorService doctorService = new DoctorService(new DoctorDao());
    private PatientService patientService = new PatientService(new PatientDao());

    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<Patient> patients = FXCollections.observableArrayList();

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public void setAppointment(Appointment appointment) {
        doctors.addAll(doctorService.getAll());
        patients.addAll(patientService.getAll());
        columnDoctorFIO.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFio()));
        columnPatientFIO.setCellValueFactory(item ->
                new SimpleStringProperty(item.getValue().getLastName() + " " + item.getValue().getFirstName().charAt(0) + ". " + item.getValue().getPatronymic().charAt(0) + ".")
        );

        doctorsTable.setItems(doctors);
        doctorsTable.getSortOrder().add(columnDoctorFIO);
        patientsTable.setItems(patients);
        patientsTable.getSortOrder().add(columnPatientFIO);

        this.appointment = appointment;

        int doctorIndex = doctors.indexOf(doctors.stream().filter(doc -> doc.getId() == appointment.getDoctorId()).findFirst().orElse(new Doctor()));
        int patientIndex = patients.indexOf(patients.stream().filter(pat -> pat.getId() == appointment.getPatientId()).findFirst().orElse(new Patient()));

        if (doctorIndex != -1) {
            doctorsTable.getSelectionModel().select(doctorIndex);
        }
        if (patientIndex != -1) {
            patientsTable.getSelectionModel().select(patientIndex);
        }
        dpAppointmentDate.setValue(appointment.getDateTime().toLocalDate());

        logger.debug("Данные выгружены");
    }

    @FXML
    void onOk(ActionEvent event) {
        logger.info("Была нажата кнопка onOk");

        Doctor doctor = (Doctor) this.doctorsTable.getSelectionModel().getSelectedItem();
        Patient patient = (Patient) this.patientsTable.getSelectionModel().getSelectedItem();

        if(doctor== null || patient == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка!");
            alert.setContentText("Пожалуйста, выберите запись из таблиц докторов и пациентов.");
            alert.showAndWait();
        }

        appointment.setDoctorId(doctor.getId());
        appointment.setPatientId(patient.getId());
        appointment.setDateTime(dpAppointmentDate.getValue().atStartOfDay());

        dialogStage.close();
        logger.debug("Данные получены, окно закрыто");
    }

    /**
     * Метод закрытия диалогового окна
     */
    @FXML
    void onCancel(ActionEvent event) {
        logger.info("Была нажата кнопка onCancel");
        dialogStage.close();
        logger.info("Закрыто окно");
    }
}
