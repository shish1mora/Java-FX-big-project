package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.dao.AppointmentDao;
import com.example.labadeniska.dao.DoctorDao;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.service.DoctorService;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class DoctorController {

    @FXML
    private TableView<Doctor> doctorsTable;

    @FXML
    private TableColumn<Doctor, String> columnDoctorFIO;

    @FXML
    private TableColumn<Doctor, String> columnDoctorSpeciality;

    @FXML
    private Label Text;

    @FXML
    private TextField searchField;


    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    private DoctorService doctorService = new DoctorService(new DoctorDao());

    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();

    private DoctorDao doctorDao;

    private Stage dialogStage;

    public DoctorController() {
        doctorDao = new DoctorDao();
    }

    @FXML
    protected void initialize(){
        doctors.addAll(this.doctorService.getAll());

        columnDoctorFIO.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFio()));
        columnDoctorSpeciality.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getSpeciality()));

        doctorsTable.setItems(doctors);
        doctorsTable.getSortOrder().add(columnDoctorFIO);

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
        Doctor doctor = new Doctor();

        try {
            showDialog(doctor);
            String doctorFio = doctor.getFio();
            String doctorSpec = doctor.getSpeciality();

            logger.info("Диалоговое окно - показано");

            doctors.add(doctor);

            this.doctorService.createDoctor(doctorFio, doctorSpec);
            doctorsTable.sort();

            logger.info("Доктор добавлен");
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
        Doctor doctor = (Doctor) this.doctorsTable.getSelectionModel().getSelectedItem();
        if (doctor != null) {
            this.doctorService.deleteDoctor(doctor);
            this.doctorsTable.getItems().remove(doctor);
            this.doctorsTable.refresh();

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
        Doctor selectedDoctor = (Doctor)this.doctorsTable.getSelectionModel().getSelectedItem();
        if (selectedDoctor == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка!");
            alert.setContentText("Пожалуйста, выберите водителя для редактирования.");
            alert.showAndWait();
        } else {
            try {
                logger.info("Диалоговое окно было открыто");
                showDialog(selectedDoctor);
                logger.info("Диалоговое окно было закрыто");

                this.doctorService.updateDoctor(selectedDoctor);
                this.doctorsTable.refresh();

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
        dialogStage.close(); logger.info("Окно закрыто");
    }

    @FXML
    void onSearch(ActionEvent event) {
        logger.info("Была нажата кнопка onSearch");
        String fiodoctor = searchField.getText();
        if (!fiodoctor.isEmpty()) {
            try {
                List<Doctor> list = this.doctorService.findAllByFIO(fiodoctor);
                doctorsTable.getItems().clear();
                doctorsTable.getItems().addAll(list);

                logger.debug("Данные выведены в таблицу");
            } catch (Exception e) {
                System.out.println(e.getMessage());

                logger.debug("Ошибка вывода данных в таблицу");
            }
        }
    }

    private void showDialog(Doctor doctor) throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle(property.getProperty("lg.languages"), Locale.getDefault());

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("add-doctor.fxml"), bundle);
        Parent root = loader.load();

        Stage addStage = new Stage();
        addStage.setTitle("Информация о врачах");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(MainApplication.getMainStage());
        Scene scene = new Scene(root);
        addStage.setScene(scene);

        AddDoctorController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setDoctor(doctor);

        addStage.showAndWait();
    }
}
