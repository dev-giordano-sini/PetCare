package com.example;

public enum PetCareServiceType {
    REGISTER_A_PET ("Register a pet"),
    SCHEDULE_AN_APPOINTMENT ("Schedule an appointment"),
    STORE_THE_DETAILS_IN_A_FILE ("Store the details in a file"),
    DISPLAY_DETAILS_OF_PETS_OR_APPOINTMENTS ("Display details of pets and/or appointments"),
    GENERATE_REPORTS ("Generate reports"),
    EXIT ("Exit");

    private final String description;

    PetCareServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
