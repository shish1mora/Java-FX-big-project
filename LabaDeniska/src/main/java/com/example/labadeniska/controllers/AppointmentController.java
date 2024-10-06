package com.example.labadeniska.controllers;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.dao.AppointmentDao;
import com.example.labadeniska.dao.PatientDao;
import com.example.labadeniska.service.AppointmentService;
import com.example.labadeniska.service.PatientService;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppointmentController {

        @FXML
        private TableView<Appointment> appointmentsTable;

        @FXML
        private TableColumn<Appointment, String> columnDoctorId;

        @FXML
        private TableColumn<Appointment, String> columnPatientId;

        @FXML
        private TableColumn<Appointment, String> columnDateTime;

        @FXML
        private Label Text;

        @FXML
        private TextField searchField;
        private static final Logger logger =
                LoggerFactory.getLogger(MainApplication.class);

        private AppointmentService appointmentService = new AppointmentService(new AppointmentDao());

        private Stage dialogStage;

        private AppointmentDao appointmentDao;

        private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        public AppointmentController() {
            appointmentDao = new AppointmentDao();
        }

        @FXML
        protected void initialize() {
            appointments.addAll(this.appointmentService.getAll());

            columnDoctorId.setCellValueFactory(item -> new SimpleStringProperty(String.valueOf(item.getValue().getDoctorId())));
            columnPatientId.setCellValueFactory(item -> new SimpleStringProperty(String.valueOf(item.getValue().getPatientId())));
            columnDateTime.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDateTime().toString()));

            appointmentsTable.setItems(appointments);
            appointmentsTable.getSortOrder().add(columnDateTime);

            logger.info("Таблица записей заполнена и отсортирована по дате и времени");
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
                long doctorId = appointment.getDoctorId();
                long patientId = appointment.getPatientId();
                LocalDateTime dateTime = appointment.getDateTime();

                logger.info("Диалоговое окно - показано");

                appointments.add(appointment);

                this.appointmentService.createAppointment(doctorId, patientId, dateTime);
                appointmentsTable.sort();




                private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);

    private AppointmentService appointmentService= new AppointmentService(new AppointmentDao());


}
