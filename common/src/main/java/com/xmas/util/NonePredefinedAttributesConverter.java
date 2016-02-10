package com.xmas.util;

import javax.persistence.AttributeConverter;

public class NonePredefinedAttributesConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        return String.valueOf(attribute);
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            return toLong(dbData);
        } catch (NumberFormatException le){
            try {
                return toDouble(dbData);
            } catch (NumberFormatException de){
                try {
                    return toBoolean(dbData);
                } catch (IllegalArgumentException be){
                    return dbData;
                }
            }
        }
    }

    private Long toLong(String s){
        return Long.valueOf(s);
    }

    private Double toDouble(String s){
        return Double.valueOf(s);
    }

    private Boolean toBoolean(String s){
        if(s.trim().equalsIgnoreCase("true")) return true;
        if(s.trim().equalsIgnoreCase("false")) return false;
        throw new IllegalArgumentException("Cant parse as boolean");
    }
}
