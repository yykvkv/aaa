package com.fordfrog.xml2csv.cli;

import org.apache.commons.cli.HelpFormatter;

public class HelpPrinter {

    private static final String APPLICATION_NAME = "xml-to-csv";

    public void print() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APPLICATION_NAME, new CommandLineOptions());
    }

}

