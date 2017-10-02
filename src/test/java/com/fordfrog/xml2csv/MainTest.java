package com.fordfrog.xml2csv;

import org.junit.Test;
import uk.co.mruoc.properties.ClasspathFileContentLoader;
import uk.co.mruoc.properties.FileContentLoader;
import uk.co.mruoc.properties.FileSystemFileContentLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    private final FileContentLoader contentLoader = new ClasspathFileContentLoader();

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
