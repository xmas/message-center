package com.xmas.entity;

public enum Medium {
    CHROME("chrome"),
    SAFARI("safari");

    private String name;

    Medium(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
