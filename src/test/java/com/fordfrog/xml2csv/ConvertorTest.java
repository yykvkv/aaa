package com.fordfrog.xml2csv;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.Assert;
import org.junit.Test;
import uk.co.mruoc.properties.ClasspathFileContentLoader;
import uk.co.mruoc.properties.FileContentLoader;

public class ConvertorTest {

    private final FileContentLoader contentLoader = new ClasspathFileContentLoader();

    public void testConvertSimple() throws Throwable {
        final String inputFile = "/input-simple.xml";
        final String outputFile = "/output-simple.csv";
        final String[] columns = new String[]{"value1", "value2", "value3"};
        final Writer writer = new StringWriter();

        Convertor.convert(this.getClass().getResourceAsStream(inputFile),
                writer, columns, null, null,
                ';', false, false, "/root/item");

        final String expected = contentLoader.loadContent(outputFile);
        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testConvertNewLines() throws Throwable {
        final Writer writer = new StringWriter();

        Convertor.convert(new ByteArrayInputStream("<r><i><v>1\n1</v></i></r>".
                getBytes()), writer, new String[]{"v"}, null, null, ';', false,
                false, "/r/i");

        Assert.assertEquals("\"v\"\n\"1\n1\"\n", writer.toString());
    }

    @Test
    public void testConvertNewLinesBetweenXMLEscape() throws Throwable {
        final Writer writer = new StringWriter();

        Convertor.convert(new ByteArrayInputStream(
                "<r><i><v>&lt;p /&gt;\n&lt;p /&gt;</v></i></r>".getBytes()),
                writer, new String[]{"v"}, null, null, ';', false, false, "/r/i");

        Assert.assertEquals("\"v\"\n\"<p />\n<p />\"\n", writer.toString());
    }

    @Test
    public void testConvertTrimValues() throws Throwable {
        final String inputFile = "/input-simple.xml";
        final String outputFile = "/output-trim.csv";
        final Writer writer = new StringWriter();

        Convertor.convert(this.getClass().getResourceAsStream(inputFile),
                writer, new String[]{"value1", "value2", "value3"}, null, null,
                ';', true, false, "/root/item");

        final String expected = contentLoader.loadContent(outputFile);
        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testConvertMutipleSelectFirst() throws Throwable {
        final String inputFile = "/input-multiple.xml";
        final String outputFile = "/output-multiple-select-first.csv";
        final Writer writer = new StringWriter();

        Convertor.convert(this.getClass().getResourceAsStream(inputFile),
                writer, new String[]{"value1", "value2", "value3"}, null, null,
                ',', false, false, "/root/item");

        final String expected = contentLoader.loadContent(outputFile);

        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testConvertMultipleJoin() throws Throwable {
        final String inputFile = "/input-multiple.xml";
        final String outputFile = "/output-multiple-join.csv";
        final Writer writer = new StringWriter();

        Convertor.convert(this.getClass().getResourceAsStream(inputFile),
                writer, new String[]{"value1", "value2", "value3"}, null, null,
                ',', false, true, "/root/item");

        final String expected = contentLoader.loadContent(outputFile);

        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testConvertDeep() throws Throwable {
        final String inputFile = "/input-deep.xml";
        final String outputFile = "/output-deep.csv";
        final Writer writer = new StringWriter();

        Convertor.convert(this.getClass().getResourceAsStream(inputFile),
                writer, new String[]{"value1", "value2", "value3"}, null, null,
                ';', false, false, "/root/item0/item1/item2");

        final String expected = contentLoader.loadContent(outputFile);

        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testConvertHierarchy() throws Throwable {
        final String inputFile = "/input-hierarchy.xml";
        final String outputFile = "/output-hierarchy.csv";
        final Writer writer = new StringWriter();

        Convertor.convert(this.getClass().getResourceAsStream(inputFile),
                writer, new String[]{"header/value1", "body/value3",
                    "body/value4/value41", "body/value4/value42"}, null, null,
                ',', false, false, "/root/item");

        final String expected = contentLoader.loadContent(outputFile);

        Assert.assertEquals(expected, writer.toString());
    }
}
