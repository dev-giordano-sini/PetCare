package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class JsonFileStorageTest {
    @TempDir
    Path tempDir;

    private final JsonFileStorage storage = new JsonFileStorage();

    @Test
    void storesAndLoadsPetsAsAJsonArray() throws Exception {
        Path file = tempDir.resolve("pets.txt");
        Pet pet = new Pet("pet-1", "Bob", "Yorkshire", "Mario", "555-0100",
                LocalDate.of(2025, 1, 1));

        storage.writeList(file.toString(), List.of(pet));
        List<Pet> loadedPets = storage.readList(file.toString(), Pet.class);

        assertEquals(1, loadedPets.size());
        assertEquals("pet-1", loadedPets.getFirst().getID());
        assertEquals(LocalDate.of(2025, 1, 1), loadedPets.getFirst().getRegistrationDate());
        assertFalse(Files.readString(file).contains("shortInfo"));
    }

    @Test
    void storesAndLoadsAppointmentsWithJavaTimeValues() throws Exception {
        Path file = tempDir.resolve("appointments.txt");
        AppointmentWithID appointment = new AppointmentWithID("pet-1", AppointmentType.GROOMING,
                LocalDate.of(2026, 3, 10), LocalTime.of(11, 0), "cut hair, please");

        storage.writeList(file.toString(), List.of(appointment));
        List<AppointmentWithID> loaded = storage.readList(file.toString(), AppointmentWithID.class);

        assertEquals(1, loaded.size());
        assertEquals("cut hair, please", loaded.getFirst().getNote());
        assertEquals(LocalTime.of(11, 0), loaded.getFirst().getTime());
    }
}
