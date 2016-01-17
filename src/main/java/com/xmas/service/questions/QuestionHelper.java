package com.xmas.service.questions;

import com.xmas.dao.questions.QuestionRepository;
import com.xmas.dao.questions.TagsRepository;
import com.xmas.entity.questions.Question;
import com.xmas.service.questions.answer.AnswerHelper;
import com.xmas.service.questions.answer.AnswerTemplateUtil;
import com.xmas.service.questions.datasource.DataService;
import com.xmas.service.questions.script.ScriptFileUtil;
import com.xmas.service.questions.script.ScriptService;
import com.xmas.util.FileUtil;
import com.xmas.util.RandomNamesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class QuestionHelper {

    public static final String QUESTIONS_BASE_DIR_PATH = QuestionHelper.class.getResource("/questions").getPath();

    @Autowired
    private ScriptService scriptService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private AnswerHelper answerHelper;

    @Autowired
    DataService dataService;

    public void saveQuestion(Question question, MultipartFile scriptFile, MultipartFile anwerTemplateFile) {

        File questionDir = createQuestionDirectory();

        ScriptFileUtil.saveScript(questionDir.getAbsolutePath(), scriptFile);
        AnswerTemplateUtil.saveTemplate(questionDir.getAbsolutePath(), anwerTemplateFile);

        question.setDirectoryPath(questionDir.getAbsolutePath());

        saveToDB(question);
    }

    public void evaluate(Question question) {
        evaluate(question, question.getDataSourceResource());
    }

    public void evaluate(Question question, Object data){
        dataService.evaluateData(question, data);
        scriptService.evaluate(question.getScriptType(),
                ScriptFileUtil.getScript(question.getDirectoryPath()),
                question.getDirectoryPath());
        question.setLastTimeEvaluated(LocalDateTime.now());
        answerHelper.saveAnswer(question);
    }

    private File createQuestionDirectory() {
        String questionDirPath = Paths.get(QUESTIONS_BASE_DIR_PATH, RandomNamesUtil.getRandomName())
                .toAbsolutePath()
                .toString();

        return FileUtil.createDirectory(questionDirPath);
    }

    private void saveToDB(Question question){
        question.setTags(question.getTags().stream()
                .map(tag -> tagsRepository.getByName(tag.getName()) == null ? tag : tagsRepository.getByName(tag.getName()))
                .peek(tagsRepository::save)
                .collect(Collectors.toList()));

        questionRepository.save(question);
    }



}
