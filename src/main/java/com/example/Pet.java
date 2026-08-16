package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonCreator
    public Pet(@JsonProperty("id") String ID,
               @JsonProperty("name") String name,
               @JsonProperty("breed") String breed,
               @JsonProperty("ownerName") String ownerName,
               @JsonProperty("contactInfo") String contactInfo,
               @JsonProperty("registrationDate") LocalDate registrationDate) {
        this.ID = ID;
        this.name = name;
        this.breed = breed;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.registrationDate = registrationDate;
    }

    @JsonProperty("id")
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

    @JsonIgnore
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    @JsonIgnore
    public String getShortInfo() {
        return "ID=" + ID + '\n' +
                ", name=" + name + '\n' +
                ", breed=" + breed + '\n' +
                ", ownerName=" + ownerName + '\n' +
                ", contactInfo=" + contactInfo;
    }

    @Override
    public String toString() {
        return "{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", registrationDate=" + registrationDate.toString() +
                ", appointments=" + appointments +
                '}';
    }
}
