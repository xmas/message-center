package com.xmas.service.questions.answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.dao.questions.AnswerRepository;
import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.exceptions.ProcessingException;
import com.xmas.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class AnswerHelper {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AnswerRepository answerRepository;

    public static final String ANSWER_FILE_NAME = "answer.json";

    private static final ObjectMapper mapper = new ObjectMapper();

    public void saveAnswer(Question question){
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
        return !Objects.equals(a1.getQuestion(), a2.getQuestion()) &&
                Objects.equals(a1.getDate(), a2.getDate()) &&
                Objects.equals(a1.getGuid(), a2.getGuid());
    }
}
