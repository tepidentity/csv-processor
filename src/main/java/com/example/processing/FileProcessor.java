package com.example.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.ToString;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@ToString
public class FileProcessor implements Closeable {

    private final CSVReader reader;
    private final JSONLWriter writer;

    public FileProcessor(String source, String targetParent, String delimiter) throws IOException {

        File file = getSource(source);
        File target = getTarget(targetParent);

        reader = new CSVReader(file.toPath(), delimiter);
        writer = new JSONLWriter(target, objectMapper());
    }

    public void process() throws IOException {

        while (reader.canRead()) {

            writer.write(reader.read());
        }
    }

    private File getSource(String path) {

        File result = new File(path);
        if (!result.exists()) {

            System.err.printf("Source file not found! '%s'%n", result.getAbsolutePath());
            throw new RuntimeException("File not found!");
        }
        if (result.isDirectory()) {

            System.err.printf("Source is a directory! '%s'%n", result.getAbsolutePath());
            throw new RuntimeException("File is Directory!");
        }
        return result;
    }

    private File getTarget(String targetParent) throws IOException {

        String targetName = String.format("out-%s.jsonl", UUID.randomUUID());
        return getTarget(targetParent, targetName);
    }

    private File getTarget(String targetParent, String targetName) throws IOException {

        File result = new File(targetParent, targetName);
        result.getParentFile().mkdirs();

        result.createNewFile();
        System.out.printf("Dumping data to file '%s'%n", result.getAbsolutePath());
        return result;
    }

    @Override
    public void close() throws IOException {

        reader.close();
        writer.close();
    }

    private ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return mapper;
    }
}
