# Xml To Csv

Simple XML to CSV conversion utility.

## What it does exactly?

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

Add more details here.

## Running Standalone

Here is the usage information that xml-to-csv outputs if run without parameters:

    Usage: java -jar xml2csv-*.jar --columns <columns> --input <file> --output <file> --item-name <xpath>

    General command line switches:

    --columns <columns>
        List of columns that should be output to the CSV file. These names must
        correspond to the element names within the item element.
    --input <file>
        Path to the input XML file.
    --item-name
        XPath which refers to XML element which will be converted to a row. It cannot
        end with slash (/).
    --join
        Join values of multiple elements into single value using (, ) as a separator.
        By default value of the first element is saved to CSV.
    --output <file>
        Path to the output CSV file. Output file content is always in UTF-8 encoding.
    --separator <character>
        Character that should be used to separate fields. Default value is (;).
    --trim
        Trim values. By default values are not trimmed.