package com.fordfrog.xml2csv;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Convertor {

    private static final Charset UTF8 = Charset.forName("UTF8");

    private final InputStreamConverter inputStreamConverter = new InputStreamConverter();
    private final XmlStreamReaderConverter xmlStreamReaderConverter = new XmlStreamReaderConverter();

    private final List<String> columns;
    private final String itemName;
    private final boolean shouldJoin;
    private final boolean shouldTrim;
    private final char separator;

    private int columnIndex;
    private StringBuilder currentPath;
    private Row row;

    public Convertor(ConversionConfig config) {
        this.columns = config.getColumns();
        this.itemName = config.getItemName();
        this.shouldJoin = config.shouldJoin();
        this.shouldTrim = config.shouldTrim();
        this.separator = config.getSeparator();
    }

    public String convert(String input) {
        InputStream inputStream = IOUtils.toInputStream(input, UTF8);
        StringWriter writer = new StringWriter();
        convert(inputStream, new DefaultCsvWriter(writer, separator));
        return writer.toString();
    }

    public void convert(final Path inputFile, final Path outputFile) {
        try (final InputStream inputStream = Files.newInputStream(inputFile)) {
            try (final Writer writer = Files.newBufferedWriter(outputFile, UTF8)) {
                convert(inputStream, new DefaultCsvWriter(writer, separator));
            }
        } catch (final IOException e) {
            throw new Xml2CsvException(e);
        }
    }

    public void convert(final InputStream inputStream, CsvWriter writer) {
        writeHeader(writer);
        writeData(inputStream, writer);
    }

    private void writeHeader(CsvWriter writer) {
        writer.write(columns);
    }

    private void writeData(final InputStream inputStream, CsvWriter writer) {
        try {
            final XMLStreamReader reader = inputStreamConverter.toXmlStreamReader(inputStream);
            try {
                currentPath = new StringBuilder();
                row = new Row(shouldTrim, columns.size());
                columnIndex = -1;
                while (reader.hasNext()) {
                    int next = reader.next();
                    switch (next) {
                        case XMLStreamReader.START_ELEMENT:
                            handleStartElement(reader);
                            break;
                        case XMLStreamReader.CHARACTERS:
                            handleCharacters(reader);
                            break;
                        case XMLStreamReader.END_ELEMENT:
                            handleEndElement(reader, writer);
                            break;
                    }
                }
            } finally {
                reader.close();
            }
        } catch (XMLStreamException e) {
            throw new Xml2CsvException(e);
        }
    }

    private void handleStartElement(XMLStreamReader reader) {
        String name = xmlStreamReaderConverter.toLocalName(reader);
        currentPath.append("/").append(name);
        String toFind = currentPath.toString().replace(itemName + "/", "");
        columnIndex = columns.indexOf(toFind);
        if (columnIndex == -1 && toFind.contains("@")) {
            toFind = toFind.substring(0, toFind.indexOf("["));
            columnIndex = columns.indexOf(toFind);
        }
        if (currentPath.toString().equals(itemName)) {
            row = new Row(shouldTrim, columns.size());
        }
    }

    private void handleCharacters(XMLStreamReader reader) {
        if (columnIndex > -1) {
            if (shouldJoin) {
                row.join(columnIndex, reader.getText());
            } else {
                row.append(columnIndex, reader.getText());
            }
        }
    }

    private void handleEndElement(XMLStreamReader reader, CsvWriter writer) {
        if (currentPath.toString().equals(itemName))
            writer.write(row);
        columnIndex = -1;
        String path = currentPath.toString();
        if (!path.isEmpty()) {
            int startIndex = path.lastIndexOf("/" + reader.getLocalName());
            currentPath = new StringBuilder(path.substring(0, startIndex));
        }
    }

}
