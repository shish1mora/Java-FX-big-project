package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Класс контроллера для основного окна приложения
 * Класс MainApplication, main-view.fxml
 */
public class MainController {

    @FXML
    private Label welcomeText;

    @FXML
    private Button bt_doctor;

    @FXML
    private Button bt_patient;

    @FXML
    private Button bt_appointment;

    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    static Properties property = new Properties();
    {
        try (InputStream input = this.getClass().getResourceAsStream("/statements.properties")) {
            property.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Обработчик события нажатия кнопки "Водитель"
     * Открывает новое окно с формой для работы с водителями
     *
     * @param event Событие нажатия кнопки
     */
    @FXML
    void onDoctor(ActionEvent event) {
        logger.info("Была нажата кнопка onDoctor");
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(property.getProperty("lg.languages"), Locale.getDefault());

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("doctor-view.fxml"), bundle);
            Parent root = loader.load();
            Stage addStage = new Stage();
            Scene scene = new Scene(root);
            addStage.setScene(scene);
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.initOwner(MainApplication.getMainStage());

            DoctorController controller = loader.getController();
            controller.setAddStage(addStage);

            addStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обработчик события нажатия кнопки "Маршруты"
     * Открывает новое окно с формой для работы с маршрутами
     *
     * @param event Событие нажатия кнопки
     */
    @FXML
    void onPatient(ActionEvent event) throws IOException {
        logger.info("Была нажата кнопка onRoute");
        try {
          //  Locale.setDefault(Locale.FRANCE);
            ResourceBundle bundle = ResourceBundle.getBundle(property.getProperty("lg.languages"), Locale.getDefault());

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("patient-view.fxml"), bundle);
            Parent root = loader.load();

            Stage addStage = new Stage();
            Scene scene = new Scene(root);
            addStage.setScene(scene);
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.initOwner(MainApplication.getMainStage());

            PatientController controller = loader.getController();
            controller.setAddStage(addStage);

            addStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обработчик события нажатия кнопки "Билеты"
     * Открывает форму взаимодействия с билетами
     *
     * @param event Событие нажатия кнопки
     */
    @FXML
    void onAppointment(ActionEvent event) {
        logger.info("Была нажата кнопка onTicket");
//        try {
//          //  Locale.setDefault(Locale.FRANCE);
//            ResourceBundle bundle = ResourceBundle.getBundle(property.getProperty("lg.languages"), Locale.getDefault());
//
//            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ticket-view.fxml"), bundle);
//            Parent root = loader.load();
//
//            Stage addStage = new Stage();
//            Scene scene = new Scene(root);
//            addStage.setScene(scene);
//            addStage.initModality(Modality.APPLICATION_MODAL);
//            addStage.initOwner(MainApplication.getMainStage());
//
//            TicketController controller = loader.getController();
//            controller.setAddStage(addStage);
//
//            addStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}