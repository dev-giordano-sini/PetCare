package com.example;


public enum AppointmentType {
    VET_VISIT ("Veterinary visit"),
    VACCINATION ("Pet vaccination"),
    GROOMING ("Pet grooming");

    private String descrption;

    AppointmentType(String descrption) {
        this.descrption = descrption;
    }

    public String getDescrption() {
        return descrption;
    }
}
