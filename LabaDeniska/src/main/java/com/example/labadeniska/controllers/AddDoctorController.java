package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.models.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddDoctorController {

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_ok;

    @FXML
    private TextField tfFioDoctor;

    @FXML
    private TextField tfSpeciality;


    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    private Stage dialogStage;
    private Doctor doctor;

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        tfFioDoctor.setText(doctor.getFio());
        tfSpeciality.setText(doctor.getSpeciality());

        logger.debug("Данные выгружены");
    }

    @FXML
    void onOk(ActionEvent event) {
        logger.info("Была нажата кнопка onOk");
        doctor.setFio(tfFioDoctor.getText());
        doctor.setSpeciality(tfSpeciality.getText());

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
