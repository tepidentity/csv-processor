package com.example.processing;

import com.example.model.Entry;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JSONLWriter implements Closeable {

    private final BufferedOutputStream buffer;
    private final ObjectMapper mapper;

    public JSONLWriter(File target, ObjectMapper mapper) throws FileNotFoundException {

        buffer = new BufferedOutputStream(new FileOutputStream(target));
        this.mapper = mapper;
    }

    public void write(Entry entry) throws IOException {

        String value = mapper.writeValueAsString(entry) + "\n";
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        buffer.write(bytes);
        buffer.flush();
    }

    @Override
    public void close() throws IOException {

        buffer.close();
    }
}
