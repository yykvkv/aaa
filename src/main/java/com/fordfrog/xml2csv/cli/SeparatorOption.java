package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;

public class SeparatorOption extends Option {

    private static final String MODE = "s";
    private static final String NAME = "separator";
    private static final boolean HAS_ARG = true;
    private static final String DESCRIPTION = "Character that should be used to separate fields. Default value " +
            "is semi-colon.";

    public SeparatorOption() {
        super(MODE, NAME, HAS_ARG, DESCRIPTION);
    }

}
