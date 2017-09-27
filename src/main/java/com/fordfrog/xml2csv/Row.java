package com.fordfrog.xml2csv;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Row implements Iterable<String> {

    private final List<String> values;

    public Row(int numberOfColumns) {
        this.values = initialiseValues(numberOfColumns);
    }

    public void join(int index, String value) {
        String appendedValue = values.get(index);
        //System.out.println("join started |" + appendedValue + "| value |" + value + "| " + StringUtils.isEmpty(value) + " len " + value.length());
        if (!StringUtils.isEmpty(appendedValue))
            appendedValue += ", ";
        appendedValue += value;
        //System.out.println("join completed " + appendedValue);
        values.set(index, appendedValue);
    }

    public void append(int index, String value) {
        String appendedValue = values.get(index);
        appendedValue += value;
        values.set(index, appendedValue);
    }

    public void set(int index, String value) {
        values.set(index, value);
    }

    public String get(int index) {
        return values.get(index);
    }

    public int getNumberOfColumns() {
        return values.size();
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    @Override
    public String toString() {
        return values.toString();
    }

    private static List<String> initialiseValues(int numberOfColumns) {
        List<String> values = Arrays.asList(new String[numberOfColumns]);
        for (int i = 0; i < numberOfColumns; i++)
            values.set(i, "");
        return values;
    }

}
