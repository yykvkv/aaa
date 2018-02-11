package com.fordfrog.xml2csv;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static java.nio.charset.StandardCharsets.*;
import static org.apache.commons.lang3.StringUtils.*;

public class Converter {

    private final Map<String, Integer> columns;
    private final String itemName;
    private final boolean shouldJoin;
    private final boolean shouldTrim;
    private final ValuesConverter valuesConverter;

    private Column column;
    private Path path;
    private Row row;

    public Converter(ConversionConfig config) {
        this.columns = config.getColumns();
        this.itemName = config.getRowItemName();
        this.shouldJoin = config.shouldJoin();
        this.shouldTrim = config.shouldTrim();
        this.valuesConverter = new ValuesConverter(config.getSeparator());
        initialise();
    }

    public String convert(String input) {
        InputStream inputStream = IOUtils.toInputStream(input, UTF_8);
        StringWriter writer = new StringWriter();
        convert(inputStream, new DefaultCsvWriter(writer));
        return writer.toString();
    }

    public void convert(PathProvider pathProvider) {
        java.nio.file.Path inputFilePath = pathProvider.getInputFilePath();
        java.nio.file.Path outputFilePath = pathProvider.getOutputFilePath();
        convert(inputFilePath, outputFilePath);
    }

    public void convert(java.nio.file.Path inputFile, java.nio.file.Path outputFile) {
        try (InputStream inputStream = Files.newInputStream(inputFile)) {
            try (Writer writer = Files.newBufferedWriter(outputFile, UTF_8)) {
                convert(inputStream, new DefaultCsvWriter(writer));
            }
        } catch (final IOException e) {
            throw new Xml2CsvException(e);
        }
    }

    private void convert(InputStream inputStream, LineHandler lineHandler) {
        writeHeader(lineHandler);
        writeData(inputStream, lineHandler);
    }

    private void writeHeader(LineHandler writer) {
        String line = valuesConverter.toLine(columns.keySet());
        writer.handle(line);
    }

    private void writeData(InputStream inputStream, LineHandler lineHandler) {
        try {
            final XMLStreamReader reader = InputStreamConverter.toXmlStreamReader(inputStream);
            try {
                initialise();
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
                            handleEndElement(reader, lineHandler);
                            break;
                        default:
                            // intentionally blank
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
        String name = XmlStreamReaderConverter.toLocalName(reader);
        path.append(name);
        System.out.println("start element " + path.path);
        String currentElement = path.getCurrentElement();
        System.out.println("required " + columns.containsKey(currentElement));
        if (columns.containsKey(currentElement)) {
            column.setIndex(columns.get(currentElement));
        } else if (currentElement.contains("@")) {
            currentElement = currentElement.substring(0, currentElement.indexOf("["));
            if (columns.containsKey(currentElement)) {
                column.setIndex(columns.get(currentElement));
            }
        }
    }

    private void initialise() {
        column = new Column();
        path = new Path(itemName);
        row = new Row(shouldTrim, columns.size());
    }

    private void handleCharacters(XMLStreamReader reader) {
        if (!column.isSet()) {
            return;
        }

        int index = column.getIndex();
        if (shouldJoin) {
            row.join(index, reader.getText());
        } else {
            row.append(index, reader.getText());
        }
    }

    private void handleEndElement(XMLStreamReader reader, LineHandler lineHandler) {
        System.out.println("ending element " + path.path);
        if (path.isNewLine()) {
            String line = valuesConverter.toLine(row.getValues());
            System.out.println("row complete, writing " + line + " to csv");
            lineHandler.handle(line);
            row = new Row(shouldTrim, columns.size());
        }
        column = new Column();
        path.removeLastOccurrence(reader.getLocalName());
    }

    private static class Column {

        private static final int NOT_SET_VALUE = -1;

        private int index = NOT_SET_VALUE;

        public boolean isSet() {
            return index > NOT_SET_VALUE;
        }

        public void setIndex(int index) {
            System.out.println("set current column " + index);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

    }

    private static class Path {

        private final String itemName;
        private String path = EMPTY;

        public Path(String itemName) {
            this.itemName = itemName;
        }

        public void append(String name) {
            path += "/" + name;
        }

        public String getCurrentElement() {
            return path.replace(itemName + "/", "");
        }

        public boolean isNewLine() {
            return path.equals(itemName);
        }

        public void removeLastOccurrence(String name) {
            int startIndex = path.lastIndexOf("/" + name);
            path = path.substring(0, startIndex);
        }

    }

}
