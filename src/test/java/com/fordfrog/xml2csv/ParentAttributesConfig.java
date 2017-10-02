package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Map;

public class ParentAttributesConfig implements ConversionConfig {

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap(Arrays.asList("item[@type='customer']/value1","item[@type='customer']/value2"));
    }

    @Override
    public String getSeparator() {
        return DEFAULT_SEPARATOR;
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
        return "/root";
    }

}
