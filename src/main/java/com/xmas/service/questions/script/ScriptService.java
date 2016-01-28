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

    @Autowired
    @Qualifier("nodeScriptEvaluator")
    private ScriptEvaluator nodeScriptEvaluator;

    public void evaluate(ScriptType type, String workDir) {
        switch (type) {
            case R:
                rScriptEvaluator.evaluate(ScriptFileUtil.getScript(workDir), workDir); break;
            case PYTHON:
                pythonScriptEvaluator.evaluate(ScriptFileUtil.getScript(workDir), workDir); break;
            case NODE:
                nodeScriptEvaluator.evaluate(null, workDir); break;
            default:
                throw new RuntimeException("Unsupported script type exception");
        }
    }

}
