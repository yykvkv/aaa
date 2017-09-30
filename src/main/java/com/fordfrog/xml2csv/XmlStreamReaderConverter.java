package com.fordfrog.xml2csv;

import javax.xml.stream.XMLStreamReader;

public class XmlStreamReaderConverter {

    public String toLocalName(XMLStreamReader reader) {
        StringBuilder name = new StringBuilder(reader.getLocalName());
        if (reader.getAttributeCount() > 0)
            for (int a = 0; a < reader.getAttributeCount(); a++)
                name.append(toString(reader, a));
        return name.toString();
    }

    private static String toString(XMLStreamReader reader, int attributeIndex) {
        StringBuilder s = new StringBuilder();
        s.append("[@");
        s.append(reader.getAttributeLocalName(attributeIndex));
        s.append("='");
        s.append(reader.getAttributeValue(attributeIndex));
        s.append("']");
        return s.toString();
    }

}
