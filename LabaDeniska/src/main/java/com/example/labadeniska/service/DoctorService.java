package com.example.labadeniska.service;

import com.example.labadeniska.dao.DoctorDao;
import com.example.labadeniska.models.Doctor;

import javax.print.Doc;
import java.util.List;

import java.util.List;

/**
 * Сервис для управления докторами.
 */
public class DoctorService {

    private final DoctorDao doctorDao;

    /**
     * Конструктор для инициализации сервиса докторов.
     *
     * @param doctorDao DAO для работы с данными докторов.
     */
    public DoctorService(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    /**
     * Получает всех докторов.
     *
     * @return Список всех докторов.
     */
    public List<Doctor> getAll() {
        return doctorDao.findAll();
    }

    /**
     * Находит доктора по его идентификатору.
     *
     * @param id Идентификатор доктора.
     * @return Доктор с указанным идентификатором.
     */
    public Doctor findDoctor(long id) {
        return doctorDao.findByID(id);
    }

    /**
     * Находит всех докторов с указанным ФИО.
     *
     * @param fio ФИО доктора.
     * @return Список докторов с указанным ФИО.
     */
    public List<Doctor> findAllByFIO(String fio) {
        return doctorDao.findByFIO(fio);
    }

    /**
     * Создает нового доктора.
     *
     * @param fio ФИО доктора.
     * @param speciality Специальность доктора.
     * @return Созданный доктор.
     * @throws IllegalArgumentException Если параметры недопустимы.
     */
    public Doctor createDoctor(String fio, String speciality) {
        if (fio != null && speciality != null) {
            Doctor doctor = new Doctor(0L, fio, speciality);
            this.doctorDao.save(doctor);

            return doctor;
        } else {
            throw new IllegalArgumentException("Недопустимые параметры для создания доктора");
        }
    }

    /**
     * Обновляет данные доктора.
     *
     * @param doctor Доктор с обновленными данными.
     * @return Обновленный доктор.
     */
    public Doctor updateDoctor(Doctor doctor) {
        return this.doctorDao.update(doctor);
    }

    /**
     * Удаляет доктора.
     *
     * @param doctor Доктор для удаления.
     */
    public void deleteDoctor(Doctor doctor) {
        this.doctorDao.deleteById(doctor.getId());
    }
}

