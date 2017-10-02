package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineOptions extends Options {

    private final Option inputFile = new InputFileOption();
    private final Option outputFile = new OutputFileOption();
    private final Option rowItemName = new RowItemNameOption();
    private final Option column = new ColumnOption();
    private final Option separator = new SeparatorOption();
    private final Option join = new JoinOption();
    private final Option trim = new TrimOption();

    public CommandLineOptions() {
        addOption(inputFile);
        addOption(outputFile);
        addOption(rowItemName);
        addOption(column);
        addOption(separator);
        addOption(join);
        addOption(trim);
    }

    public String getInputFile() {
        return inputFile.getOpt();
    }

    public String getOutputFile() {
        return outputFile.getOpt();
    }

    public String getRowItemName() {
        return rowItemName.getOpt();
    }

    public String getColumn() {
        return column.getOpt();
    }

    public String getSeparator() {
        return separator.getOpt();
    }

    public String getJoin() {
        return join.getOpt();
    }

    public String getTrim() {
        return trim.getOpt();
    }

}
