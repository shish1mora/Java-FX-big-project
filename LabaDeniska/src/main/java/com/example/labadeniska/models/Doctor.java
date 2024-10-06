package com.example.labadeniska.models;

public class Doctor {

    long id;

    String fio;

    String speciality;

    public Doctor(long id, String fio, String speciality) {
        this.id = id;
        this.fio = fio;
        this.speciality = speciality;
    }

    public Doctor() {
        this(0, "", "");
    }

    public long getId() {
        return this.id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getFio() {
        return this.fio;
    }

    public void setFio(String value) {
        this.fio = value;
    }

    public String getSpeciality() {
        return this.speciality;
    }

    public void setSpeciality(String value) {
        this.speciality = value;
    }
}
