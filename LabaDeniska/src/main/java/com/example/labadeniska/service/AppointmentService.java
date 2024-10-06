package com.example.labadeniska.service;

import com.example.labadeniska.dao.AppointmentDao;
import com.example.labadeniska.models.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {
    private final AppointmentDao appointmentDao;

    public AppointmentService(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public List<Appointment> getAll() {
        return appointmentDao.findAll();
    }

    public Appointment findAppointment(long id) {
        return appointmentDao.findByID(id);
    }

    public Appointment createAppointment(long doctorId, long patientId, LocalDateTime dateTime) {
        if (doctorId != 0 && patientId != 0 && dateTime != null) {
            Appointment appointment = new Appointment(0L, doctorId, patientId, dateTime);
            this.appointmentDao.save(appointment);

            return appointment;
        } else {
            throw new IllegalArgumentException("Недопустимые параметры для создания записи");
        }
    }

    public Appointment updateAppointment(Appointment appointment) {
        return this.appointmentDao.update(appointment);
    }






}
