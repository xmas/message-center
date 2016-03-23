package com.xmas.service.notifiers.safari.pushpackage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ZipCreatorTest {


    ZipCreator zipCreator = new ZipCreator();

    @Test
    public void createAuthenticationTokenTest(){


        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            Long l  = random.nextLong();
            assertTrue(zipCreator.createAuthenticationToken(l).length() >= 16);
            assertEquals(zipCreator.encodeUserGUID(zipCreator.createAuthenticationToken(l)), l);
        }

        for (int i = 1; i < 1000; i++) {
            String token = zipCreator.createAuthenticationToken((long) i);
            System.out.println(i + " = " + token);
            assertTrue(token.length() >= 16);
        }
    }



}