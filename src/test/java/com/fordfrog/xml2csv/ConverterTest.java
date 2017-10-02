package com.fordfrog.xml2csv;

import org.junit.Test;
import uk.co.mruoc.properties.ClasspathFileContentLoader;
import uk.co.mruoc.properties.FileContentLoader;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

    private final FileContentLoader contentLoader = new ClasspathFileContentLoader();

    @Test
    public void testConvertSimple() throws Throwable {
        String input = contentLoader.loadContent("/input-simple.xml");
        String expected = contentLoader.loadContent("/output-simple.csv");
        Converter converter = new Converter(new SimpleConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertTrimValues() throws Throwable {
        String input = contentLoader.loadContent("/input-simple.xml");
        String expected = contentLoader.loadContent("/output-trim.csv");
        Converter converter = new Converter(new TrimConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertMultipleSelectAll() throws Throwable {
        String input = contentLoader.loadContent("/input-multiple.xml");
        String expected = contentLoader.loadContent("/output-multiple-select-all.csv");
        Converter converter = new Converter(new SimpleConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertMultipleJoin() throws Throwable {
        String input = contentLoader.loadContent("/input-multiple.xml");
        String expected = contentLoader.loadContent("/output-multiple-join.csv");
        Converter converter = new Converter(new JoinConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertDeep() throws Throwable {
        String input = contentLoader.loadContent("/input-deep.xml");
        String expected = contentLoader.loadContent("/output-deep.csv");
        Converter converter = new Converter(new DeepConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertHierarchy() throws Throwable {
        String input = contentLoader.loadContent("/input-hierarchy.xml");
        String expected = contentLoader.loadContent("/output-hierarchy.csv");
        Converter converter = new Converter(new HierarchyConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertAttributes() throws Throwable {
        String input = contentLoader.loadContent("/input-attributes.xml");
        String expected = contentLoader.loadContent("/output-attributes.csv");
        Converter converter = new Converter(new AttributesConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertParentAttributes() throws Throwable {
        String input = contentLoader.loadContent("/input-parent-attributes.xml");
        String expected = contentLoader.loadContent("/output-parent-attributes.csv");
        Converter converter = new Converter(new ParentAttributesConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertNewLines() throws Throwable {
        String input = "<r><i><v>1\n1</v></i></r>";
        String expected = "v\n1\n1\n";
        Converter converter = new Converter(new NewLinesConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertNewLinesBetweenXmlEscape() throws Throwable {
        String input = "<r><i><v>&lt;p /&gt;\n&lt;p /&gt;</v></i></r>";
        String expected = "v\n<p />\n<p />\n";
        Converter converter = new Converter(new NewLinesConfig());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testHandleRealWorldScenarioArm2() throws Throwable {
        String input = contentLoader.loadContent("/input-real-arm2.xml");
        String expected = contentLoader.loadContent("/output-real-arm2.csv");
        Converter converter = new Converter(new Arm2Config());

        String output = converter.convert(input);

        assertThat(output).isEqualTo(expected);
    }

}
