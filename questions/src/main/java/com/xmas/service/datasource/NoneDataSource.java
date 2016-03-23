package com.xmas.service.datasource;

import com.xmas.service.DataSource;

/**
 * It's a fake data source class that represents nothing
 * Some questions can get data by script
 * and it don't need any data source
 */
public class NoneDataSource implements DataSource {

    @Override
    public byte[] getData() {
        return null;
    }
}
