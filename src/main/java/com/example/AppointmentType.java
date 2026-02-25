package com.example;


public enum AppointmentType {
    VET_VISIT ("Veterinary visit"),
    VACCINATION ("Pet vaccination"),
    GROOMING ("Pet grooming");

    private String description;

    AppointmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
