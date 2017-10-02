package com.fordfrog.xml2csv;

import java.util.*;

public class DefaultConversionConfig implements ConversionConfig {

    private final Map<String, Integer> columns;
    private final String separator;
    private final boolean trim;
    private final boolean join;
    private final String itemName;

    public DefaultConversionConfig(DefaultConversionConfigBuilder builder) {
        this.columns = builder.columns;
        this.separator = builder.separator;
        this.trim = builder.trim;
        this.join = builder.join;
        this.itemName = builder.itemName;
    }

    @Override
    public Map<String, Integer> getColumns() {
        return columns;
    }

    @Override
    public String getSeparator() {
        return separator;
    }

    @Override
    public boolean shouldTrim() {
        return trim;
    }

    @Override
    public boolean shouldJoin() {
        return join;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public static class DefaultConversionConfigBuilder {

        private Map<String, Integer> columns;
        private String separator = DEFAULT_SEPARATOR;
        private boolean trim = false;
        private boolean join = false;
        private String itemName;

        public DefaultConversionConfigBuilder setColumns(List<String> columns) {
            this.columns = ColumnsConverter.toMap(columns);
            return this;
        }

        public DefaultConversionConfigBuilder setSeparator(String separator) {
            this.separator = separator;
            return this;
        }

        public DefaultConversionConfigBuilder setTrim(boolean trim) {
            this.trim = trim;
            return this;
        }

        public DefaultConversionConfigBuilder setJoin(boolean join) {
            this.join = join;
            return this;
        }

        public DefaultConversionConfigBuilder setItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public ConversionConfig build() {
            return new DefaultConversionConfig(this);
        }

    }

}
