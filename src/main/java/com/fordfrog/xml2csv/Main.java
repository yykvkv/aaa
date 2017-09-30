package com.fordfrog.xml2csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;

public class Main {

    public static void main(final String[] args) {
        if (args == null || args.length == 0) {
            printUsage();
            return;
        }

        String[] columns = null;
        Path inputFile = null;
        Path outputFile = null;
        char separator = ',';
        boolean trimValues = false;
        boolean join = false;
        String itemName = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--columns":
                    i++;
                    columns = args[i].split(",");

                    break;
                case "--input":
                    i++;
                    inputFile = Paths.get(args[i]);

                    break;
                case "--item-name":
                    i++;
                    itemName = args[i];

                    break;
                case "--output":
                    i++;
                    outputFile = Paths.get(args[i]);

                    break;
                case "--separator":
                    i++;

                    if (args[i].length() == 1) {
                        separator = args[i].charAt(0);
                    } else {
                        throw new RuntimeException(
                                "Separator must be a character.");
                    }

                    break;
                case "--trim":
                    trimValues = true;

                    break;
                case "--join":
                    join = true;

                    break;
                default:
                    throw new RuntimeException(MessageFormat.format(
                            "Unsupported command line argument: {0}", args[i]));
            }
        }

        Objects.requireNonNull(columns, "--columns argument must be specified, "
                + "example: --columns COL1,COL2");
        Objects.requireNonNull(inputFile, "--input argument must be specified, "
                + "example: --input input_file_path");
        Objects.requireNonNull(outputFile, "--output argument must be "
                + "specified, example: --output output_file_path");
        Objects.requireNonNull(columns, "--output argument must be specified, "
                + "example: --output output_file_path");
        Objects.requireNonNull(itemName, "--item-name argument must be "
                + "specified, example: --item-name /root/item");

        ConversionConfig config = new DefaultConversionConfig.DefaultConversionConfigBuilder()
                .setColumns(Arrays.asList(columns))
                .setSeparator(separator)
                .setTrim(trimValues)
                .setJoin(join)
                .setItemName(itemName)
                .build();
        Convertor convertor = new Convertor(config);
        convertor.convert(inputFile, outputFile);
    }

    private static void printUsage() {
        try (final InputStream inputStream = Main.class.getResourceAsStream(
                "/usage.txt");
                final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, "UTF-8"))) {
            String line = reader.readLine();

            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (final IOException ex) {
            throw new RuntimeException(
                    "Failed to output usage information", ex);
        }
    }

}
