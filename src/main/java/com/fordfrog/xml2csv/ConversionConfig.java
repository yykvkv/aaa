package com.fordfrog.xml2csv;

import java.util.List;

public interface ConversionConfig {

    List<String> getColumns();

    char getSeparator();

    boolean shouldTrim();

    boolean shouldJoin();

    String getItemName();

}
