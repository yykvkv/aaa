package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class NewLinesConfig implements ConversionConfig {

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap(Collections.singletonList("v"));
    }

    @Override
    public String getSeparator() {
        return SEMI_COLON;
    }

    @Override
    public boolean shouldTrim() {
        return false;
    }

    @Override
    public boolean shouldJoin() {
        return false;
    }

    @Override
    public String getRowItemName() {
        return "/r/i";
    }

}
