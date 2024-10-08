package com.example.labadeniska.dao;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.models.Appointment;
import com.example.labadeniska.models.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * DAO для работы с данными записей на прием.
 */
public class AppointmentDao implements Dao<Appointment, Long> {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    static Properties property = new Properties();

    {
        logger.debug("Получены ресурсы");
        try (InputStream input = this.getClass().getResourceAsStream("/statements.properties")) {
            property.load(input);
        } catch (IOException e) {
            logger.error("Ошибка загрузки ресурсов");
        }
    }

    /**
     * Находит запись на прием по её идентификатору.
     *
     * @param id Идентификатор записи на прием.
     * @return Запись на прием с указанным идентификатором.
     */
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

    /**
     * Получает все записи на прием.
     *
     * @return Список всех записей на прием.
     */
    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.getAllAppointments"))) {
            rs = statement.executeQuery();
            appointments = mapper(rs);
            logger.debug("Выполнен запрос поиска всех записей на прием");
        } catch (SQLException e) {
            logger.error("Ошибка поиска всех записей на прием");
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    /**
     * Сохраняет новую запись на прием.
     *
     * @param appointment Запись на прием для сохранения.
     * @return Сохраненная запись на прием.
     */
    @Override
    public Appointment save(Appointment appointment) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.createAppointment"), new String[]{"id"})) {
            statement.setLong(1, appointment.getDoctorId());
            statement.setLong(2, appointment.getPatientId());
            statement.setString(3, appointment.getDateTime().toString());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long appointmentId = rs.getLong("id");
                appointment.setId(appointmentId);
            }
            logger.debug("Запрос на сохранение данных был выполнен");

        } catch (SQLException e) {
            logger.error("Запрос на сохранение данных был не выполнен");
            System.out.println("Ошибка обновления Driver save: " + e.getMessage());
        }
        return appointment;
    }

    /**
     * Обновляет данные записи на прием.
     *
     * @param appointment Запись на прием с обновленными данными.
     * @return Обновленная запись на прием.
     */
    @Override
    public Appointment update(Appointment appointment) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updateAppointment"), Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, appointment.getDoctorId());
            statement.setLong(2, appointment.getPatientId());
            statement.setString(3, appointment.getDateTime().toString());
            statement.setLong(4, appointment.getId());

            statement.executeUpdate();
            logger.debug("Запрос на обновление данных был выполнен");
        } catch (SQLException e) {
            logger.error("Запрос на обновление данных был не выполнен");
            System.out.println("Ошибка обновления Driver update: " + e.getMessage());
        }
        return appointment;
    }

    /**
     * Удаляет запись на прием по её идентификатору.
     *
     * @param id Идентификатор записи на прием для удаления.
     */
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

    /**
     * Преобразует ResultSet в список записей на прием.
     *
     * @param rs ResultSet с данными записей на прием.
     * @return Список записей на прием.
     */
    private List<Appointment> mapper(ResultSet rs) {
        List<Appointment> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(new Appointment(
                        rs.getLong("id"),
                        rs.getLong("doctorId"),
                        rs.getLong("patientId"),
                        LocalDateTime.parse(rs.getString("dateTime"))
                ));
            }
        } catch (SQLException e) {
            logger.error("Ошибка преобразования ResultSet в список записей на прием");
            System.out.println(e.getMessage());
        }
        return list;
    }
}


