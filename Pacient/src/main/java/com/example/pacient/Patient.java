package com.example.pacient;


import java.time.LocalDate;
import java.time.Period;

public class Patient {
    private String lastName;
    private String firstName;
    private String middleName;
    private String nationality;
    private double height;
    private double weight;
    private LocalDate birthDate;
    private String medicalCardNumber;

    public Patient(String lastName, String firstName, String middleName, String nationality, double height, double weight, LocalDate birthDate, String medicalCardNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.nationality = nationality;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.medicalCardNumber = medicalCardNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getNationality() {
        return nationality;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getMedicalCardNumber() {
        return medicalCardNumber;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
