package com.fordfrog.xml2csv;

import java.nio.file.Path;

public interface PathProvider {

    Path getInputFilePath();

    Path getOutputFilePath();

}
