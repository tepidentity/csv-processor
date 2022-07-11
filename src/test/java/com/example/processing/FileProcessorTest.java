package com.example.processing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

    @TempDir
    File outDir;

    @Test
    public void happyPath_file1() throws IOException {

        processFile("src/test/resources/DSV input 1.txt", ",");
    }

    @Test
    public void happyPath_file2() throws IOException {

        processFile("src/test/resources/DSV input 2.txt", "|");
    }

    private void processFile(String sourceFile, String delimiter) throws IOException {

        try (FileProcessor fileProcessor = new FileProcessor(sourceFile, outDir.toString(), delimiter)) {

            fileProcessor.process();
        }

        File[] files = outDir.listFiles();
        File expected = new File("src/test/resources/JSONL output.jsonl");

        assertAll(
                () -> assertEquals(1, files.length), // single output file
                () -> assertLinesMatch(Files.lines(expected.toPath()), Files.lines(files[0].toPath()))
        );
   }
}