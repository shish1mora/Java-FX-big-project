package com.example.labadeniska.service;

import com.example.labadeniska.dao.AppointmentDao;
import com.example.labadeniska.models.Appointment;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для управления записями на прием.
 */
public class AppointmentService {

    private final AppointmentDao appointmentDao;

    /**
     * Конструктор для инициализации сервиса записей на прием.
     *
     * @param appointmentDao DAO для работы с данными записей на прием.
     */
    public AppointmentService(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    /**
     * Получает все записи на прием.
     *
     * @return Список всех записей на прием.
     */
    public List<Appointment> getAll() {
        return appointmentDao.findAll();
    }

    /**
     * Находит запись на прием по её идентификатору.
     *
     * @param id Идентификатор записи на прием.
     * @return Запись на прием с указанным идентификатором.
     */
    public Appointment findAppointment(long id) {
        return appointmentDao.findByID(id);
    }

    /**
     * Создает новую запись на прием.
     *
     * @param doctorId Идентификатор доктора.
     * @param patientId Идентификатор пациента.
     * @param dateTime Дата и время записи на прием.
     * @return Созданная запись на прием.
     * @throws IllegalArgumentException Если параметры недопустимы.
     */
    public Appointment createAppointment(long doctorId, long patientId, LocalDateTime dateTime) {
        if (doctorId != 0 && patientId != 0 && dateTime != null) {
            Appointment appointment = new Appointment(0L, doctorId, patientId, dateTime);
            this.appointmentDao.save(appointment);

            return appointment;
        } else {
            throw new IllegalArgumentException("Недопустимые параметры для создания записи");
        }
    }

    /**
     * Обновляет данные записи на прием.
     *
     * @param appointment Запись на прием с обновленными данными.
     * @return Обновленная запись на прием.
     */
    public Appointment updateAppointment(Appointment appointment) {
        return this.appointmentDao.update(appointment);
    }

    /**
     * Удаляет запись на прием.
     *
     * @param appointment Запись на прием для удаления.
     */
    public void deleteAppointment(Appointment appointment) {
        this.appointmentDao.deleteById(appointment.getId());
    }
}

