package com.xmas.util.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    public void evaluate(ScriptType type, String workDir, Map<String, String> args) {
        switch (type) {
            case R:
                rScriptEvaluator.evaluate(ScriptFileUtil.getScript(workDir), workDir, args); break;
            case PYTHON:
                pythonScriptEvaluator.evaluate(ScriptFileUtil.getScript(workDir), workDir, args); break;
            case NODE:
                nodeScriptEvaluator.evaluate(null, workDir, args); break;
            default:
                throw new RuntimeException("Unsupported script type exception");
        }
    }

}
