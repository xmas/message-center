package com.xmas.R.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class RService {

    @Autowired
    RConnectionManager connectionManager;



    protected String loadScript(String scriptName) throws IOException {
        InputStream scriptResourceStream = this.getClass().getResource(scriptName).openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(scriptResourceStream));
        return reader.lines().reduce((acum, str) -> acum + "\n" + str).get();
    }


}
