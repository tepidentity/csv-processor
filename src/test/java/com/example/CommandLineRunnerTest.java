package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineRunnerTest {

    @TempDir
    File outDir;

    @Test
    public void happyPath_shorthandArgs() {

        assertDoesNotThrow(() -> CommandLineRunner.main(
                "-s", "src/test/resources/DSV input 1.txt",
                "-t", outDir.toString(),
                "-d", ","));

        assertFile();
    }

    @Test
    public void happyPath_fullLengthArgs() {

        assertDoesNotThrow(() -> CommandLineRunner.main(
                "--source", "src/test/resources/DSV input 1.txt",
                "--target", outDir.toString(),
                "--delimiter", ","));

        assertFile();
    }

    @Test
    public void failOnMissingArg_Source() {

        Exception expected = assertThrows(IllegalArgumentException.class, CommandLineRunner::main);

        assertAll(() -> assertTrue(expected.getMessage().contains("'-s'")),
                () -> assertTrue(expected.getMessage().contains("'--source'")));
    }

    @Test
    public void failOnMissingArg_Delimiter() {

        Exception expected = assertThrows(IllegalArgumentException.class,
                () -> CommandLineRunner.main("-s", "src/test/resources/DSV input 1.txt"));

        assertAll(() -> assertTrue(expected.getMessage().contains("'-d'")),
                () -> assertTrue(expected.getMessage().contains("'--delimiter'")));
    }

    private void assertFile() {

        File[] files = outDir.listFiles();
        File expected = new File("src/test/resources/JSONL output.jsonl");

        assertAll(
                () -> assertEquals(1, files.length), // single output file
                () -> assertLinesMatch(Files.lines(expected.toPath()), Files.lines(files[0].toPath()))
        );
    }
}