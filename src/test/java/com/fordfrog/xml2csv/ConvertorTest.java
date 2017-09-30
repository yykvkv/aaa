package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fordfrog.xml2csv.DefaultConversionConfig.DefaultConversionConfigBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.mruoc.properties.ClasspathFileContentLoader;
import uk.co.mruoc.properties.FileContentLoader;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(output).isEqualTo(expected);
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

        assertThat(output).isEqualTo(expected);
    }

    @Test
    @Ignore
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

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertMultipleSelectAll() throws Throwable {
        String input = contentLoader.loadContent("/input-multiple.xml");
        String expected = contentLoader.loadContent("/output-multiple-select-all.csv");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Arrays.asList("value1", "value2", "value3"))
                .setItemName("/root/item")
                .setSeparator(',')
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        assertThat(output).isEqualTo(expected);
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

        assertThat(output).isEqualTo(expected);
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

        assertThat(output).isEqualTo(expected);
    }

    @Test
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

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertAttributes() throws Throwable {
        String input = contentLoader.loadContent("/input-attributes.xml");
        String expected = contentLoader.loadContent("/output-attributes.csv");
        List<String> columns = Arrays.asList("value[@type='user']","value[@type='customer']");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(columns)
                .setItemName("/root/item")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertParentAttributes() throws Throwable {
        String input = contentLoader.loadContent("/input-parent-attributes.xml");
        String expected = contentLoader.loadContent("/output-parent-attributes.csv");
        List<String> columns = Arrays.asList("item[@type='customer']/value1","item[@type='customer']/value2");
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(columns)
                .setItemName("/root")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertNewLines() throws Throwable {
        String input = "<r><i><v>1\n1</v></i></r>";
        String expected = "v\n1\n1\n";
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Collections.singletonList("v"))
                .setItemName("/r/i")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testConvertNewLinesBetweenXMLEscape() throws Throwable {
        String input = "<r><i><v>&lt;p /&gt;\n&lt;p /&gt;</v></i></r>";
        String expected = "v\n<p />\n<p />\n";
        ConversionConfig config = new DefaultConversionConfigBuilder()
                .setColumns(Collections.singletonList("v"))
                .setItemName("/r/i")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void testHandleRealWorldScenario() throws Throwable {
        String input = contentLoader.loadContent("/input-real.xml");
        String expected = contentLoader.loadContent("/output-real.csv");
        List<String> columns = Arrays.asList(
                "Brand",
                "PayerID",
                "CustomerID",
                "InvoiceNumber",
                "TransactionDate",
                "Contact[@type='Customer']/Name",
                "Contact[@type='Customer']/TelephoneNumber",
                "CostCentre",
                "TransactionAmount",
                "TransactionType",
                "CustomerName",
                "Address[@type='Customer']/AddressLine[@sequence='1']",
                "Address[@type='Customer']/AddressLine[@sequence='2']",
                "Address[@type='Customer']/PostalCode",
                "CreditStatus",
                "DueDate",
                "TransactionNetAmount",
                "POAReference",
                "TransactionID",
                "Region",
                //email
                //credit control clerk
                "LegalStatus"
        );
        ConversionConfig config = new DefaultConversionConfig.DefaultConversionConfigBuilder()
                .setColumns(columns)
                .setSeparator('~')
                .setTrim(true)
                .setItemName("/SyncTPAROpenItems[@schemaLocation='http://schema.infor.com/InforOAGIS/2 SyncTPAROpenItems.xsd'][@releaseID='9.2'][@versionID='2.0.1'][@systemEnvironmentCode='Production'][@languageCode='GB']/DataArea/TPAROpenItems[@type='Reconcilliation']/OpenItem")
                .build();
        Convertor convertor = new Convertor(config);

        String output = convertor.convert(input);

        assertThat(output).isEqualTo(expected);
    }

}
