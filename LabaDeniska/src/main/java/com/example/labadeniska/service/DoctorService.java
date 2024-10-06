package com.example.labadeniska.service;

import com.example.labadeniska.dao.DoctorDao;
import com.example.labadeniska.models.Doctor;

import javax.print.Doc;
import java.util.List;

public class DoctorService {

    private final DoctorDao doctorDao;

    public DoctorService(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    public List<Doctor> getAll() {
        return doctorDao.findAll();
    }

    public Doctor findDoctor(long id) {
        return doctorDao.findByID(id);
    }

    public List<Doctor> findAllByFIO(String fio){
        return doctorDao.findByFIO(fio);
    }

    public Doctor createDoctor(String fio, String speciality) {
        if (fio != null && speciality != null) {
            Doctor doctor = new Doctor(0L, fio, speciality);
            this.doctorDao.save(doctor);

            return doctor;
        } else {
            throw new IllegalArgumentException("Недопустимые параметры для создания доктора");

        }

    }

    public Doctor updateDoctor(Doctor doctor) {
        return this.doctorDao.update(doctor);
    }

    public void deleteDoctor(Doctor doctor) {
        this.doctorDao.deleteById(doctor.getId());
    }
}
