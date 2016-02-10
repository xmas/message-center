package com.xmas.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NonePredefinedAttributesConverterTest {

    private NonePredefinedAttributesConverter converter = new NonePredefinedAttributesConverter();

    @Test
    public void testConvertToDatabaseColumn() throws Exception {
        assertEquals("125", converter.convertToDatabaseColumn(125));
        assertEquals("125.3", converter.convertToDatabaseColumn(125.3));
        assertEquals("true", converter.convertToDatabaseColumn(true));
        assertEquals("test", converter.convertToDatabaseColumn("test"));
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        assertEquals(125L, converter.convertToEntityAttribute("125"));
        assertEquals(125.3, converter.convertToEntityAttribute("125.3"));
        assertEquals(true, converter.convertToEntityAttribute("TRUE"));
        assertEquals("test", converter.convertToEntityAttribute("test"));
    }
}