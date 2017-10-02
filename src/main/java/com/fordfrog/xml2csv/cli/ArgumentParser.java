package com.fordfrog.xml2csv.cli;

import com.fordfrog.xml2csv.DefaultSettings;
import com.fordfrog.xml2csv.cli.Arguments.ArgumentsBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.List;

public class ArgumentParser {

    private final CommandLineOptions options;
    private final CommandLineParser parser;

    public ArgumentParser() {
        this(new CommandLineOptions(), new DefaultParser());
    }

    public ArgumentParser(CommandLineOptions options, CommandLineParser parser) {
        this.options = options;
        this.parser = parser;
    }

    public Arguments parse(String... args) {
        try {
            CommandLine commandLine = parser.parse(options, args);
            return toArguments(commandLine);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Arguments toArguments(CommandLine commandLine) {
        return new ArgumentsBuilder()
                .setInputFilePath(extractInputFilePath(commandLine))
                .setOutputFilePath(extractOutputFilePath(commandLine))
                .setItemName(extractRowItemName(commandLine))
                .setColumns(extractColumns(commandLine))
                .setSeparator(extractSeparator(commandLine))
                .setJoin(extractJoin(commandLine))
                .setTrim(extractTrim(commandLine))
                .build();
    }

    private String extractInputFilePath(CommandLine commandLine) {
        return commandLine.getOptionValue(options.getInputFile());
    }

    private String extractOutputFilePath(CommandLine commandLine) {
        return commandLine.getOptionValue(options.getOutputFile());
    }

    private String extractRowItemName(CommandLine commandLine) {
        return commandLine.getOptionValue(options.getRowItemName());
    }

    private String extractColumns(CommandLine commandLine) {
        return commandLine.getOptionValue(options.getColumn());
    }

    private String extractSeparator(CommandLine commandLine) {
        return commandLine.getOptionValue(options.getSeparator(), DefaultSettings.SEPARATOR);
    }

    private boolean extractJoin(CommandLine commandLine) {
        return commandLine.hasOption(options.getJoin());
    }

    private boolean extractTrim(CommandLine commandLine) {
        return commandLine.hasOption(options.getTrim());
    }

}
