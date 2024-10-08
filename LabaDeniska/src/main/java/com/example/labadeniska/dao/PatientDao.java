package com.example.labadeniska.dao;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.models.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * DAO для работы с данными пациентов.
 */
public class PatientDao implements Dao<Patient, Long> {

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
     * Находит пациента по его идентификатору.
     *
     * @param id Идентификатор пациента.
     * @return Пациент с указанным идентификатором.
     */
    @Override
    public Patient findByID(long id) {
        Patient patient = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findPatientById"))) {
            statement.setLong(1, id);
            rs = statement.executeQuery();
            patient = mapper(rs).getFirst();
            logger.debug("Выполнен запрос поиска по ID");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID");
            System.out.println(e.getMessage());
        }
        return patient;
    }

    /**
     * Находит всех пациентов с указанной фамилией.
     *
     * @param lastName Фамилия пациента.
     * @return Список пациентов с указанной фамилией.
     */
    public List<Patient> findByLastName(String lastName) {
        List<Patient> patients = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findAllPatientByLastName"))) {
            statement.setString(1, lastName);
            rs = statement.executeQuery();
            patients = mapper(rs);
            logger.debug("Выполнен запрос поиска по ФИО");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ФИО");
            System.out.println(e.getMessage());
        }
        return patients;
    }

    /**
     * Получает всех пациентов.
     *
     * @return Список всех пациентов.
     */
    @Override
    public List<Patient> findAll() {
        List<Patient> patients = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.getAllPatients"))) {
            rs = statement.executeQuery();
            patients = mapper(rs);
            logger.debug("Выполнен запрос поиска всех пациентов");
        } catch (SQLException e) {
            logger.error("Ошибка поиска всех пациентов");
            System.out.println(e.getMessage());
        }
        return patients;
    }

    /**
     * Сохраняет нового пациента.
     *
     * @param patient Пациент для сохранения.
     * @return Сохраненный пациент.
     */
    @Override
    public Patient save(Patient patient) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.createPatient"), new String[]{"id"})) {
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getDateOfBirth().toString());
            statement.setString(5, patient.getPassSeries());
            statement.setString(6, patient.getPassNumber());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long patientId = rs.getLong("id");
                patient.setId(patientId);
            }
            logger.debug("Запрос на сохранение данных был выполнен");

        } catch (SQLException e) {
            logger.error("Запрос на сохранение данных был не выполнен");
            System.out.println("Ошибка обновления Driver save: " + e.getMessage());
        }
        return patient;
    }

    /**
     * Обновляет данные пациента.
     *
     * @param patient Пациент с обновленными данными.
     * @return Обновленный пациент.
     */
    @Override
    public Patient update(Patient patient) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updatePatient"), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getDateOfBirth().toString());
            statement.setString(5, patient.getPassSeries());
            statement.setString(6, patient.getPassNumber());
            statement.setLong(7, patient.getId());

            statement.executeUpdate();
            logger.debug("Запрос на обновление данных был выполнен");
        } catch (SQLException e) {
            logger.error("Запрос на обновление данных был не выполнен");
            System.out.println("Ошибка обновления Driver update: " + e.getMessage());
        }
        return patient;
    }

    /**
     * Удаляет пациента по его идентификатору.
     *
     * @param id Идентификатор пациента для удаления.
     */
    @Override
    public void deleteById(long id) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.deletePatient"))) {
            statement.setLong(1, id);
            statement.executeUpdate();
            logger.debug("Запрос на удаление данных по ID был выполнен");
        } catch (SQLException e) {
            logger.error("Запрос на удаление данных был не выполнен");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Преобразует ResultSet в список пациентов.
     *
     * @param rs ResultSet с данными пациентов.
     * @return Список пациентов.
     */
    private List<Patient> mapper(ResultSet rs) {
        List<Patient> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(new Patient(
                        rs.getLong("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("patronymic"),
                        LocalDate.parse(rs.getString("dateOfBirth")),
                        rs.getString("passSeries"),
                        rs.getString("passNumber")
                ));
            }
        } catch (SQLException e) {
            logger.error("Ошибка преобразования ResultSet в список пациентов");
            System.out.println(e.getMessage());
        }
        return list;
    }
}

