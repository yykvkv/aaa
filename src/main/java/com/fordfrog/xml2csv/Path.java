package com.fordfrog.xml2csv;

import java.util.ArrayDeque;
import java.util.Deque;

public class Path {

    private Deque<PathElement> elements = new ArrayDeque<>();

    public void add(PathElement element) {
        elements.push(element);
    }

    public void remove(PathElement element) {
        elements.remove(element);
    }

    public String toBasicPath() {
        StringBuilder path = new StringBuilder("/");
        for (PathElement element : elements) {
            path.append(element.getName());
            path.append("/");
        }
        return path.toString();
    }

    /*public String toAttributePath() {
        StringBuilder path = new StringBuilder("/");
        for (PathElement element : elements) {
            path.append(element.getName());
            path.append("/");
            for (int a = 0; a < element.getAttributeCount(); a++) {

            }
            StringBuilder name = new StringBuilder(reader.getLocalName());
            if (reader.getAttributeCount() > 0)
                for (int a = 0; a < reader.getAttributeCount(); a++)
                    name.append("[@").append(reader.getAttributeLocalName(a)).append("='").append(reader.getAttributeValue(a)).append("']");
            return name.toString();
        }
        return path.toString();
    }*/
}
