package com.fordfrog.xml2csv;

import java.util.ArrayList;
import java.util.List;

public class PathElement {

    private String name;
    private List<Attribute> attributes = new ArrayList<>();

    public int getAttributeCount() {
        return attributes.size();
    }

    public Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    public String getName() {
        return name;
    }

}
