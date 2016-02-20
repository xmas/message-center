package com.xmas.util.script.node;

import com.xmas.exceptions.ProcessingException;
import com.xmas.util.script.ScriptEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("nodeScriptEvaluator")
public class NodeScriptEvaluator implements ScriptEvaluator {

    private static final Logger logger = LogManager.getLogger();

    public static final String SCRIPT_FILE = "/script/script.sc";

    @Value("${node.global.lib}")
    private String nodePath;

    @Override
    public void evaluate(String scriptFileName, String workDir, Map<String, String> args){
        try {

            Process process = Runtime.getRuntime()
                    .exec(buildExecString(workDir, args), getEnvironment(), new File(workDir));

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

    private String buildExecString(String workDir, Map<String, String> args){
        return "node " +  workDir + SCRIPT_FILE + " " + buildScriptArgsString(args);
    }

    private String buildScriptArgsString(Map<String, String> args){
        return args.keySet().stream()
                .map(key -> key + " " + args.get(key))
                .collect(Collectors.joining(" "));
    }

    private String[] getEnvironment(){
        return new String[]{"NODE_PATH="+nodePath};
    }

    private String getError(InputStream errorStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
        return reader.lines().collect(Collectors.joining("\n"));
    }


}
