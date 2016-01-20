package com.xmas.controller.questions;

import com.xmas.dao.questions.AnswerRepository;
import com.xmas.dao.questions.QuestionRepository;
import com.xmas.entity.questions.Answer;
import com.xmas.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/questions/{qId}/answers")
public class AnswersController {

    public static final String MIN_DATE = "1970-01-01";
    public static final String MAX_DATE = "3000-01-01";

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AnswerRepository answerRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;


    @RequestMapping
    public Iterable<Answer> getAnswers(@PathVariable Integer qId,
                                       @RequestParam(required = false, defaultValue = MIN_DATE) LocalDate from,
                                       @RequestParam(required = false, defaultValue = MAX_DATE) LocalDate to) {
        return questionRepository.getById(qId)
                .map(q -> answerRepository.findAnswers(q, from, to))
                .orElseThrow(() -> new NotFoundException("There is no question with such id"));
    }

    @RequestMapping("/today")
    public Answer getToday(@PathVariable Integer qId) {
        return questionRepository.getById(qId)
                .map(q -> answerRepository
                        .findAnswer(q, LocalDate.now())
                        .orElseGet(() -> {
                            throw new NotFoundException("Answer is not evaluated still.");
                        }))
                .orElseThrow(() -> new NotFoundException("There is no question with such id"));
    }
}
