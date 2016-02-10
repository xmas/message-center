package com.xmas.util.script;

import java.util.Map;

public interface ScriptEvaluator {
    void evaluate(String script, String workDir, Map<String, String> args);
}
