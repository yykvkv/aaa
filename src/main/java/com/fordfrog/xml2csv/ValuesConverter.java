package com.fordfrog.xml2csv;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class ValuesConverter {

    private final String separator;

    public ValuesConverter(String separator) {
        this.separator = separator;
    }

    public String toLine(Collection<String> values) {
        return StringUtils.join(values, separator);
    }

}
