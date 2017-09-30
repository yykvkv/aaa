package com.fordfrog.xml2csv;

import java.util.Collection;

public interface CsvWriter {

    void write(Row row);

    void write(Collection<String> values);

}
