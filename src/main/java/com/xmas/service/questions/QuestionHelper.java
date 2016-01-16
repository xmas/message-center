package com.xmas.service.questions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.dao.questions.AnswerRepository;
import com.xmas.dao.questions.QuestionRepository;
import com.xmas.dao.questions.TagsRepository;
import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.exceptions.ProcessingException;
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
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuestionHelper {

    public static final String ANSWER_FILE_NAME = "answer.json";

    public static final String QUESTIONS_BASE_DIR_PATH = QuestionHelper.class.getResource("/questions").getPath();

    private static final ObjectMapper mapper = new ObjectMapper();

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
    private AnswerRepository answerRepository;

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
        dataService.evaluateData(question, question.getDataSourceResource());
        scriptService.evaluate(question.getScriptType(),
                ScriptFileUtil.getScript(question.getDirectoryPath()),
                question.getDirectoryPath());
        saveAnswer(question);
    }

    public void evaluate(Question question, Object data){
        dataService.evaluateData(question, data);
        scriptService.evaluate(question.getScriptType(),
                ScriptFileUtil.getScript(question.getDirectoryPath()),
                question.getDirectoryPath());
        question.setLastTimeEvaluated(LocalDateTime.now());
        saveAnswer(question);
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

    private void saveAnswer(Question question){
        answerRepository.save(
                answerRepository
                        .findAnswer(question, LocalDate.now())
                        .map(answer -> updateDataFields(answer, parseAnswer(question)))
                        .orElse(parseAnswer(question)));

    }

    private Answer parseAnswer(Question question){
        try {
            String answerFilePath = Paths.get(question.getDirectoryPath(), ANSWER_FILE_NAME).toString();

            String rawAnswerData = new String(FileUtil.getFile(answerFilePath));

            Answer answer =  mapper.reader().forType(Answer.class).readValue(rawAnswerData);

            answer.setDate(LocalDate.now());
            answer.setQuestion(question);

            return answer;
        } catch (IOException e) {
            throw new ProcessingException("Can't get answer from output file. Probably script evaluate wrong file structure");
        }
    }

    private Answer updateDataFields(Answer fromDb, Answer newAnswer){
        if (isUnique(fromDb, newAnswer)) return newAnswer;

        fromDb.setTitle(newAnswer.getTitle());
        fromDb.setDetails(newAnswer.getDetails());
        return fromDb;
    }

    private boolean isUnique(Answer a1, Answer a2){
        return Objects.equals(a1.getQuestion(), a2.getQuestion()) &&
                Objects.equals(a1.getDate(), a2.getDate()) &&
                Objects.equals(a1.getGuid(), a2.getGuid());
    }

}
