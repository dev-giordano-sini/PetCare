package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void reportsTheRecordAndFieldWhenARequiredValueIsMissingOrNull() throws Exception {
        Path file = tempDir.resolve("pets.txt");
        Files.writeString(file, """
                [
                  {"id": "pet-1", "name": null}
                ]
                """);

        IOException error = assertThrows(IOException.class,
                () -> storage.readList(file.toString(), Pet.class, "id", "name", "breed"));

        assertTrue(error.getMessage().contains("record[0]"));
        assertTrue(error.getMessage().contains("field 'name'"));
        assertTrue(error.getMessage().contains("cannot be null"));

        Files.writeString(file, "[{\"name\": \"Bob\"}]");
        IOException missingError = assertThrows(IOException.class,
                () -> storage.readList(file.toString(), Pet.class, "id", "name"));
        assertTrue(missingError.getMessage().contains("record[0]"));
        assertTrue(missingError.getMessage().contains("field 'id'"));
        assertTrue(missingError.getMessage().contains("is missing"));
    }

    @Test
    void restoresTheFirstFileWhenReplacingTheSecondFileFails() throws Exception {
        Path first = tempDir.resolve("pets.txt");
        Path invalidSecondTarget = tempDir.resolve("appointments.txt");
        Files.writeString(first, "original pets");
        Files.createDirectory(invalidSecondTarget);
        Files.writeString(invalidSecondTarget.resolve("keep.txt"), "prevents directory replacement");

        assertThrows(IOException.class, () -> storage.writeListsAtomically(
                first.toString(), List.of("new pets"),
                invalidSecondTarget.toString(), List.of("new appointments")));

        assertEquals("original pets", Files.readString(first));
        assertTrue(Files.isDirectory(invalidSecondTarget));
    }
}
