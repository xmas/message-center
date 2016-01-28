package com.xmas.service.questions.script.pyton;

import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.script.ScriptEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("pythonScriptEvaluator")
public class PythonScriptEvaluator implements ScriptEvaluator{

    private static final Logger logger = LogManager.getLogger();

    public static final String DIR_ARG_NAME = "question_dir";
    public static final String SCRIPT_FILE = "/script/script.sc";

    @Autowired
    private PythonInterpreterManager interpreterManager;

    @Override
    public void evaluate(String script, String workDir) {
        try {
            PythonInterpreter interpreter = interpreterManager.getInterpreter();

            interpreter.set(DIR_ARG_NAME, workDir);
            interpreter.execfile(workDir + SCRIPT_FILE);
        }catch (Exception pyException){
            logger.error("Error during executind script " + workDir + SCRIPT_FILE);
            logger.debug(pyException.getMessage(), pyException);
            throw new ProcessingException(pyException);
        }

    }

}
