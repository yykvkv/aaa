package com.fordfrog.xml2csv;

import javax.xml.stream.XMLStreamReader;

public class XmlStreamReaderConverter {

    public String toLocalName(XMLStreamReader reader) {
        StringBuilder name = new StringBuilder(reader.getLocalName());
        if (reader.getAttributeCount() > 0)
            for (int a = 0; a < reader.getAttributeCount(); a++)
                name.append("[@").append(reader.getAttributeLocalName(a)).append("='").append(reader.getAttributeValue(a)).append("']");
        return name.toString();
    }

}
