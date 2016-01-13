package com.xmas.service.questions.script.pyton;

import com.xmas.service.questions.script.ScriptEvaluator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("pythonScriptEvaluator")
public class PythonScriptEvaluator implements ScriptEvaluator{

    @Override
    public void evaluate(String script, String workDir) {

    }
}
