package com.fordfrog.xml2csv;

import java.util.List;

public interface CsvWriter {
    void write(Row row);

    void write(List<String> values);
}
