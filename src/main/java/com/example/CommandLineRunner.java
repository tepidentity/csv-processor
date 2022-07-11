package com.example;

import com.example.processing.FileProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandLineRunner {

    public static void main(String... args) throws IOException {

        Map<String, String> argsMap = Argument.parseArgs(args);

        String source = Argument.SOURCE.retrieveValue(argsMap).orElseThrow(() ->
                new IllegalArgumentException("Input file is mandatory. Set it up using '-s' or '--source'"));
        String target = Argument.TARGET.retrieveValue(argsMap).orElse(".");
        String delimiter = Argument.DELIMITER.retrieveValue(argsMap).orElseThrow(() ->
                new IllegalArgumentException("Delimiter is mandatory. Set it up using '-d' or '--delimiter'"));

        try (FileProcessor processor = new FileProcessor(source, target, delimiter)) {

            processor.process();
        }
    }

    private enum Argument {

        SOURCE("-s", "--source"),
        TARGET("-t", "--target"),
        DELIMITER("-d", "--delimiter");

        private final List<String> aliases;

        Argument(String... aliases) {

            this.aliases = Arrays.asList(aliases);
        }

        public Optional<String> retrieveValue(Map<String, String> args) {

            return args.entrySet().stream()
                    .filter(arg -> this.aliases.contains(arg.getKey()))
                    .findAny()
                    .map(Map.Entry::getValue);
        }

        private static boolean hasArgument(String arg) {

            return Arrays.stream(values()).anyMatch(argument -> argument.aliases.contains(arg));
        }

        public static Map<String, String> parseArgs(String... args) {

            Map<String, String> result = new HashMap<>();

            for (int i = 0, j = 1; j <= args.length; i += 2, j += 2) {

                if (!hasArgument(args[i])) {

                    throw new IllegalStateException(String.format("Parameter '%s' not recognized!", i));
                }

                if (j >= args.length || hasArgument(args[j])) {

                    throw new IllegalStateException(String.format("Missing value for parameter '%s'!", i));
                }

                result.put(args[i], args[j]);
            }

            return result;
        }
    }
}
