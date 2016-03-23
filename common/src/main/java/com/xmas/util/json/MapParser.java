package com.xmas.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.exceptions.BadRequestException;

import java.io.IOException;
import java.util.Map;

public class MapParser {

    @SuppressWarnings("unchecked")
    public static Map<String, String> parseScriptArgs(String string){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(string, Map.class);
        } catch (IOException e) {
            throw new BadRequestException("Can't parse string \"" + string + "\" as script arguments.", e);
        }
    }
}
