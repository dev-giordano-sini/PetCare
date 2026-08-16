package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentWithID extends Appointment {
    protected String ID;

    @JsonCreator
    public AppointmentWithID(@JsonProperty("id") String ID,
                             @JsonProperty("type") AppointmentType type,
                             @JsonProperty("date") LocalDate date,
                             @JsonProperty("time") LocalTime time,
                             @JsonProperty("note") String note) {
        super(type, date, time, note);
        this.ID = ID;
    }

    @JsonProperty("id")
    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "AppointmentWithID{" +
                "ID='" + ID + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", time=" + time +
                ", note='" + note + '\'' +
                '}';
    }
}
