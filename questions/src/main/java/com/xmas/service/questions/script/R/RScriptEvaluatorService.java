package com.xmas.service.questions.script.R;

import com.xmas.exceptions.questions.ScriptEvaluationExceprion;
import com.xmas.service.questions.script.ScriptEvaluator;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Qualifier("rScriptEvaluator")
public class RScriptEvaluatorService implements ScriptEvaluator{

    public static final String R_DIR_ARG_NAME = "dir";
    public static final String FUNCTION_NAME = "mainEvaluatingFunction";

    @Autowired
    private RConnectionManager connectionManager;

    public void evaluate(String script, String requestDir, Map<String, String> args){
        try {
            REngine R = connectionManager.getConnection();

            setArgs(R, args, requestDir);

            R.parseAndEval(createFunction(script));
            REXP r = R.parseAndEval("try(" + FUNCTION_NAME + "(), silent=FALSE)");
            if (r.inherits("try-error"))
                throw new ScriptEvaluationExceprion(r.asString());

        }catch (REngineException | REXPMismatchException e){
            throw new ScriptEvaluationExceprion(e);
        }
    }

    private void setArgs(REngine R, Map<String, String> args, String workDir) throws REngineException {
        R.assign(R_DIR_ARG_NAME, workDir);
        args.keySet().stream().forEach(key -> assignArg(R, key, args.get(key)));
    }

    private void assignArg(REngine R, String argName, String argValue){
        try {
            R.assign(argName, argValue);
        } catch (REngineException e) {
            throw new ScriptEvaluationExceprion(e);
        }
    }

    private String createFunction(String functionBody){
        return FUNCTION_NAME + "<- function(){\n" +
                functionBody + "\n" +
                "}";
    }

    public void setConnectionManager(RConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
