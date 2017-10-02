package com.fordfrog.xml2csv;

import java.util.Map;

public interface ConversionConfig {

    String SEMI_COLON = ";";
    String COMMA = ",";
    String TILDE = "~";

    Map<String, Integer> getColumns();

    String getSeparator();

    boolean shouldTrim();

    boolean shouldJoin();

    String getRowItemName();

}
