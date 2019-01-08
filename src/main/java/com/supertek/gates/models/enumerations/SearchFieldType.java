package com.supertek.gates.models.enumerations;

public enum SearchFieldType {

    NAME ("Name"),
    ID ("ID");

    private final String name;

    SearchFieldType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
