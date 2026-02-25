package com.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentWithID extends Appointment {
    protected String ID;

    public AppointmentWithID(String ID, AppointmentType type, LocalDate date, LocalTime time, String note) {
        super(type, date, time, note);
        this.ID = ID;
    }

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

    public String getInfoToStore() {
        return  "ID=\"" + ID + "\","
                + "type=\"" + type + "\","
                + "date=\"" + date + "\","
                + "time=\"" + time + "\","
                +  "note=\"" + note + "\"";
    }
}
