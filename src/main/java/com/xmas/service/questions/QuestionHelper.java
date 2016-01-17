package com.xmas.service.questions;

import com.xmas.dao.questions.QuestionRepository;
import com.xmas.dao.questions.TagsRepository;
import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.ProcessingException;
import com.xmas.exceptions.questions.QuestionNotFoundException;
import com.xmas.service.questions.answer.AnswerHelper;
import com.xmas.service.questions.answer.AnswerTemplateUtil;
import com.xmas.service.questions.datasource.DataService;
import com.xmas.service.questions.datasource.DataSourceType;
import com.xmas.service.questions.script.ScriptFileUtil;
import com.xmas.service.questions.script.ScriptService;
import com.xmas.util.FileUtil;
import com.xmas.util.RandomNamesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;
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

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AnswerHelper answerHelper;

    @Autowired
    DataService dataService;

    public void saveQuestion(Question question, MultipartFile scriptFile, MultipartFile answerTemplateFile) {

        File questionDir = createQuestionDirectory();

        ScriptFileUtil.saveScript(questionDir.getAbsolutePath(), scriptFile);
        AnswerTemplateUtil.saveTemplate(questionDir.getAbsolutePath(), answerTemplateFile);

        question.setDirectoryPath(questionDir.toPath().getFileName().toString());

        saveToDB(question);
    }

    public void updateQuestion(Integer qId, Question question, MultipartFile scriptFile, MultipartFile answerTemplateFile){
        Question fromDb = questionRepository.getById(qId).orElseThrow(QuestionNotFoundException::new);

        updateExistingFields(fromDb, question);

        if(scriptFile != null) ScriptFileUtil.replaceScript(getQuestionDirFullPath(fromDb), scriptFile);
        if(answerTemplateFile != null) AnswerTemplateUtil.replaceTemplate(getQuestionDirFullPath(fromDb), answerTemplateFile);

        saveToDB(fromDb);
    }

    public void evaluate(Question question) {
        if(question.getDataSourceType().equals(DataSourceType.FILE_UPLOAD))
            throw new BadRequestException("Cant evaluate this question without uploaded data");
        evaluate(question, question.getDataSourceResource());
    }

    public void evaluate(Question question, Object data){
        checkInput(question, data);

        dataService.evaluateData(question, data);
        scriptService.evaluate(question.getScriptType(),
                ScriptFileUtil.getScript(getQuestionDirFullPath(question)),
                getQuestionDirFullPath(question));
        question.setLastTimeEvaluated(LocalDateTime.now());
        answerHelper.saveAnswer(question);
    }

    private void checkInput(Question question, Object data){
        if(question == null) throw new IllegalArgumentException("Cant evaluate empty question.");
        if(data == null) throw new IllegalArgumentException("Cant evaluate question with empty data.");
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
