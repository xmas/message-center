package com.xmas.R.service;

import com.xmas.exceptions.ScriptEvaluationExceprion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScriptEvaluatorService {

    private static final Logger logger = LogManager.getLogger();

    public static final String R_DIR_ARG_NAME = "dir";

    @Autowired
    private RConnectionManager connectionManager;

    public void evaluateScript(String script, String requestDir){
        try {
            REngine R = connectionManager.getConnection();

            R.assign(R_DIR_ARG_NAME, requestDir);

            REXP r = R.parseAndEval(script);
            if (r.inherits("try-error")) System.err.println("Error: "+r.asString());

        }catch (REngineException | REXPMismatchException e){
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ScriptEvaluationExceprion(e);
        }
    }

    public void setConnectionManager(RConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
