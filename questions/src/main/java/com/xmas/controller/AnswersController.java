package com.xmas.controller;

import com.xmas.dao.AnswerRepository;
import com.xmas.dao.QuestionRepository;
import com.xmas.entity.Answer;
import com.xmas.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/{qId}/answers")
public class AnswersController {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AnswerRepository answerRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;


    @RequestMapping
    public Iterable<Answer> getAnswers(@PathVariable Integer qId,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate from,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)LocalDate to) {
        LocalDateTime startFromDay = getStartOfDay(from);
        LocalDateTime endToDay = getEndOfDay(to);
        return questionRepository.getById(qId)
                .map(q -> answerRepository.findAnswers(q, startFromDay, endToDay))
                .orElseThrow(() -> new NotFoundException("There is no question with such id"));
    }

    private LocalDateTime getStartOfDay(LocalDate date){
        return date == null ? LocalDate.now().atStartOfDay() : date.atStartOfDay();
    }

    private LocalDateTime getEndOfDay(LocalDate date){
        return date == null ? LocalDate.now().atTime(23, 59, 59) : date.atTime(23, 59, 59);
    }
}
