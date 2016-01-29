package com.xmas.service.questions.script;

import java.util.Map;

public interface ScriptEvaluator {
    void evaluate(String script, String workDir, Map<String, String> args);
}
