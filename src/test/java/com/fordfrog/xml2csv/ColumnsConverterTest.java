package com.fordfrog.xml2csv;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ColumnsConverterTest {

    @Test
    public void shouldConvertColumnsStringIntoMapWithColumnIndexes() {
        String columns = "column A, column B, column C";

        Map<String, Integer> map = ColumnsConverter.toMap(columns);

        System.out.println(map);
        assertThat(map.get("column A")).isEqualTo(0);
        assertThat(map.get("column B")).isEqualTo(1);
        assertThat(map.get("column C")).isEqualTo(2);
    }

    @Test
    public void shouldConvertColumnsListIntoMapWithColumnIndexes() {
        List<String> columns = Arrays.asList("column A", "column B", "column C");

        Map<String, Integer> map = ColumnsConverter.toMap(columns);

        System.out.println(map);
        assertThat(map.get("column A")).isEqualTo(0);
        assertThat(map.get("column B")).isEqualTo(1);
        assertThat(map.get("column C")).isEqualTo(2);
    }


}
