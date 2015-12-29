package com.xmas.R.service;

import com.xmas.dao.ScriptRepository;
import com.xmas.entity.Script;
import com.xmas.exceptions.ScriptEvaluationExceprion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class RService {

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

    public void saveScript(Script script){
        scriptRepository.save(script);
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


}
