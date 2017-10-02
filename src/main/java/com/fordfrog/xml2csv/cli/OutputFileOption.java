package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;

public class OutputFileOption extends Option {

    private static final String MODE = "o";
    private static final String NAME = "outputFile";
    private static final boolean HAS_ARG = true;
    private static final String DESCRIPTION = "Path to the output CSV file. " +
            "Output file content is always in UTF-8 encoding.";

    public OutputFileOption() {
        super(MODE, NAME, HAS_ARG, DESCRIPTION);
    }

}
