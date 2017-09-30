package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Map;

public class SimpleConfig implements ConversionConfig {

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap(Arrays.asList("value1", "value2", "value3"));
    }

    @Override
    public char getSeparator() {
        return ';';
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
    public String getItemName() {
        return "/root/item";
    }

}
