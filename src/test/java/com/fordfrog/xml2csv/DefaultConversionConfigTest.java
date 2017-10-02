package com.fordfrog.xml2csv;

import com.fordfrog.xml2csv.DefaultConversionConfig.DefaultConversionConfigBuilder;
import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultConversionConfigTest {

    private final DefaultConversionConfigBuilder builder = new DefaultConversionConfigBuilder();

    @Test
    public void shouldReturnColumns() {
        builder.setColumns(Arrays.asList("column1", "column2"));

        ConversionConfig config = builder.build();

        Map<String, Integer> columns = config.getColumns();
        assertThat(columns).containsExactly(new SimpleEntry<>("column1", 0), new SimpleEntry<>("column2", 1));
    }

    @Test
    public void separatorShouldDefaultToSemiColon() {
        ConversionConfig config = builder.build();

        assertThat(config.getSeparator()).isEqualTo(";");
    }

    @Test
    public void shouldReturnSeparator() {
        String separator = "~";
        builder.setSeparator(separator);

        ConversionConfig config = builder.build();

        assertThat(config.getSeparator()).isEqualTo(separator);
    }

    @Test
    public void trimShouldDefaultToFalse() {
        ConversionConfig config = builder.build();

        assertThat(config.shouldTrim()).isFalse();
    }

    @Test
    public void shouldReturnTrim() {
        builder.setTrim(true);

        ConversionConfig config = builder.build();

        assertThat(config.shouldTrim()).isTrue();
    }

    @Test
    public void joinShouldDefaultToFalse() {
        ConversionConfig config = builder.build();

        assertThat(config.shouldJoin()).isFalse();
    }

    @Test
    public void shouldReturnJoin() {
        builder.setJoin(true);

        ConversionConfig config = builder.build();

        assertThat(config.shouldJoin()).isTrue();
    }

    @Test
    public void shouldReturnItemName() {
        String itemName = "/item/name";
        builder.setItemName(itemName);

        ConversionConfig config = builder.build();

        assertThat(config.getRowItemName()).isEqualTo(itemName);
    }

}
