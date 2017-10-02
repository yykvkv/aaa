package com.fordfrog.xml2csv.cli;

import com.fordfrog.xml2csv.Xml2CsvException;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ArgumentsValidator {

    public boolean isValid(Arguments arguments) {
        validateColumns(arguments);
        validateRowItemName(arguments);
        validateInputPath(arguments);
        validateOutputPath(arguments);
        return true;
    }

    private void validateColumns(Arguments arguments) {
        if (StringUtils.isEmpty(arguments.getColumnsAsString()))
            throw new Xml2CsvException("at least one column must be provided");
    }

    private void validateRowItemName(Arguments arguments) {
        if (StringUtils.isEmpty(arguments.getRowItemName())) {
            throw new Xml2CsvException("row item name must be provided");
        }
    }


    private void validateInputPath(Arguments arguments) {
        String path = arguments.getInputFilePathAsString();
        if (StringUtils.isEmpty(path))
            throw new Xml2CsvException("input file path must be provided");

        if (!Files.exists(Paths.get(path)))
            throw new Xml2CsvException("input file " + path + " does not exist");
    }

    private void validateOutputPath(Arguments arguments) {
        String path = arguments.getOutputFilePathAsString();
        if (StringUtils.isEmpty(path))
            throw new Xml2CsvException("output file path must be provided");

        //if (!Files.exists(Paths.get(path)))
        //    throw new Xml2CsvException("output file " + path + " does not exist");
    }

}
