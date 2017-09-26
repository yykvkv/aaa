package com.fordfrog.xml2csv;

import java.util.List;

public interface ConversionConfig {

    List<String> getColumns();

    Filters getFilters();

    Remappings getRemappings();

    char getSeparator();

    boolean shouldTrim();

    boolean shouldJoin();

    String getItemName();

}
