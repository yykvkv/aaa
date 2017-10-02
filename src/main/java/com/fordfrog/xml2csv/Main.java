package com.fordfrog.xml2csv;

import com.fordfrog.xml2csv.cli.ArgumentParser;
import com.fordfrog.xml2csv.cli.Arguments;
import com.fordfrog.xml2csv.cli.ArgumentsValidator;
import com.fordfrog.xml2csv.cli.HelpPrinter;

public class Main {

    private static final ArgumentsValidator VALIDATOR = new ArgumentsValidator();
    private static final HelpPrinter HELP_PRINTER = new HelpPrinter();

    public static void main(String... args) {
        try {
            Arguments arguments = parse(args);
            if (VALIDATOR.isValid(arguments))
                convert(arguments);
        } catch (Xml2CsvException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            HELP_PRINTER.print();
        }
    }

    private static Arguments parse(String... args) {
        ArgumentParser parser = new ArgumentParser();
        return parser.parse(args);
    }

    private static void convert(Arguments arguments) {
        Converter converter = new Converter(arguments);
        converter.convert(arguments);
    }

}
