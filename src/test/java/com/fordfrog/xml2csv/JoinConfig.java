package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Map;

public class JoinConfig implements ConversionConfig {

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap(Arrays.asList("value1", "value2", "value3"));
    }

    @Override
    public String getSeparator() {
        return COMMA;
    }

    @Override
    public boolean shouldTrim() {
        return false;
    }

    @Override
    public boolean shouldJoin() {
        return true;
    }

    @Override
    public String getItemName() {
        return "/root/item";
    }

}
