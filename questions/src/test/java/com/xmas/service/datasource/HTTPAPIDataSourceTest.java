package com.xmas.service.datasource;

import com.xmas.service.DataSource;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class HTTPAPIDataSourceTest {

    public static final String API_URI = "https://schemata.io/query/query?queryToken=PSLXNBJ9EJPIJM2I1GRSKEPBVGF4H338HY6XSN3Q";

    DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new HTTPAPIDataSource(API_URI);
    }

    @Test
    public void testGetData() throws Exception {
        byte[] data = dataSource.getData();

        assertNotNull(data);
        assertFalse(Arrays.equals(data, new byte[data.length]));
    }
}