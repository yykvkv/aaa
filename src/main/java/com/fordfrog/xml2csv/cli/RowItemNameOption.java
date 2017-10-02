package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.Option;

public class RowItemNameOption extends Option {

    private static final String MODE = "r";
    private static final String NAME = "row-item-name";
    private static final boolean HAS_ARG = true;
    private static final String DESCRIPTION = "XPath which refers to XML element which will be converted to a row.";

    public RowItemNameOption() {
        super(MODE, NAME, HAS_ARG, DESCRIPTION);
    }

}
