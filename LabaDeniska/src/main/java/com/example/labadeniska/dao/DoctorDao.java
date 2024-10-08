package com.example.labadeniska.dao;

import com.example.labadeniska.MainApplication;
import com.example.labadeniska.models.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * DAO для работы с данными докторов.
 */
public class DoctorDao implements Dao<Doctor, Long> {

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
     * Находит доктора по его идентификатору.
     *
     * @param id Идентификатор доктора.
     * @return Доктор с указанным идентификатором.
     */
    @Override
    public Doctor findByID(long id) {
        Doctor doctor = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findDoctorById"))) {
            statement.setLong(1, id);
            rs = statement.executeQuery();
            doctor = mapper(rs).getFirst();
            logger.debug("Выполнен запрос поиска по ID");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID");
            System.out.println(e.getMessage());
        }
        return doctor;
    }

    /**
     * Получает всех докторов.
     *
     * @return Список всех докторов.
     */
    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.getAllDoctors"))) {
            rs = statement.executeQuery();
            doctors = mapper(rs);
            logger.debug("Выполнен запрос поиска всех докторов");
        } catch (SQLException e) {
            logger.error("Ошибка поиска всех докторов");
            System.out.println(e.getMessage());
        }
        return doctors;
    }

    /**
     * Находит всех докторов с указанным ФИО.
     *
     * @param fio ФИО доктора.
     * @return Список докторов с указанным ФИО.
     */
    public List<Doctor> findByFIO(String fio) {
        List<Doctor> doctors = null;
        ResultSet rs = null;

        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findAllByFIO"))) {
            statement.setString(1, fio);
            rs = statement.executeQuery();
            doctors = mapper(rs);
            logger.debug("Выполнен запрос поиска по ФИО");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ФИО");
            System.out.println(e.getMessage());
        }
        return doctors;
    }

    /**
     * Сохраняет нового доктора.
     *
     * @param doctor Доктор для сохранения.
     * @return Сохраненный доктор.
     */
    @Override
    public Doctor save(Doctor doctor) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.createDoctor"), new String[]{"id"})) {
            statement.setString(1, doctor.getFio());
            statement.setString(2, doctor.getSpeciality());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long doctorId = rs.getLong("id");
                doctor.setId(doctorId);
            }
            logger.debug("Запрос на сохранение данных был выполнен");

        } catch (SQLException e) {
            logger.error("Запрос на сохранение данных был не выполнен");
            System.out.println("Ошибка обновления Driver save: " + e.getMessage());
        }
        return doctor;
    }

    /**
     * Обновляет данные доктора.
     *
     * @param doctor Доктор с обновленными данными.
     * @return Обновленный доктор.
     */
    @Override
    public Doctor update(Doctor doctor) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updateDoctor"), Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, doctor.getFio());
            statement.setString(2, doctor.getSpeciality());
            statement.setLong(3, doctor.getId());

            statement.executeUpdate();
            logger.debug("Запрос на обновление данных был выполнен");
        } catch (SQLException e) {
            logger.error("Запрос на обновление данных был не выполнен");
            System.out.println("Ошибка обновления Driver update: " + e.getMessage());
        }
        return doctor;
    }

    /**
     * Удаляет доктора по его идентификатору.
     *
     * @param id Идентификатор доктора для удаления.
     */
    @Override
    public void deleteById(long id) {
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.deleteDoctor"))) {
            statement.setLong(1, id);
            statement.executeUpdate();
            logger.debug("Запрос на удаление данных по ID был выполнен");
        } catch (SQLException e) {
            logger.error("Запрос на удаление данных был не выполнен");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Преобразует ResultSet в список докторов.
     *
     * @param rs ResultSet с данными докторов.
     * @return Список докторов.
     */
    private List<Doctor> mapper(ResultSet rs) {
        List<Doctor> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(new Doctor(
                        rs.getLong("id"),
                        rs.getString("fio"),
                        rs.getString("speciality")
                ));
            }
        } catch (SQLException e) {
            logger.error("Ошибка преобразования ResultSet в список докторов");
            System.out.println(e.getMessage());
        }
        return list;
    }
}
