package com.xmas.service.questions.answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.dao.questions.AnswerRepository;
import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.QuestionHelper;
import com.xmas.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AnswerHelper {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AnswerRepository answerRepository;

    public static final String ANSWERS_FILE_NAME = "answers.json";

    private static final ObjectMapper mapper = new ObjectMapper();

    public void saveAnswers(Question question) {
        answerRepository.save(parseAnswers(question));
    }

    private List<Answer> parseAnswers(Question question) {
        try {
            return collect(mapper.reader()
                    .forType(Answer.class)
                    .readValues(getRawData(question)), question);
        } catch (IOException e) {
            throw new ProcessingException("Can't get answer from output file. Probably script evaluate wrong file structure");
        }
    }

    private List<Answer> collect(Iterator<Answer> answerIterator ,Question question){
        List<Answer> answers = new ArrayList<>();
        answerIterator.forEachRemaining(answer -> {
            answers.add(fillDefaultFields(answer, question));
        });
        return answers;
    }

    private String getRawData(Question question){
        String answersFilePath = Paths.get(QuestionHelper.getQuestionDirFullPath(question), ANSWERS_FILE_NAME).toString();
        return new String(FileUtil.getFile(answersFilePath));
    }

    private Answer fillDefaultFields(Answer answer, Question question){
        answer.setQuestion(question);
        answer.setDate(question.getLastTimeEvaluated());
        return answer;
    }
}
