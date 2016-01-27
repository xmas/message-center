package com.xmas.service.questions.datasource;

import com.xmas.service.questions.DataSource;

/**
 * It's a fake data source class that represents nothing
 * Some questions can get data by script
 * and it don't need any data source
 */
public class NoneDataSource implements DataSource{

    @Override
    public byte[] getData() {
        return null;
    }
}
