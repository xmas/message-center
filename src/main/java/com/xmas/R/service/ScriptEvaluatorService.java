package com.xmas.R.service;

import com.xmas.exceptions.ScriptEvaluationExceprion;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScriptEvaluatorService {

    public static final String R_DIR_ARG_NAME = "dir";

    @Autowired
    RConnectionManager connectionManager;

    public void evaluateScript(String script, String requestDir){
        try {
            REngine R = connectionManager.getConnection();

            R.assign(R_DIR_ARG_NAME, requestDir);
            R.parseAndEval(script);
        }catch (REngineException | REXPMismatchException e){
            throw new ScriptEvaluationExceprion(e);
        }

    }

}
