package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Map;

public class HierarchyConfig implements ConversionConfig {

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap(Arrays.asList(
                "header/value1",
                "body/value3",
                "body/value4/value41",
                "body/value4/value42")
        );
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
        return false;
    }

    @Override
    public String getRowItemName() {
        return "/root/item";
    }

}
