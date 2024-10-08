package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.dao.AppointmentDao;
import com.example.labadeniska.dao.DoctorDao;
import com.example.labadeniska.dao.PatientDao;
import com.example.labadeniska.models.Appointment;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.models.Patient;
import com.example.labadeniska.service.AppointmentService;
import com.example.labadeniska.service.DoctorService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class AppointmentController {

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, String> columnDoctorFIO;

    @FXML
    private TableColumn<Appointment, String> columnPatientFIO;

    @FXML
    private TableColumn<Appointment, String> columnAppointmentDate;

    @FXML
    private Label Text;

    @FXML
    private TextField searchField;


    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private AppointmentDao appointmentDao;

    private DoctorService doctorService = new DoctorService(new DoctorDao());

    private PatientService patientService = new PatientService(new PatientDao());

    private AppointmentService appointmentService = new AppointmentService(new AppointmentDao());

    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    private Stage dialogStage;

    public AppointmentController() {

        doctorDao = new DoctorDao();
        patientDao = new PatientDao();
        appointmentDao = new AppointmentDao();
    }

    @FXML
    protected void initialize() {
        doctors.addAll(this.doctorService.getAll());
        patients.addAll(this.patientService.getAll());
        appointments.addAll(this.appointmentService.getAll());

        columnDoctorFIO.setCellValueFactory(item ->
                new SimpleStringProperty(
                        doctors.stream()
                                .filter(doctor -> doctor.getId() == item.getValue().getDoctorId())
                                .findFirst()
                                .orElse(new Doctor())
                                .getFio()
                )
        );
        columnPatientFIO.setCellValueFactory(item ->
                new SimpleStringProperty(
                        patients.stream()
                                .filter(patient -> patient.getId() == item.getValue().getPatientId())
                                .findFirst()
                                .map(patient -> patient.getLastName() + " " + patient.getFirstName().charAt(0) + ". " + patient.getPatronymic().charAt(0) + ".")
                                .orElse("")
                )
        );
        columnAppointmentDate.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDateTime().toString()));

        appointmentsTable.setItems(appointments);
        appointmentsTable.getSortOrder().add(columnDoctorFIO);

        logger.info("Таблица записей заполнена и отсортирована по фио доктора");
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
        Appointment appointment = new Appointment();

        try {
            showDialog(appointment);
            Long doctorId = appointment.getDoctorId();
            Long patientId = appointment.getPatientId();
            LocalDateTime appointmentDate = appointment.getDateTime();

            logger.info("Диалоговое окно - показано");

            appointments.add(appointment);

            this.appointmentService.createAppointment(doctorId, patientId, appointmentDate);
            appointmentsTable.sort();

            logger.info("Запись добавлена");
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
        Appointment appointment = (Appointment) this.appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            this.appointmentService.deleteAppointment(appointment);
            this.appointmentsTable.getItems().remove(appointment);
            this.appointmentsTable.refresh();

            logger.info("Данные были удалены");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка!");
            alert.setContentText("Пожалуйста, выберите запись из таблицы для удаления.");
            alert.showAndWait();
        }
    }


    @FXML
    void onEdit(ActionEvent event) {
        logger.info("Была нажата кнопка onEdit");
        Appointment selectedAppointment = (Appointment) this.appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка!");
            alert.setContentText("Пожалуйста, выберите запись для редактирования.");
            alert.showAndWait();
        } else {
            try {
                logger.info("Диалоговое окно было открыто");
                showDialog(selectedAppointment);
                logger.info("Диалоговое окно было закрыто");

                this.appointmentService.updateAppointment(selectedAppointment);
                this.appointmentsTable.refresh();

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

    private void showDialog(Appointment appointment) throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle(property.getProperty("lg.languages"), Locale.getDefault());

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("add-appointment.fxml"), bundle);
        Parent root = loader.load();

        Stage addStage = new Stage();
        addStage.setTitle("Информация о врачах");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(MainApplication.getMainStage());
        Scene scene = new Scene(root);
        addStage.setScene(scene);

        AddAppointmentController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setAppointment(appointment);

        addStage.showAndWait();
    }
}
