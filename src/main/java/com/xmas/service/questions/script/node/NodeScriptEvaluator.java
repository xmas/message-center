package com.xmas.service.questions.script.node;

import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.script.ScriptEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.stream.Collectors;

@Service
@Qualifier("nodeScriptEvaluator")
public class NodeScriptEvaluator implements ScriptEvaluator{

    private static final Logger logger = LogManager.getLogger();

    public static final String SCRIPT_FILE = "/script/script.sc";

    @Override
    public void evaluate(String scriptFileName, String workDir){
        try {

            Process process = Runtime.getRuntime().exec(workDir + SCRIPT_FILE, getEnvironment(), new File(workDir));

            int processResult = process.waitFor();

            if(processResult != 0){
                String error = getError(process.getErrorStream());
                throw new ProcessingException("Error during evaluating Node script. Exit code: " + processResult + ".\n" +
                        error);
            }
        } catch (IOException | InterruptedException | ProcessingException e) {
            logger.error("Error during executind script " + workDir + SCRIPT_FILE);
            logger.debug(e.getMessage(), e);
            throw new ProcessingException(e);
        }
    }

    private String[] getEnvironment(){
        return System.getenv()
                .keySet()
                .stream()
                .map(key -> key+ "=" + System.getenv().get(key))
                .peek(System.out::println)
                .toArray((l) -> new String[l]);
    }

    private String getError(InputStream errorStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
        return reader.lines().collect(Collectors.joining("\n"));
    }


}
