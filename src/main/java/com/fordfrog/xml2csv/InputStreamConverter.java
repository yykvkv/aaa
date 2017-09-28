package com.fordfrog.xml2csv;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class InputStreamConverter {

    public XMLStreamReader toXmlStreamReader(InputStream inputStream) {
        try {
            final XMLInputFactory xMLInputFactory = XMLInputFactory.newInstance();
            return xMLInputFactory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            throw new Xml2CsvException(e);
        }
    }

}
