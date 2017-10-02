package com.fordfrog.xml2csv.cli;

import com.fordfrog.xml2csv.ColumnsConverter;
import com.fordfrog.xml2csv.ConversionConfig;
import com.fordfrog.xml2csv.DefaultSettings;
import com.fordfrog.xml2csv.PathProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Arguments implements ConversionConfig, PathProvider {

    private final String columns;
    private final String separator;
    private final boolean trim;
    private final boolean join;
    private final String rowItemName;
    private final String inputFilePath;
    private final String outputFilePath;

    public Arguments(ArgumentsBuilder builder) {
        this.columns = builder.columns;
        this.separator = builder.separator;
        this.trim = builder.trim;
        this.join = builder.join;
        this.rowItemName = builder.itemName;
        this.inputFilePath = builder.inputFilePath;
        this.outputFilePath = builder.outputFilePath;
    }

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap(columns);
    }

    @Override
    public String getSeparator() {
        return separator;
    }

    @Override
    public boolean shouldTrim() {
        return trim;
    }

    @Override
    public boolean shouldJoin() {
        return join;
    }

    @Override
    public String getRowItemName() {
        return rowItemName;
    }

    @Override
    public Path getOutputFilePath() {
        return Paths.get(outputFilePath);
    }

    @Override
    public Path getInputFilePath() {
        return Paths.get(inputFilePath);
    }

    public String getOutputFilePathAsString() {
        return outputFilePath;
    }

    public String getInputFilePathAsString() {
        return inputFilePath;
    }

    public String getColumnsAsString() {
        return columns;
    }

    public static class ArgumentsBuilder {

        private String columns;
        private String separator = DefaultSettings.SEPARATOR;
        private boolean trim = false;
        private boolean join = false;
        private String itemName;
        private String inputFilePath;
        private String outputFilePath;

        public ArgumentsBuilder setColumns(String columns) {
            this.columns = columns;
            return this;
        }

        public ArgumentsBuilder setSeparator(String separator) {
            this.separator = separator;
            return this;
        }

        public ArgumentsBuilder setTrim(boolean trim) {
            this.trim = trim;
            return this;
        }

        public ArgumentsBuilder setJoin(boolean join) {
            this.join = join;
            return this;
        }

        public ArgumentsBuilder setItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public ArgumentsBuilder setInputFilePath(String inputFilePath) {
            this.inputFilePath = inputFilePath;
            return this;
        }

        public ArgumentsBuilder setOutputFilePath(String outputFilePath) {
            this.outputFilePath = outputFilePath;
            return this;
        }

        public Arguments build() {
            return new Arguments(this);
        }

    }

}
