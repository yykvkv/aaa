package com.fordfrog.xml2csv;

import java.util.Map;

public interface ConversionConfig {

    String DEFAULT_SEPARATOR = ";";
    String COMMA = ",";
    String TILDE = "~";

    Map<String, Integer> getColumns();

    String getSeparator();

    boolean shouldTrim();

    boolean shouldJoin();

    String getItemName();

}
