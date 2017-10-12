package com.fordfrog.xml2csv;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class DefaultCsvWriter implements LineHandler {

    private final Writer writer;

    public DefaultCsvWriter() {
        this(new StringWriter());
    }

    public DefaultCsvWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void handler(String line) {
        try {
            writer.append(line);
            writer.append(System.lineSeparator());
        } catch (IOException e) {
            throw new Xml2CsvException(e);
        }
    }

}
