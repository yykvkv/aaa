package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;

public class JoinOption extends Option {

    private static final String MODE = "j";
    private static final String NAME = "join";
    private static final boolean HAS_ARG = false;
    private static final String DESCRIPTION = "Join values of duplicated elements into single value using comma as " +
            "a separator. By default value of all duplicated elements are saved to CSV.";

    public JoinOption() {
        super(MODE, NAME, HAS_ARG, DESCRIPTION);
    }

}
