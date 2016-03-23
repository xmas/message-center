package com.xmas.util.script.node;

import com.xmas.exceptions.ProcessingException;
import com.xmas.util.script.ScriptEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Qualifier("nodeScriptEvaluator")
@Slf4j
public class NodeScriptEvaluator implements ScriptEvaluator {

    public static final String SCRIPT_FILE = "/script/script.sc";

    @Value("${node.global.lib}")
    private String nodePath;

    @Override
    public void evaluate(String scriptFileName, String workDir, Map<String, String> args) {
        try {

            Process process = Runtime.getRuntime()
                    .exec(buildExecString(workDir, args), getEnvironment(), new File(workDir));

            logScriptExecutingProcess(process);

            int processResult = process.waitFor();

            if (processResult != 0) {
                String error = getError(process.getErrorStream());
                processError(error, processResult);
            }
        } catch (IOException | ProcessingException | InterruptedException e) {
            throw new ProcessingException(e);
        }
    }

    private void logScriptExecutingProcess(Process process){
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        BufferedReader stdReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        logStrings(errorReader.lines(), 4);
        logStrings(stdReader.lines(), 0);
    }

    private void logStrings(Stream<String> stream, int level){
        switch (level) {
            case 0 : stream.forEach(log::trace);break;
            case 1 : stream.forEach(log::debug);break;
            case 2 : stream.forEach(log::info);break;
            case 3 : stream.forEach(log::warn);break;
            case 4 : stream.forEach(log::error);break;
        }
    }

    private void processError(String executionResult, int processResult) {
        Pattern noModuleErrorPattern = Pattern.compile("Error: Cannot find module '([a-z]+)'");
        Matcher matcher = noModuleErrorPattern.matcher(executionResult);
        if (matcher.find()) {
            try {
                tryToInstallRequiredModule(matcher.group(1));
            } catch (ProcessingException pe){
                throw new ProcessingException("Error during evaluating Node script. Exit code: " + processResult + ". "+ executionResult);
            }
        } else {
            throw new ProcessingException("Error during evaluating Node script. Exit code: "+ processResult + ". " + executionResult);
        }
    }

    private String buildExecString(String workDir, Map<String, String> args) {
        return new ArrayList<String>() {{
            add(workDir + SCRIPT_FILE);
            addAll(buildScriptArgsString(args));
        }}.stream().collect(Collectors.joining(" "));
    }

    private List<String> buildScriptArgsString(Map<String, String> args) {
        return args.keySet().stream()
                .map(key -> key + " " + args.get(key))
                .collect(Collectors.toList());
    }

    private String[] getEnvironment(){
        return new String[]{"NODE_PATH="+nodePath};
    }

    private String getError(InputStream errorStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    private void tryToInstallRequiredModule(String moduleName) {
        try {
            log.debug("Try to install node module " + moduleName);
            Process process = new ProcessBuilder()
                    .command("npm", "install", "-g", moduleName)
                    .start();

            int processResult = process.waitFor();

            if (processResult != 0) {
                String error = getError(process.getErrorStream());
                throw new ProcessingException("Error during installing Node module. Exit code: " + processResult + ".\n" +
                        error);
            }
            log.info("Node module " + moduleName + "successfully installed.");
        } catch (IOException | InterruptedException | ProcessingException e) {
            throw new ProcessingException(e);
        }
    }

}
