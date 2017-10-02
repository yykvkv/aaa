package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;

public class InputFileOption extends Option {

    private static final String MODE = "i";
    private static final String NAME = "inputFile";
    private static final boolean HAS_ARG = true;
    private static final String DESCRIPTION = "Path to the input XML file. " +
            "Input file content should always be in UTF-8 encoding.";

    public InputFileOption() {
        super(MODE, NAME, HAS_ARG, DESCRIPTION);
    }

}
