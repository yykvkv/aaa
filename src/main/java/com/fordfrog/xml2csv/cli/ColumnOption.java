package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;

public class ColumnOption extends Option {

    private static final String MODE = "c";
    private static final String NAME = "columns";
    private static final boolean HAS_ARG = true;
    private static final String DESCRIPTION = "List of columns that should be output to the CSV file. " +
            "These names must correspond to the element names within the item element.";

    public ColumnOption() {
        super(MODE, NAME, HAS_ARG, DESCRIPTION);
    }

}
