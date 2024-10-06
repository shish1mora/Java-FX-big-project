package com.example.labadeniska.service;

import com.example.labadeniska.dao.PatientDao;
import com.example.labadeniska.models.Doctor;
import com.example.labadeniska.models.Patient;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class PatientService {

    private final PatientDao patientDao;

    public PatientService(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    public List<Patient> getAll() {
        return patientDao.findAll();
    }

    public Patient findPatient(long id) {
        return patientDao.findByID(id);
    }

    public Patient createPatient(String firstName, String lastName, String patronymic, LocalDate dateOfBirth, String passSeries, String passNumber) {
        if (firstName != null && lastName != null && dateOfBirth != null && passSeries != null && passNumber != null) {
            Patient patient = new Patient(0L, firstName, lastName, patronymic, dateOfBirth, passSeries, passNumber);
            this.patientDao.save(patient);

            return patient;
        } else {
            throw new IllegalArgumentException("Недопустимые параметры для создания доктора");

        }

    }

    public List<Patient> findAllByLastName(String lastName){
        return patientDao.findByLastName(lastName);
    }

    public void deletePatient(Patient patient) {
        this.patientDao.deleteById(patient.getId());
    }

    public Patient updatePatient(Patient patient) {
        return this.patientDao.update(patient);
    }
}