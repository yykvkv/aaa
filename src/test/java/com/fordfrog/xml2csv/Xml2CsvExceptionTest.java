package com.fordfrog.xml2csv;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Xml2CsvExceptionTest {

    @Test
    public void shouldReturnCause() {
        Throwable cause = new Exception();

        Xml2CsvException exception = new Xml2CsvException(cause);

        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    public void shouldReturnMessage() {
        String message = "my message";

        Xml2CsvException exception = new Xml2CsvException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

}
