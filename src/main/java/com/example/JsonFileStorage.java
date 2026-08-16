package com.example;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public <T> List<T> readList(String filename, Class<T> elementType) throws IOException {
        Path path = Path.of(filename);
        if (Files.notExists(path) || Files.size(path) == 0) {
            return new ArrayList<>();
        }

        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
        return objectMapper.readValue(path.toFile(), listType);
    }

    public void writeList(String filename, List<?> values) throws IOException {
        objectMapper.writeValue(Path.of(filename).toFile(), values);
    }
}
