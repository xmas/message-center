package com.xmas.service.questions.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ScriptService {

    @Autowired
    @Qualifier("rScriptEvaluator")
    private ScriptEvaluator rScriptEvaluator;

    public void evaluate(ScriptType type, String script, String workDir) {
        switch (type){
            case R : rScriptEvaluator.evaluate(script, workDir);
        }
    }
}
