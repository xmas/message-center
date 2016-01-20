package com.xmas.service.R;

import com.xmas.dao.R.ScriptRepository;
import com.xmas.entity.R.Script;
import com.xmas.exceptions.R.ScriptEvaluationExceprion;
import com.xmas.util.FileUtil;
import com.xmas.util.RandomNamesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class RService {

    public static final String SCRIPTS_DIRECTORY = "/R/scripts/";

    @Autowired
    RScriptEvaluatorService scriptEvaluator;

    @Autowired
    RequestDirectoriesProcessor directoriesProcessor;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    ScriptRepository scriptRepository;

    public Iterable<Script> getScripts(){
        return scriptRepository.findAll();
    }

    public Script getScript(Integer id){
        return scriptRepository.findOne(id);
    }

    public Script createScript(MultipartFile file, String name, String description){
        String scriptFileName = generateScriptFileName(file.getOriginalFilename());
        Script script = new Script(scriptFileName);
        script.setName(name);
        script.setDescription(description);
        saveScriptFile(scriptFileName, file);
        scriptRepository.save(script);
        return script;
    }

    public String evaluateScript(Integer id, MultipartFile input){
        String requestDirectory = directoriesProcessor.createDirectoriesForRequest().getPath();

        saveInputFile(requestDirectory, input);

        String script = loadScript(scriptRepository.findOne(id).getScriptFileName());
        scriptEvaluator.evaluate(script, requestDirectory);

        return retrieveFinalDir(requestDirectory);
    }

    private String retrieveFinalDir(String fullPath){
        String[] path = fullPath.split("/");
        return path[path.length-1];
    }

    protected String loadScript(String scriptName){
        try {
            String appBaseFolder = this.getClass().getResource("/").getPath();
            InputStream scriptResourceStream = new FileInputStream(appBaseFolder + SCRIPTS_DIRECTORY + scriptName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(scriptResourceStream));
            return reader.lines().reduce((res, str) -> res + "\n" + str).get();
        }catch (IOException ioe){
            throw new ScriptEvaluationExceprion(ioe);
        }
    }

    protected String generateScriptFileName(String rawName){
        return rawName.substring(0, rawName.lastIndexOf(".")) +
                "_" +
                RandomNamesUtil.getRandomName() +
                rawName.substring(rawName.lastIndexOf("."));
    }

    protected void saveScriptFile(String fileName, MultipartFile file){
        String appBaseFolder = this.getClass().getResource("/").getPath();
        File scriptFile = new File(appBaseFolder + SCRIPTS_DIRECTORY + fileName);
        FileUtil.saveUploadedFile(scriptFile, file);
    }

    protected void saveInputFile(String dir, MultipartFile file){
        File inputFile = new File(dir + "/input/input.txt");
        FileUtil.saveUploadedFile(inputFile, file);
    }


}
