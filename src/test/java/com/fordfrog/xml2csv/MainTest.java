package com.fordfrog.xml2csv;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import uk.co.mruoc.properties.ClasspathFileContentLoader;
import uk.co.mruoc.properties.FileContentLoader;
import uk.co.mruoc.properties.FileSystemFileContentLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private static final String NEW_LINE = System.lineSeparator();

    private final FileContentLoader contentLoader = new ClasspathFileContentLoader();

    @Test
    public void shouldPrintErrorIfItemNameNotProvided() {
        Main.main();

        assertThat(systemErrRule.getLog()).isEqualTo("row item name must be provided" + NEW_LINE);
        assertThat(systemOutRule.getLog()).isEqualToIgnoringWhitespace(loadUsageText());
    }

    @Test
    public void shouldPrintErrorIfColumnsNotProvided() {
        Main.main("-r", "/root/item");

        assertThat(systemErrRule.getLog()).isEqualTo("at least one column must be provided" + NEW_LINE);
        assertThat(systemOutRule.getLog()).isEqualToIgnoringWhitespace(loadUsageText());
    }

    @Test
    public void shouldPrintErrorIfInputFilePathNotProvided() {
        Main.main("-r", "/root/item",
                "-c", "value1, value2, value3");

        assertThat(systemErrRule.getLog()).isEqualTo("input file path must be provided" + NEW_LINE);
        assertThat(systemOutRule.getLog()).isEqualToIgnoringWhitespace(loadUsageText());
    }

    @Test
    public void shouldPrintErrorIfInputFilePathDoesNotExist() {
        Main.main("-r", "/root/item",
                "-c", "value1, value2, value3",
                "-i", "invalid/path");

        assertThat(systemErrRule.getLog()).isEqualTo("input file invalid/path does not exist" + NEW_LINE);
        assertThat(systemOutRule.getLog()).isEqualToIgnoringWhitespace(loadUsageText());
    }

    @Test
    public void shouldPrintErrorIfOutputFilePathNotProvided() {
        PathProvider pathProvider = new StubPathProvider();
        Path inputFilePath = pathProvider.getInputFilePath();

        Main.main("-r", "/root/item",
                "-c", "value1, value2, value3",
                "-i", inputFilePath.toString());

        assertThat(systemErrRule.getLog()).isEqualTo("output file path must be provided" + NEW_LINE);
        assertThat(systemOutRule.getLog()).isEqualToIgnoringWhitespace(loadUsageText());
    }

    @Test
    public void shouldPrintErrorIfOutputFilePathParentFolderDoesNotExist() {
        PathProvider pathProvider = new StubPathProvider();
        Path inputFilePath = pathProvider.getInputFilePath();

        Main.main("-r", "/root/item",
                "-c", "value1, value2, value3",
                "-i", inputFilePath.toString(),
                "-o", "invalid/path");

        assertThat(systemErrRule.getLog()).isEqualTo("output folder invalid does not exist or is not a directory" + NEW_LINE);
        assertThat(systemOutRule.getLog()).isEqualToIgnoringWhitespace(loadUsageText());
    }

    private String loadUsageText() {
        return contentLoader.loadContent("/usage.txt");
    }

    @Test
    public void testConvertSimple() throws IOException {
        PathProvider pathProvider = new StubPathProvider();
        Path inputFilePath = pathProvider.getInputFilePath();
        Path outputFilePath = pathProvider.getOutputFilePath();

        try {
            Main.main("-r", "/root/item",
                    "-c", "value1, value2, value3",
                    "-i", inputFilePath.toString(),
                    "-o", outputFilePath.toString());

            String expectedContent = contentLoader.loadContent("/output-simple.csv");
            String actualContent = loadFileSystemContent(outputFilePath);
            assertThat(actualContent).isEqualTo(expectedContent);
        } finally {
            deleteFileIfExists(outputFilePath);
        }
    }

    private String loadFileSystemContent(Path path) {
        FileContentLoader loader = new FileSystemFileContentLoader();
        return loader.loadContent(path.toString());
    }

    private void deleteFileIfExists(Path path) throws IOException {
        if (Files.exists(path))
            Files.delete(path);
    }

}
