package com.xmas.service.questions.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ScriptService {

    @Autowired
    @Qualifier("rScriptEvaluator")
    private ScriptEvaluator rScriptEvaluator;

    @Autowired
    @Qualifier("pythonScriptEvaluator")
    private ScriptEvaluator pythonScriptEvaluator;

    public void evaluate(ScriptType type, String script, String workDir) {
        getEvaluator(type).evaluate(script, workDir);
    }

    private ScriptEvaluator getEvaluator(ScriptType scriptType) {
        switch (scriptType) {
            case R:
                return rScriptEvaluator;
            case PYTHON:
                return pythonScriptEvaluator;
            default:
                throw new RuntimeException("Unsupported script type exception");
        }
    }
}
