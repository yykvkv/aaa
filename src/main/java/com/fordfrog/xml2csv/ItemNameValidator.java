package com.fordfrog.xml2csv;

public class ItemNameValidator {

    public boolean isValid(String itemName) {
        if (itemName.trim().isEmpty())
            throw new IllegalArgumentException("item name cannot be empty");

        if (itemName.endsWith("/"))
            throw new IllegalArgumentException("itemName cannot end with a slash (/)");

        return true;
    }

}
