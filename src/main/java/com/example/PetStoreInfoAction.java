package com.example;

public enum PetStoreInfoAction {
    DISPLAY_ALL_PETS ("All registered pets"),
    DISPLAY_ALL_APPOINTMENTS_SPECIFIC_PET("All appointments for a specific pet"),
    DISPLAY_UPCOMING_ALL_APPOINTMENTS("Upcoming appointments for all pets"),
    DISPLAY_PAST_APPOINTMENTS ("Past appointment history for each pet"),
    EXIT ("Exit");

    private final String description;

    PetStoreInfoAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
