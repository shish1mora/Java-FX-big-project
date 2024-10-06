package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddPatientController {

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_ok;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfPatronymic;

    @FXML
    private DatePicker dpDateOfBirth;

    @FXML
    private TextField tfPassSeries;

    @FXML
    private TextField tfPassNumber;


    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    private Stage dialogStage;
    private Patient patient;

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        tfLastName.setText(patient.getLastName());
        tfFirstName.setText(patient.getFirstName());
        tfPatronymic.setText(patient.getPatronymic());
        dpDateOfBirth.setValue(patient.getDateOfBirth());
        tfPassSeries.setText(patient.getPassSeries());
        tfPassNumber.setText(patient.getPassNumber());

        logger.debug("Данные выгружены");
    }

    @FXML
    void onOk(ActionEvent event) {
        logger.info("Была нажата кнопка onOk");
        patient.setLastName(tfLastName.getText());
        patient.setFirstName(tfFirstName.getText());
        patient.setPatronymic(tfPatronymic.getText());
        patient.setDateOfBirth(dpDateOfBirth.getValue());
        patient.setPassSeries(tfPassSeries.getText());
        patient.setPassNumber(tfPassNumber.getText());

        dialogStage.close();
        logger.debug("Данные получены, окно закрыто");
    }

    /**
     * Метод закрытия диалогового окна
     */
    @FXML
    void onCancel(ActionEvent event) {
        logger.info("Была нажата кнопка onCancel");
        dialogStage.close(); logger.info("Закрыто окно");
    }

}
