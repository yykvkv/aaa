package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fordfrog.xml2csv.DefaultConversionConfig.DefaultConversionConfigBuilder;
import org.junit.Assert;
import org.junit.Test;
import uk.co.mruoc.properties.ClasspathFileContentLoader;
import uk.co.mruoc.properties.FileContentLoader;

public class ConvertorTest {

    private final FileContentLoader contentLoader = new ClasspathFileContentLoader();

    @Test
    public void testConvertSimple() throws Throwable {
        String input = contentLoader.loadContent("/input-simple.xml");
        String expected = contentLoader.loadContent("/output-simple.csv");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Arrays.asList("value1", "value2", "value3"))
                .setItemName("/root/item")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConvertTrimValues() throws Throwable {
        String input = contentLoader.loadContent("/input-simple.xml");
        String expected = contentLoader.loadContent("/output-trim.csv");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Arrays.asList("value1", "value2", "value3"))
                .setItemName("/root/item")
                .setTrim(true)
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConvertMultipleSelectFirst() throws Throwable {
        String input = contentLoader.loadContent("/input-multiple.xml");
        String expected = contentLoader.loadContent("/output-multiple-select-first.csv");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Arrays.asList("value1", "value2", "value3"))
                .setItemName("/root/item")
                .setSeparator(',')
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConvertMultipleJoin() throws Throwable {
        String input = contentLoader.loadContent("/input-multiple.xml");
        String expected = contentLoader.loadContent("/output-multiple-join.csv");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Arrays.asList("value1", "value2", "value3"))
                .setItemName("/root/item")
                .setSeparator(',')
                .setJoin(true)
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConvertDeep() throws Throwable {
        String input = contentLoader.loadContent("/input-deep.xml");
        String expected = contentLoader.loadContent("/output-deep.csv");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Arrays.asList("value1", "value2", "value3"))
                .setItemName("/root/item0/item1/item2")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

    public void testConvertHierarchy() throws Throwable {
        String input = contentLoader.loadContent("/input-hierarchy.xml");
        String expected = contentLoader.loadContent("/output-hierarchy.csv");
        List<String> columns = Arrays.asList("header/value1",
                "body/value3",
                "body/value4/value41",
                "body/value4/value42");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(columns)
                .setItemName("/root/item")
                .setSeparator(',')
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConvertNewLines() throws Throwable {
        String input = "<r><i><v>1\n1</v></i></r>";
        String expected = "\"v\"\n\"1\n1\"\n";
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Collections.singletonList("v"))
                .setItemName("/r/i")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConvertNewLinesBetweenXMLEscape() throws Throwable {
        String input = "<r><i><v>&lt;p /&gt;\n&lt;p /&gt;</v></i></r>";
        String expected = "\"v\"\n\"<p />\n<p />\"\n";
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Collections.singletonList("v"))
                .setItemName("/r/i")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        Assert.assertEquals(expected, output);
    }

}
