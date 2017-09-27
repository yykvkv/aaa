package com.fordfrog.xml2csv;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Convertor {

    private final ConversionConfig config;

    public Convertor(ConversionConfig config) {
        this.config = config;
    }

    public void convert(final Path inputFile, final Path outputFile) {
        try (final InputStream inputStream = Files.newInputStream(inputFile);
             final Writer writer = Files.newBufferedWriter(
                     outputFile, Charset.forName("UtF-8"))) {
            convert(inputStream, writer);
        } catch (final IOException ex) {
            throw new RuntimeException("IO operation failed", ex);
        }
    }

    public String convert(String input) throws IOException {
        StringWriter writer = new StringWriter();
        InputStream in = IOUtils.toInputStream(input, "UTF-8");
        convert(in, writer);
        return writer.toString();
    }

    public void convert(final InputStream inputStream, final Writer writer) {
        String itemName = config.getItemName();
        final XMLInputFactory xMLInputFactory = XMLInputFactory.newInstance();

        if (itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("itemName is an empty string. ");
        }

        if (itemName.trim().length() != 1 && itemName.endsWith("/")) {
            throw new IllegalArgumentException(
                    "itemName cannot end with a shash (/).");
        }

        try {
            writeHeader(writer);
            final XMLStreamReader reader = xMLInputFactory.createXMLStreamReader(inputStream);
            List<String> columns = config.getColumns();
            StringBuilder currentPath = new StringBuilder();
            Row row = new Row(columns.size());
            int columnIndex = -1;
            while (reader.hasNext()) {
                int next = reader.next();
                switch (next) {
                    case XMLStreamReader.START_ELEMENT:
                        String name = getLocalName(reader);
                        currentPath.append("/").append(name);
                        String toFind = currentPath.toString().replace(config.getItemName() + "/", "");
                        columnIndex = columns.indexOf(toFind);
                        if (columnIndex == -1 && toFind.contains("@")) {
                            toFind = toFind.substring(0, toFind.indexOf("["));
                            columnIndex = columns.indexOf(toFind);
                        }
                        if (currentPath.toString().equals(config.getItemName())) {
                            row = new Row(columns.size());
                        }
                        break;
                    case XMLStreamReader.CHARACTERS:
                        if (columnIndex > -1) {
                            if (config.shouldJoin()) {
                                row.join(columnIndex, reader.getText());
                            } else {
                                row.append(columnIndex, reader.getText());
                            }
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        if (currentPath.toString().equals(config.getItemName()))
                            writeRow(writer, row);
                        columnIndex = -1;
                        String path = currentPath.toString();
                        if (!path.isEmpty()) {
                            int startIndex = path.lastIndexOf("/" + reader.getLocalName());
                            currentPath = new StringBuilder(path.substring(0, startIndex));
                        }
                        break;
                }
            }
        } catch (final IOException ex) {
            throw new RuntimeException("IO operation failed", ex);
        } catch (final XMLStreamException ex) {
            throw new RuntimeException("XML stream exception", ex);
        }
    }

    private String getLocalName(XMLStreamReader reader) {
        StringBuilder name = new StringBuilder(reader.getLocalName());
        if (reader.getAttributeCount() > 0)
            for (int a = 0; a < reader.getAttributeCount(); a++)
                name.append("[@").append(reader.getAttributeLocalName(a)).append("='").append(reader.getAttributeValue(a)).append("']");
        return name.toString();
    }

    private void writeRow(Writer writer, Row row) throws IOException {
        writer.append(StringUtils.join(extractValues(row), config.getSeparator()));
        writer.append(System.lineSeparator());
    }

    private List<String> extractValues(Row row) {
        if (config.shouldTrim())
            return row.getTrimmedValues();
        return row.getValues();
    }

    private void writeHeader(final Writer writer) throws IOException {
        List<String> columns = new ArrayList<>(config.getColumns().size());
        for (String column : config.getColumns())
            columns.add(CsvUtils.quoteString(column));
        writer.append(StringUtils.join(columns, config.getSeparator()));
        writer.append(System.lineSeparator());
    }

}
