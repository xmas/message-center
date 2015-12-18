package com.xmas.notifiers.safari.pushpackage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ZipCreatorTest {


    ZipCreator zipCreator = new ZipCreator();

    @Test
    public void createAuthenticationTokenTest(){
        assertEquals(zipCreator.createAuthenticationToken(123456789L), ("MDAwMDEyMzQ1Njc4OQ=="));
    }



}