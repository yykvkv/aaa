package com.fordfrog.xml2csv;

import java.util.ArrayList;
import java.util.List;

public class DefaultConversionConfig implements ConversionConfig {

    private final List<String> columns;
    private final char separator;
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
    public List<String> getColumns() {
        return columns;
    }

    @Override
    public char getSeparator() {
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

        private List<String> columns = new ArrayList<>();
        private char separator = ';';
        private boolean trim = false;
        private boolean join = false;
        private String itemName;

        public DefaultConversionConfigBuilder setColumns(List<String> columns) {
            this.columns = columns;
            return this;
        }

        public DefaultConversionConfigBuilder setSeparator(char separator) {
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
