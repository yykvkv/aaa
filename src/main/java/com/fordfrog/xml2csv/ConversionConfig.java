package com.fordfrog.xml2csv;

import java.util.Map;

public interface ConversionConfig {

    Map<String, Integer> getColumns();

    char getSeparator();

    boolean shouldTrim();

    boolean shouldJoin();

    String getItemName();

}
