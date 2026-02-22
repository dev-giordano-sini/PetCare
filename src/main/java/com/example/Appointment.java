package com.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private AppointmentType type;
    private LocalDate date;
    private LocalTime time;
    private String note;

    public Appointment(AppointmentType type, LocalDate date, LocalTime time, String note) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public AppointmentType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "type=" + type +
                ", date=" + date +
                ", time=" + time +
                ", note='" + note + '\'' +
                '}';
    }
}




