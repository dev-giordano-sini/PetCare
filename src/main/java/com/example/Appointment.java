package com.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    protected AppointmentType type;
    protected LocalDate date;
    protected LocalTime time;
    protected String note;

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
        return "{" +
                "type=" + type +
                ", date=" + date +
                ", time=" + time +
                ", note='" + note + '\'' +
                '}';
    }

        public String getInfo() {
            return  "type=" + type +
                    ", date=" + date +
                    ", time=" + time +
                    ", note=" + note;
        }
}




