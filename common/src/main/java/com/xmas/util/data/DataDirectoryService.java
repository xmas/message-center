package com.xmas.util.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DataDirectoryService {

    @Value("${data.directory}")
    private String dataDir;

    public Path getDataDirectory(){
        return Paths.get(dataDir);
    }
}
