package com.example.labadeniska.service;

import com.example.labadeniska.dao.PatientDao;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.models.Patient;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;


/**
 * Сервис для управления пациентами.
 */
public class PatientService {

    private final PatientDao patientDao;

    /**
     * Конструктор для инициализации сервиса пациентов.
     *
     * @param patientDao DAO для работы с данными пациентов.
     */
    public PatientService(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    /**
     * Получает всех пациентов.
     *
     * @return Список всех пациентов.
     */
    public List<Patient> getAll() {
        return patientDao.findAll();
    }

    /**
     * Находит пациента по его идентификатору.
     *
     * @param id Идентификатор пациента.
     * @return Пациент с указанным идентификатором.
     */
    public Patient findPatient(long id) {
        return patientDao.findByID(id);
    }

    /**
     * Создает нового пациента.
     *
     * @param firstName Имя пациента.
     * @param lastName Фамилия пациента.
     * @param patronymic Отчество пациента.
     * @param dateOfBirth Дата рождения пациента.
     * @param passSeries Серия паспорта пациента.
     * @param passNumber Номер паспорта пациента.
     * @return Созданный пациент.
     * @throws IllegalArgumentException Если параметры недопустимы.
     */
    public Patient createPatient(String firstName, String lastName, String patronymic, LocalDate dateOfBirth, String passSeries, String passNumber) {
        if (firstName != null && lastName != null && dateOfBirth != null && passSeries != null && passNumber != null) {
            Patient patient = new Patient(0L, firstName, lastName, patronymic, dateOfBirth, passSeries, passNumber);
            this.patientDao.save(patient);

            return patient;
        } else {
            throw new IllegalArgumentException("Недопустимые параметры для создания пациента");
        }
    }

    /**
     * Находит всех пациентов с указанной фамилией.
     *
     * @param lastName Фамилия пациента.
     * @return Список пациентов с указанной фамилией.
     */
    public List<Patient> findAllByLastName(String lastName) {
        return patientDao.findByLastName(lastName);
    }

    /**
     * Удаляет пациента.
     *
     * @param patient Пациент для удаления.
     */
    public void deletePatient(Patient patient) {
        this.patientDao.deleteById(patient.getId());
    }

    /**
     * Обновляет данные пациента.
     *
     * @param patient Пациент с обновленными данными.
     * @return Обновленный пациент.
     */
    public Patient updatePatient(Patient patient) {
        return this.patientDao.update(patient);
    }
}
