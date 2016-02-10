package com.xmas.service;

import com.xmas.dao.QuestionRepository;
import com.xmas.dao.TagsRepository;
import com.xmas.entity.Question;
import com.xmas.entity.Tag;
import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.ProcessingException;
import com.xmas.exceptions.QuestionNotFoundException;
import com.xmas.service.answer.AnswerHelper;
import com.xmas.service.datasource.DataService;
import com.xmas.util.FileUtil;
import com.xmas.util.RandomNamesUtil;
import com.xmas.util.script.ScriptFileUtil;
import com.xmas.util.script.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class QuestionHelper implements QuestionEvaluator{

    public static final String QUESTIONS_BASE_DIR_PATH = QuestionHelper.class.getResource("/questions").getPath();

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private AnswerHelper answerHelper;

    @Autowired
    DataService dataService;

    public void saveQuestion(Question question, MultipartFile scriptFile) {

        File questionDir = createQuestionDirectory();

        ScriptFileUtil.saveScript(questionDir.getAbsolutePath(), scriptFile);

        question.setDirectoryPath(questionDir.toPath().getFileName().toString());

        saveToDB(question);
    }

    public void updateQuestion(Integer qId, Question question, MultipartFile scriptFile){
        Question fromDb = questionRepository.getById(qId).orElseThrow(QuestionNotFoundException::new);

        updateExistingFields(fromDb, question);

        if(scriptFile != null) ScriptFileUtil.replaceScript(getQuestionDirFullPath(fromDb), scriptFile);

        saveToDB(fromDb);
    }

    public void evaluate(Question question) {
        if(question.getDataSourceType().requireData() && question.getDataSourceResource() == null)
            throw new BadRequestException("Cant evaluate this question without uploaded data");
        evaluate(question, question.getDataSourceResource());
    }

    public void evaluate(Question question, Object data){
        checkInput(question, data);

        LocalDateTime evaluationTime = dataService.evaluateData(question, data);
        scriptService.evaluate(question.getScriptType(), getQuestionDirFullPath(question), question.getScriptArgs());
        question.setLastTimeEvaluated(evaluationTime);
        answerHelper.saveAnswers(question);
        dataService.packageQuestionData(question);
    }

    private void checkInput(Question question, Object data){
        if(question == null) throw new IllegalArgumentException("Cant evaluate empty question.");
        if(data == null && question.getDataSourceType().requireData())
            throw new IllegalArgumentException("Cant evaluate question with empty data.");
    }

    private File createQuestionDirectory() {
        String questionDirPath = Paths.get(QUESTIONS_BASE_DIR_PATH, RandomNamesUtil.getRandomName())
                .toAbsolutePath()
                .toString();

        return FileUtil.createDirectory(questionDirPath);
    }

    private void saveToDB(Question question){
        question.setTags(question.getTags().stream()
                .map(this::getSavedOrNew)
                .collect(Collectors.toList()));

        questionRepository.save(question);
    }

    private Tag getSavedOrNew(Tag newTag){
        Tag saved = tagsRepository.getByName(newTag.getName());
        if(saved != null){
            newTag.setId(saved.getId());
        }
        tagsRepository.save(newTag);
        return newTag;
    }

    private Question updateExistingFields(Question fromDb, Question patch) {
        try {
            for (Field field : Question.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(patch) != null ? field.get(patch) : field.get(fromDb);
                field.set(fromDb, value);
            }
            return fromDb;
        }catch (IllegalAccessException iae){
            throw new ProcessingException("Can't update question.", iae);
        }

    }

    public static String getQuestionDirFullPath(Question question){
        if(question == null || question.getDirectoryPath() == null)
            throw new IllegalArgumentException("Cant evaluate dir for this question.");
        return QUESTIONS_BASE_DIR_PATH + "/" +  question.getDirectoryPath();
    }



}
