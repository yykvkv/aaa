package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;

public class TrimOption extends Option {

    private static final String MODE = "t";
    private static final String NAME = "trim";
    private static final boolean HAS_ARG = false;
    private static final String DESCRIPTION = "Trim values. By default values are not trimmed.";

    public TrimOption() {
        super(MODE, NAME, HAS_ARG, DESCRIPTION);
    }

}
