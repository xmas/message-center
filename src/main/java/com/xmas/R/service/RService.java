package com.xmas.R.service;

import com.xmas.dao.ScriptRepository;
import com.xmas.entity.Script;
import com.xmas.exceptions.ProcessingException;
import com.xmas.exceptions.ScriptEvaluationExceprion;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Random;

@Service
public class RService {

    private static final Logger logger = LogManager.getLogger();

    public static final String SCRIPTS_DIRECTORY = "/r/scripts/";

    @Autowired
    ServletContext servletContext;

    @Autowired
    ScriptEvaluatorService scriptEvaluator;

    @Autowired
    RequestDirectoriesProcessor directoriesProcessor;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    ScriptRepository scriptRepository;

    public Iterable<Script> getScripts(){
        return scriptRepository.findAll();
    }

    public Script getScript(Integer id){
        return scriptRepository.findOne(id);
    }

    public Script createScript(MultipartFile file){
        String scriptFileName = generateScriptFileName(file.getOriginalFilename());
        Script script = new Script(scriptFileName);
        script.setName(file.getOriginalFilename());
        saveScriptFile(scriptFileName, file);
        scriptRepository.save(script);
        return script;
    }

    public String evaluateScript(Integer id){
        String requestDirectory = directoriesProcessor.createDirectoriesForRequest().getPath();
        String script = loadScript(scriptRepository.findOne(id).getScriptFileName());

        scriptEvaluator.evaluateScript(script, requestDirectory);

        return requestDirectory;
    }

    protected String loadScript(String scriptName){
        try {
            InputStream scriptResourceStream = this.getClass().getResource(scriptName).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(scriptResourceStream));
            return reader.lines().reduce((acum, str) -> acum + "\n" + str).get();
        }catch (IOException ioe){
            throw new ScriptEvaluationExceprion(ioe);
        }
    }

    protected String generateScriptFileName(String rawName){
        Random random = new Random();

        return rawName + "_" + random.nextLong();
    }

    protected void saveScriptFile(String fileName, MultipartFile file){
        String appBaseFolder = servletContext.getRealPath("/");
        File scriptFile = new File(appBaseFolder + SCRIPTS_DIRECTORY + fileName);
        try {
            FileUtils.writeByteArrayToFile(scriptFile, file.getBytes());
        }catch (IOException ioe){
            logger.error(ioe.getMessage());
            logger.debug(ioe.getMessage(), ioe);
            throw new ProcessingException("Cant save file.");
        }



    }


}
