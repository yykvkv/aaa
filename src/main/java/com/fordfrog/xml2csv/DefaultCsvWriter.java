package com.fordfrog.xml2csv;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;

public class DefaultCsvWriter implements CsvWriter {

    private final Writer writer;
    private final char separator;

    public DefaultCsvWriter(char separator) {
        this(new StringWriter(), separator);
    }

    public DefaultCsvWriter(Writer writer, char separator) {
        this.writer = writer;
        this.separator = separator;
    }

    @Override
    public void write(Row row) {
        write(row.getValues());
    }

    @Override
    public void write(Collection<String> values) {
        try {
            writer.append(StringUtils.join(values, separator));
            writer.append(System.lineSeparator());
        } catch (IOException e) {
            throw new Xml2CsvException(e);
        }
    }

}
