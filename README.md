# Xml To Csv

Simple XML to CSV conversion utility.

## What does it do?

It converts any XML file to CSV:

    <root>
        <item>
            <subitem1>
                <value1>...</value1>
                <value2>...</value2>
            </subitem1>
            <subitem2>
                <value3>...</value3>
            </subitem2>
        </item>
        <item>
            <subitem1>
                <value1>...</value1>
            </subitem1>
            <subitem2>
                <value3>...</value3>
            </subitem2>
        </item>
        ...
    </root>

Choose any XML element using XPath expression in order to select XML elements
for conversion to CSV file. Only child elements that match the expression will
be converted.

### XPath Support

The application does not comprehensively support all xpath queries. You can do simple
matching on element names, e.g. from the above example you could select value three using
/root/item/subitem2/value3. You can also select specific elements using attributes
that are attached, however if you do this, you must specify all attributes on the element
rather than just one, this is a current limitation of the implementation.

### Encoding

The application expects all files to be UTF-8 encoded

## Usage

To use the library from a program you will need to add a dependency to your project. In
gradle you would do this by adding the following to your build.gradle file:

```
dependencies {
    compile 'com.github.michaelruocco:xml-to-csv:1.0.0'
}
```

Once you have the dependency in you need to about building you conversion config.
You can either use the DefaultConversionConfig and its builder class and pass it the
information it needs, or you can implement the ConversionConfig interface and provide
your own custom reusable config. Equivalent examples of both usage patterns are shown
below:

### Using DefaultConversionConfig

```
DefaultConversionConfigBuilder builder = new DefaultConversionConfigBuilder()
    .setColumns(Arrays.asList("value1", "value2", "value3")) //sets columns to extract
    .setSeparator("~") //sets field separator
    .setTrim(true) //trims field values (defaults to false)
    .setJoin(true) //joins duplicate field values (defaults to false)
    .setRowItemName("/root/item") //row item name
    .build();
ConversionConfig config = builder.build();
Converter converter = new Converter(config);
...
```

### Implementing ConversionConfig

```
package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Map;

public class SimpleConfig implements ConversionConfig {

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap("value1, value2, value3");
    }

    @Override
    public String getSeparator() {
        return TILDE;
    }

    @Override
    public boolean shouldTrim() {
        return true;
    }

    @Override
    public boolean shouldJoin() {
        return true;
    }

    @Override
    public String getRowItemName() {
        return "/root/item";
    }

}
```

Then once you have defined your config, you can use it like
this:

```
...
ConversionConfig config = new SimpleConversionConfig();
Converter converter = new Converter(config);

// to return the csv content into a variable you can do
String csvContent = converter.convert(xmlContent);

// to input from and output to a file you can do
converter.convert(Paths.get("/input/file.xml"), Paths.get("/output/file.csv"));
```

Add more details here.

## Running Standalone

You can also run the library as a standalone application from the command line, the usage
instructions are listed below:

    Usage: java -jar xml-to-csv-*.jar --columns <columns> --inputFile <file> --outputFile <file> --row-item-name <xpath>

    Command line switches:

    -c,--columns <arg>          List of comma separated columns that should be 
                                output to the CSV file. These names must correspond
                                to the element names within the item element.
    -i,--inputFile <arg>        Path to the input XML file. Input file content
                                should always be in UTF-8 encoding.
    -j,--join                   Join values of duplicated elements into single
                                value using comma as a separator. By default
                                value of all duplicated elements are saved to
                                CSV.
    -o,--outputFile <arg>       Path to the output CSV file. Output file
                                content is always in UTF-8 encoding.
    -r,--row-item-name <arg>    XPath which refers to XML element which will
                                be converted to a row.
    -s,--separator <arg>        Character that should be used to separate
                                fields. Default value is semi-colon.
    -t,--trim                   Trim values. By default values are not
                                trimmed.
                                
                                
## Running the Tests

You can run the tests for this project by running the following command:

```
gradlew clean build
```

## Checking dependencies

You can check the current dependencies used by the project to see whether
or not they are currently up to date by running the following command:

```
gradlew dependencyUpdates
```