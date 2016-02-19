package com.xmas.insight.service;

import com.xmas.insight.dao.InsightEvaluatorRepository;
import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.util.FileUtil;
import com.xmas.util.data.FileSystemData;
import com.xmas.util.script.ScriptFileUtil;
import com.xmas.util.script.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

@Service
public class InsightEvaluatorHelper {

    @Autowired
    private InsightEvaluatorRepository repository;

    @Autowired
    private ScriptService scriptService;

    private static String EVALUATORS_BASE_DIR = InsightEvaluatorHelper.class.getResource("/insights").getPath();

    public void saveInsightEvaluator(InsightEvaluator evaluator, MultipartFile scriptFile){
        File evaluatorDir = FileUtil.createRandomNameDirInThis(EVALUATORS_BASE_DIR);
        ScriptFileUtil.saveScript(evaluatorDir.getAbsolutePath(), scriptFile);
        evaluator.setDirectoryPath(evaluatorDir.toPath().getFileName().toString());
        saveToDB(evaluator);
    }

    private void saveToDB(InsightEvaluator evaluator){
        repository.save(evaluator);
    }

    public void evaluate(InsightEvaluator evaluator){
        FileSystemData fileSystemData  = new FileSystemData(getQuestionDirFullPath(evaluator));
        LocalDateTime evaluationTime = fileSystemData.evaluateData(null);
        scriptService.evaluate(evaluator.getScriptType(), getQuestionDirFullPath(evaluator), evaluator.getScriptArgs());
        evaluator.setLastTimeEvaluated(evaluationTime);
        //answerHelper.saveAnswers(question);
        fileSystemData.packageEvaluatedFiles();
    }

    private String getQuestionDirFullPath(InsightEvaluator evaluator) {
        return "/insights/" + evaluator.getDirectoryPath();
    }

}
