package com.fordfrog.xml2csv;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StubPathProvider implements PathProvider {

    @Override
    public Path getInputFilePath() {
        return Paths.get(".","test-files/input-simple.xml");
    }

    @Override
    public Path getOutputFilePath() {
        return Paths.get(".", "test-files/output-simple.csv");
    }
}
