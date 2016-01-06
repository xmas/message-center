package com.xmas.R.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RequestDirectoriesProcessorTest {

    @InjectMocks
    RequestDirectoriesProcessor processor;

    @Test
    public void testCreateDirectoryForRequest() throws Exception {

    }

    @Test
    public void testGetDirectoryName() throws Exception {
        Thread[] threads = new Thread[100];
        Map<String, Boolean> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++)
                    map.put(processor.getDirectoryName(), false);
            });
        }

        for (int i = 0; i < 100; i++) {
            threads[i].start();
        }

        Thread.sleep(2000);

        assertEquals(1000, map.size());
    }
}