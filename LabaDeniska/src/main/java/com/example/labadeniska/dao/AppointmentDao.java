package com.example.labadeniska.dao;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.models.Appointment;
import com.example.labadeniska.models.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AppointmentDao implements Dao<Appointment, Long> {

    private static final Logger logger =
            LoggerFactory.getLogger(MainApplication.class);


    static Properties property = new Properties();

    {
        logger.debug("Получены ресурсы");
        try (InputStream input = this.getClass().getResourceAsStream("/statements.properties")) {
            property.load(input);
        } catch (IOException e) {
            logger.error("Ошибка загрузки ресурсов");
        }
    }

    @Override
    public Appointment findByID(long id) {
        Appointment appointment = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findAppointmentById"))) {
            statement.setLong(1, id);
            rs = statement.executeQuery();
            appointment = mapper(rs).getFirst();
            logger.debug("Выполнен запрос поиска по ID");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID");
            System.out.println(e.getMessage());
        }
        return appointment;
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.getAllAppointments"))) {
            rs = statement.executeQuery();
            appointments = mapper(rs);
            logger.debug("Выполнен запрос поиска по ID");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID");
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    @Override
    public Appointment save(Appointment appointment) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.createAppointment"),new String[] {"id"})) {
            statement.setLong(1, appointment.getId());
            statement.setLong(2, appointment.getDoctorId());
            statement.setLong(3, appointment.getPatientId());
            statement.setTimestamp(5, Timestamp.valueOf(appointment.getDateTime().toString()));


            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                long appointmentId = rs.getLong("id");
                appointment.setId(appointmentId);
            }
            logger.debug("Запрос на сохранение данных был выполнен");

        } catch (SQLException e) {
            logger.error("Запрос на сохранение данных был не выполнен");
            System.out.println("Ошибка обновления Driver save: " + e.getMessage());
        }
        //TODO обработать ошибку запроса
        return appointment;
    }

    @Override
    public Appointment update(Appointment appointment) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updateAppointment"), Statement.RETURN_GENERATED_KEYS)) { //update
            statement.setLong(1, appointment.getId());
            statement.setLong(2, appointment.getDoctorId());
            statement.setLong(3, appointment.getPatientId());
            statement.setTimestamp(5, Timestamp.valueOf(appointment.getDateTime().toString()));


            statement.executeUpdate();
            logger.debug("Запрос на обновление данных был выполнен");
        } catch (SQLException e) {
            System.out.println("Ошибка обновления Driver update: " + e.getMessage());
            //TODO обработать ошибку запроса
        }
        return appointment;
    }

    @Override
    public void deleteById(long id) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.deleteAppointment"))) {
            statement.setLong(1, id);
            statement.executeUpdate();
            logger.debug("Запрос на удаление данных по ID был выполнен");
        } catch (SQLException e) {
            logger.error("Запрос на удаление данных был не выполнен");
            System.out.println(e.getMessage());
        }
    }

    private List<Appointment> mapper(ResultSet rs) {
        List<Appointment> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(new Appointment(
                        rs.getLong("id"),
                        rs.getLong("doctorId"),
                        rs.getLong("patientId"),
                        rs.getTimestamp("dateTime").toLocalDateTime()


                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //TODO обработать ошибку ResultSet
        }
        return list;
    }
}


