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

    /*public void convert(final InputStream inputStream, final Writer writer) {
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
    }*/

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

            final XMLStreamReader reader = xMLInputFactory.
                    createXMLStreamReader(inputStream);

            //List<Row> rows = new ArrayList<>();
            List<String> columns = config.getColumns();
            StringBuilder currentPath = new StringBuilder();
            Row row = new Row(columns.size());
            //System.out.println("row column count " + row.getNumberOfColumns());
            int columnIndex = -1;
            while (reader.hasNext()) {
                int next = reader.next();
                switch (next) {
                    case XMLStreamReader.START_ELEMENT:
                        //System.out.println("START_ELEMENT " + reader.getLocalName());
                        String name = getLocalName(reader);
                        currentPath.append("/").append(name);
                        //System.out.println("currentPath " + currentPath.toString());

                        //System.out.println("config item name " + config.getItemName());
                        //System.out.println(currentPath.toString());
                        //System.out.println(config.getItemName());

                        String toFind = currentPath.toString().replace(config.getItemName() + "/", "");
                        //System.out.println("toFind " + toFind + " in " + columns.toString());
                        columnIndex = columns.indexOf(toFind);
                        if (columnIndex == -1 && toFind.contains("@")) {
                            toFind = toFind.substring(0, toFind.indexOf("["));
                            //System.out.println("not found removed attribute now toFind " + toFind);
                            columnIndex = columns.indexOf(toFind);
                        }

                        /*if (columnIndex == -1 && reader.getAttributeCount() > 0) {
                            for (int a = 0; a < reader.getAttributeCount() && columnIndex == -1; a++) {
                                String attributeToFind = toFind + "[@" + reader.getAttributeLocalName(a) + "='" + reader.getAttributeValue(a) + "']";
                                System.out.println("attributeToFind " + attributeToFind);
                                columnIndex = columns.indexOf(attributeToFind);
                                System.out.println("new columnIndex " + columnIndex);
                            }
                        }*/

                        //System.out.println(reader.getLocalName() + " attribute count " + reader.getAttributeCount() + " columnIndex " + columnIndex + " toFind " + toFind + " cols " + columns);
                        if (currentPath.toString().equals(config.getItemName())) {
                            //System.out.println("ROW START");
                            row = new Row(columns.size());
                        }
                        break;
                    case XMLStreamReader.CHARACTERS:
                        //System.out.println("CHARACTERS " + reader.getText());
                        if (columnIndex > -1) {
                            //System.out.println("adding text " + currentPath.toString() + " " + reader.getText());
                            if (config.shouldJoin()) {
                                row.join(columnIndex, reader.getText());
                            } else {
                                row.append(columnIndex, reader.getText());
                            }
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        if (currentPath.toString().equals(config.getItemName())) {
                            //System.out.println("ROW END");
                            //if (row.size() > 0) {
                            writeRow(writer, row);
                                //rows.add(row);
                            //}
                        }
                        columnIndex = -1;

                        String path = currentPath.toString();
                        if (!path.isEmpty()) {
                            int startIndex = path.lastIndexOf("/" + reader.getLocalName());
                            //System.out.println("removing element " + reader.getLocalName() + " from path " + path + " start index " + startIndex + " " + path.length());
                            currentPath = new StringBuilder(path.substring(0, startIndex));
                            //System.out.println("path after removal " + currentPath.toString());
                        }

                        //currentPath = new StringBuilder(StringUtils.removeEnd(currentPath.toString(), "/" + getLocalName(reader)));
                        break;
                }

            }

            //System.out.println("rows " + rows.size());
            //for (Row row1 : rows) {
            //    writeRow(writer, row1);
                //System.out.println(row1);
            //}

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

    private String toText(XMLStreamReader reader) {
        String value = reader.getText();
        if (config.shouldTrim())
            value = value.trim();
        return value;
    }

    private void writeRow(Writer writer, Row row) throws IOException {
        ArrayList<String> quoted = new ArrayList<>();
        for (String i : row) {
            if (config.shouldTrim())// && i != null)
               i = i.trim();
            quoted.add(CsvUtils.quoteString(i));
        }
        writer.append(StringUtils.join(quoted, config.getSeparator()));
        writer.append(System.lineSeparator());
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
                    System.out.println("process root - current element path " + currentElementPath);
                    System.out.println("process root - item name " + itemName);
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
            int next = reader.next();
            System.out.println("reader.next() " + next);
            switch (next) {
                case XMLStreamReader.START_ELEMENT:
                    final String currentElementPath = getParentName(
                            parentElement, reader.getLocalName());

                    //if (reader.getAttributeCount() > 0) {
                    //    System.out.println("attribute " + reader.getAttributeLocalName(0) + " " + reader.getAttributeValue(0));
                    //}
                    processItem(reader, writer, currentElementPath, values);
                    break;
                case XMLStreamReader.CHARACTERS:
                    sb.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    String itemName = config.getItemName();

                    System.out.println("parentElement " + parentElement + " itemName " + itemName);
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

            System.out.println("writeRow() method");
            System.out.println("values keyset " + values.keySet().toString());
            System.out.println("valuesset " + values.values().toString());
            System.out.println("column " + columns.get(i));
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
        System.out.println("process value method()");
        System.out.println("elementName " + elementName);
        System.out.println("value " + value);
        System.out.println("values before " + values.toString());

        if (!values.containsKey(elementName)) {
            values.put(elementName, new ArrayList<String>(10));
        }

        values.get(elementName).add(value);
        System.out.println("values after " + values.toString());
    }

}
