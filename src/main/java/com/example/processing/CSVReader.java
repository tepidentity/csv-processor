package com.example.processing;

import com.example.model.Entry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CSVReader implements Closeable {

    private final CSVParser csvRecords;

    public CSVReader(Path source, String delimiter) throws IOException {

        BufferedReader reader = Files.newBufferedReader(source);
        csvRecords = format(delimiter).parse(reader);
    }

    public Entry read() {

        CSVRecord record = csvRecords.iterator().next();
        Entry result = new Entry();
        for (String key : record.getParser().getHeaderNames())
        {
            parseValue(record.get(key)).ifPresent(value -> result.setProperty(key, value));
        }
        return result;
    }

    private Optional<?> parseValue(String value) {

        if (value == null || value.isEmpty()) {

            return Optional.empty();
        }

        Optional<LocalDate> dateValue = parseAsDate(value);
        if (dateValue.isPresent()) {

            return dateValue;
        }

        Optional<Number> intValue = parseAsNumber(value);
        if (intValue.isPresent()) {

            return intValue;
        }
        return Optional.of(value);
    }

    private Optional<LocalDate> parseAsDate(String value) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd]" + "[dd-MM-yyyy]" + "[yyyy/MM/dd]");

        try {
            return Optional.of(LocalDate.parse(value, formatter));

        } catch (Exception ignored) {

            // not a date
        }
        return Optional.empty();
    }

    private Optional<Number> parseAsNumber(String value) {

        try {
            return Optional.of(Integer.valueOf(value));

        } catch (Exception ignored) {

            // not an integer
        }
        return Optional.empty();
    }
    public boolean canRead() {

        return csvRecords.iterator().hasNext();
    }

    @Override
    public void close() throws IOException {

        csvRecords.close();
    }

    private CSVFormat format(String delimiter) {

        return CSVFormat.Builder.create()
                .setDelimiter(delimiter)
                .setRecordSeparator(delimiter)
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
    }
}
