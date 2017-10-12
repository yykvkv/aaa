package com.fordfrog.xml2csv;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Converter {

    private static final Charset UTF8 = Charset.forName("UTF8");

    private final InputStreamConverter inputStreamConverter = new InputStreamConverter();
    private final XmlStreamReaderConverter xmlStreamReaderConverter = new XmlStreamReaderConverter();

    private final ValuesConverter valuesConverter;

    private final Map<String, Integer> columns;
    private final String itemName;
    private final boolean shouldJoin;
    private final boolean shouldTrim;
    private final String separator;

    private int columnIndex;
    private String currentPath;
    private Row row;

    public Converter(ConversionConfig config) {
        this.columns = config.getColumns();
        this.itemName = config.getRowItemName();
        this.shouldJoin = config.shouldJoin();
        this.shouldTrim = config.shouldTrim();
        this.separator = config.getSeparator();
        this.valuesConverter = new ValuesConverter(separator);
    }

    public String convert(String input) {
        InputStream inputStream = IOUtils.toInputStream(input, UTF8);
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
            try (Writer writer = Files.newBufferedWriter(outputFile, UTF8)) {
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
        writer.handler(line);
    }

    private void writeData(InputStream inputStream, LineHandler lineHandler) {
        try {
            final XMLStreamReader reader = inputStreamConverter.toXmlStreamReader(inputStream);
            try {
                currentPath = "";
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
        String name = xmlStreamReaderConverter.toLocalName(reader);
        currentPath += "/" + name;
        String toFind = currentPath.replace(itemName + "/", "");
        if (columns.containsKey(toFind)) {
            columnIndex = columns.get(toFind);
        } else if (toFind.contains("@")) {
            toFind = toFind.substring(0, toFind.indexOf("["));
            if (columns.containsKey(toFind))
                columnIndex = columns.get(toFind);
        }
        if (currentPath.equals(itemName)) {
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

    private void handleEndElement(XMLStreamReader reader, LineHandler lineHandler) {
        if (currentPath.equals(itemName)) {
            String line = valuesConverter.toLine(row.getValues());
            lineHandler.handler(line);
        }
        columnIndex = -1;
        if (!StringUtils.isEmpty(currentPath)) {
            int startIndex = currentPath.lastIndexOf("/" + reader.getLocalName());
            currentPath = currentPath.substring(0, startIndex);
        }
    }

}
