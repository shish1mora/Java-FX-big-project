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

public class PatientDao implements Dao<Patient, Long> {

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

    public List<Patient> findByLastName(String lastName){
        List<Patient> patients = null;
        ResultSet rs = null;

        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findAllPatientByLastName"))){
            statement.setString(1, lastName);
            rs = statement.executeQuery();
            patients = mapper(rs);
            logger.debug("Выполнен запрос поиска по ФИО");
        }catch (SQLException e) {
            logger.error("Ошибка поиска по ID");
            System.out.println(e.getMessage());
        }
        return patients;
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.getAllPatients"))) {
            rs = statement.executeQuery();
            patients = mapper(rs);
            logger.debug("Выполнен запрос поиска по ID");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID");
            System.out.println(e.getMessage());
        }
        return patients;
    }

    @Override
    public Patient save(Patient patient) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.createPatient"),new String[] {"id"})) {
//            statement.setLong(1, patient.getId());
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getDateOfBirth().toString());
            statement.setString(5, patient.getPassSeries());
            statement.setString(6, patient.getPassNumber());


            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                long patientId = rs.getLong("id");
                patient.setId(patientId);
            }
            logger.debug("Запрос на сохранение данных был выполнен");

        } catch (SQLException e) {
            logger.error("Запрос на сохранение данных был не выполнен");
            System.out.println("Ошибка обновления Driver save: " + e.getMessage());
        }
        //TODO обработать ошибку запроса
        return patient;
    }

    @Override
    public Patient update(Patient patient) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updatePatient"), Statement.RETURN_GENERATED_KEYS)) { //update
//            statement.setLong(1, patient.getId());
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
            System.out.println("Ошибка обновления Driver update: " + e.getMessage());
            //TODO обработать ошибку запроса
        }
        return patient;
    }

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
            System.out.println(e.getMessage());
            //TODO обработать ошибку ResultSet
        }
        return list;
    }
}
