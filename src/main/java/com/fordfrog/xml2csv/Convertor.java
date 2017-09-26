package com.fordfrog.xml2csv;

import jdk.internal.util.xml.impl.Input;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
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
            final XMLStreamReader reader = xMLInputFactory.
                    createXMLStreamReader(inputStream);

            writeHeader(writer);

            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamReader.START_ELEMENT:
                        processRoot(reader, writer, getParentName(null,
                                        reader.getLocalName()));
                }
            }
        } catch (final IOException ex) {
            throw new RuntimeException("IO operation failed", ex);
        } catch (final XMLStreamException ex) {
            throw new RuntimeException("XML stream exception", ex);
        }
    }

    private void writeHeader(final Writer writer) throws IOException {
        List<String> columns = config.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                writer.append(config.getSeparator());
            }

            writer.append(CsvUtils.quoteString(columns.get(i)));
        }

        writer.append('\n');
    }

    private void processRoot(final XMLStreamReader reader,
            final Writer writer, final String parentElement) throws XMLStreamException,
            IOException {
        List<String> columns = config.getColumns();
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    final String currentElementPath = getParentName(
                            parentElement, reader.getLocalName());
                    String itemName = config.getItemName();
                    if ((currentElementPath).compareTo(itemName) == 0) {
                        final Map<String, List<String>> values = new HashMap<>(
                                columns.size());
                        processItem(reader, writer, currentElementPath, values);
                    } else {
                        processRoot(reader, writer, currentElementPath);
                    }

                    break;
                case XMLStreamReader.END_ELEMENT:
                    return;
            }
        }
    }

    private void processItem(final XMLStreamReader reader,
            final Writer writer, final String parentElement,
            final Map<String, List<String>> values)
            throws XMLStreamException,
            IOException {
        final StringBuilder sb = new StringBuilder(1_024);

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamReader.START_ELEMENT:
                    final String currentElementPath = getParentName(
                            parentElement, reader.getLocalName());
                    processItem(reader, writer, currentElementPath, values);

                    break;
                case XMLStreamReader.CHARACTERS:
                    sb.append(reader.getText());

                    break;
                case XMLStreamReader.END_ELEMENT:
                    String itemName = config.getItemName();
                    if ((parentElement).compareTo(itemName) == 0) {
                        List<String> columns = config.getColumns();
                        final Map<String, String> singleValues = new HashMap<>(
                                columns.size());

                        boolean trim = config.shouldTrim();
                        boolean join = config.shouldJoin();
                        for (Entry<String, List<String>> mapEntry : values.
                                entrySet()) {
                            singleValues.put(mapEntry.getKey(), prepareValue(
                                    mapEntry.getValue(), ", ", trim, join));
                        }

                        Filters filters = config.getFilters();
                        Remappings remappings = config.getRemappings();
                        if (filters == null || filters.matchesFilters(
                                singleValues)) {
                            if (remappings != null) {
                                remappings.replaceValues(singleValues);
                            }

                            char separator = config.getSeparator();
                            writeRow(writer, singleValues, separator);
                        }
                    } else {
                        processValue(parentElement.replaceFirst(Pattern.quote(
                                itemName + "/"), ""), sb.toString(), values);
                    }
                    return;
            }
        }
    }

    private void writeRow(final Writer writer,
            final Map<String, String> values, final char separator)
            throws IOException {
        List<String> columns = config.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                writer.append(separator);
            }

            writer.append(CsvUtils.quoteString(values.get(columns.get(i))));
        }

        writer.append('\n');
    }

    private static String prepareValue(List<String> values,
            final String valueSeparator, final boolean trim, final boolean join) {
        if (values.isEmpty()) {
            return null;
        }
        if (join) {
            final StringBuilder sb = new StringBuilder(1_024);

            for (int i = 0; i < values.size(); i++) {
                final String value = trim ? values.get(i).trim() : values.get(i);

                sb.append(value);

                if (i < values.size() - 1) {
                    sb.append(valueSeparator);
                }
            }

            return sb.toString();
        } else {
            final String value = values.get(0);

            return trim ? value.trim() : value;
        }
    }

    private static String getParentName(final String parentName,
            final String currentElement) {
        return (parentName == null ? "" : parentName) + "/" + currentElement;
    }

    private static void processValue(String elementName, String value,
            Map<String, List<String>> values) {
        if (!values.containsKey(elementName)) {
            values.put(elementName, new ArrayList<String>(10));
        }

        values.get(elementName).add(value);
    }

}
