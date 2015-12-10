package com.xmas.entity;

public enum MimeType {

    TEXT("text/plain");

    private String value;

    MimeType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
