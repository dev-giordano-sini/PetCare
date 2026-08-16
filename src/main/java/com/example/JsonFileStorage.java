package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class JsonFileStorage {
    private final ObjectMapper objectMapper;

    public JsonFileStorage() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public <T> List<T> readList(String filename, Class<T> elementType, String... requiredFields) throws IOException {
        Path path = Path.of(filename);
        if (Files.notExists(path) || Files.size(path) == 0) {
            return new ArrayList<>();
        }

        JsonNode root = objectMapper.readTree(path.toFile());
        if (!root.isArray()) {
            throw new IOException("Invalid JSON in " + filename + ": expected an array");
        }

        List<T> values = new ArrayList<>();
        for (int index = 0; index < root.size(); index++) {
            JsonNode record = root.get(index);
            validateRecord(filename, index, record, requiredFields);
            try {
                values.add(objectMapper.treeToValue(record, elementType));
            } catch (IOException | IllegalArgumentException e) {
                throw new IOException("Invalid JSON in " + filename + " at record[" + index + "]: "
                        + e.getMessage(), e);
            }
        }
        return values;
    }

    public void writeList(String filename, List<?> values) throws IOException {
        objectMapper.writeValue(Path.of(filename).toFile(), values);
    }

    public void writeListsAtomically(String firstFilename, List<?> firstValues,
                                     String secondFilename, List<?> secondValues) throws IOException {
        Path first = Path.of(firstFilename).toAbsolutePath();
        Path second = Path.of(secondFilename).toAbsolutePath();
        Path firstTemporary = createTemporarySibling(first, ".new");
        Path secondTemporary = createTemporarySibling(second, ".new");
        Path firstBackup = null;
        Path secondBackup = null;
        boolean firstExisted = Files.exists(first);
        boolean secondExisted = Files.exists(second);

        try {
            objectMapper.writeValue(firstTemporary.toFile(), firstValues);
            objectMapper.writeValue(secondTemporary.toFile(), secondValues);
            if (firstExisted) {
                firstBackup = createTemporarySibling(first, ".backup");
                Files.copy(first, firstBackup, StandardCopyOption.REPLACE_EXISTING);
            }
            if (secondExisted) {
                secondBackup = createTemporarySibling(second, ".backup");
                Files.copy(second, secondBackup, StandardCopyOption.REPLACE_EXISTING);
            }

            replace(firstTemporary, first);
            try {
                replace(secondTemporary, second);
            } catch (IOException e) {
                restore(first, firstBackup, firstExisted);
                restore(second, secondBackup, secondExisted);
                throw e;
            }
        } finally {
            Files.deleteIfExists(firstTemporary);
            Files.deleteIfExists(secondTemporary);
            if (firstBackup != null) {
                Files.deleteIfExists(firstBackup);
            }
            if (secondBackup != null) {
                Files.deleteIfExists(secondBackup);
            }
        }
    }

    private void validateRecord(String filename, int index, JsonNode record, String[] requiredFields)
            throws IOException {
        if (!record.isObject()) {
            throw new IOException("Invalid JSON in " + filename + " at record[" + index
                    + "]: expected an object");
        }
        for (String field : requiredFields) {
            if (!record.has(field)) {
                throw invalidField(filename, index, field, "is missing");
            }
            if (record.get(field).isNull()) {
                throw invalidField(filename, index, field, "cannot be null");
            }
        }
    }

    private IOException invalidField(String filename, int index, String field, String problem) {
        return new IOException("Invalid JSON in " + filename + " at record[" + index + "] field '"
                + field + "': " + problem);
    }

    private Path createTemporarySibling(Path target, String suffix) throws IOException {
        return Files.createTempFile(target.getParent(), target.getFileName().toString(), suffix);
    }

    private void replace(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void restore(Path target, Path backup, boolean existed) throws IOException {
        if (existed) {
            replace(backup, target);
        } else {
            Files.deleteIfExists(target);
        }
    }
}
