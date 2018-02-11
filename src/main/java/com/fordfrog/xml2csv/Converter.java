package com.fordfrog.xml2csv;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static java.nio.charset.StandardCharsets.*;
import static org.apache.commons.lang3.StringUtils.*;

public class Converter {

    private final ValuesConverter valuesConverter;

    private final Map<String, Integer> columns;
    private final String itemName;
    private final boolean shouldJoin;
    private final boolean shouldTrim;

    private final CurrentColumn currentColumn;
    private final CurrentPath currentPath;
    private Row row;

    public Converter(ConversionConfig config) {
        this.columns = config.getColumns();
        this.itemName = config.getRowItemName();
        this.shouldJoin = config.shouldJoin();
        this.shouldTrim = config.shouldTrim();
        this.valuesConverter = new ValuesConverter(config.getSeparator());

        this.currentColumn = new CurrentColumn();
        this.currentPath = new CurrentPath(itemName);
    }

    public String convert(String input) {
        InputStream inputStream = IOUtils.toInputStream(input, UTF_8);
        StringWriter writer = new StringWriter();
        convert(inputStream, new DefaultCsvWriter(writer));
        return writer.toString();
    }

    public void convert(PathProvider pathProvider) {
        Path inputFilePath = pathProvider.getInputFilePath();
        Path outputFilePath = pathProvider.getOutputFilePath();
        convert(inputFilePath, outputFilePath);
    }

    public void convert(Path inputFile, Path outputFile) {
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
                currentPath.reset();
                row = new Row(shouldTrim, columns.size());
                currentColumn.reset();
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
        currentPath.append(name);
        String toFind = currentPath.getElementToFind();
        if (columns.containsKey(toFind)) {
            currentColumn.setIndex(columns.get(toFind));
        } else if (toFind.contains("@")) {
            toFind = toFind.substring(0, toFind.indexOf("["));
            if (columns.containsKey(toFind)) {
                currentColumn.setIndex(columns.get(toFind));
            }
        }
        if (currentPath.isNewLine()) {
            row = new Row(shouldTrim, columns.size());
        }
    }

    private void handleCharacters(XMLStreamReader reader) {
        if (currentColumn.isSet()) {
            int index = currentColumn.getIndex();
            if (shouldJoin) {
                row.join(index, reader.getText());
            } else {
                row.append(index, reader.getText());
            }
        }
    }

    private void handleEndElement(XMLStreamReader reader, LineHandler lineHandler) {
        if (currentPath.isNewLine()) {
            String line = valuesConverter.toLine(row.getValues());
            lineHandler.handle(line);
        }
        currentColumn.reset();
        if (!currentPath.isEmpty()) {
            currentPath.removeLastOccurrence(reader.getLocalName());
        }
    }

    private static class CurrentColumn {

        private static final int NOT_SET_VALUE = -1;

        private int index = NOT_SET_VALUE;

        public void reset() {
            index = NOT_SET_VALUE;
        }

        public boolean isSet() {
            return index > NOT_SET_VALUE;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

    }

    private static class CurrentPath {

        private final String itemName;
        private String path = EMPTY;

        public CurrentPath(String itemName) {
            this.itemName = itemName;
        }

        public void reset() {
            path = EMPTY;
        }

        public void append(String name) {
            path += "/" + name;
        }

        public String getElementToFind() {
            return path.replace(itemName + "/", "");
        }

        public boolean isEmpty() {
            return StringUtils.isEmpty(path);
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
