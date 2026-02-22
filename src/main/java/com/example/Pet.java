package com.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Pet {
    private final String ID;
    private final String name;
    private final String breed;
    private String ownerName;
    private String contactInfo;
    private final LocalDate registrationDate;
    private final List<Appointment> appointments = new ArrayList<>();

    public Pet(String ID, String name, String breed, String ownerName, String contactInfo, LocalDate registrationDate) {
        this.ID = ID;
        this.name = name;
        this.breed = breed;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.registrationDate = registrationDate;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public String getShortInfo() {
        return "Pet{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Pet{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", registrationDate=" + registrationDate +
                ", appointments=" + appointments +
                '}';
    }
}

