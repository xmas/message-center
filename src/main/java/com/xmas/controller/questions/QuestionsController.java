package com.xmas.controller.questions;

import com.xmas.dao.questions.AnswerRepository;
import com.xmas.dao.questions.QuestionRepository;
import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import com.xmas.exceptions.ProcessingException;
import com.xmas.exceptions.questions.QuestionNotFoundException;
import com.xmas.service.questions.QuestionHelper;
import com.xmas.service.questions.data.DataType;
import com.xmas.service.questions.datasource.DataSourceType;
import com.xmas.service.questions.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionHelper questionHelper;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AnswerRepository answerRepository;

    @RequestMapping
    public Iterable<Question> getQuestions(){
        return questionRepository.findAll();
    }

    @RequestMapping("/{id}")
    public Question getQuestion(@PathVariable Integer id){
        return questionRepository.getById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addQuestion(@RequestParam MultipartFile script,
                            @RequestParam MultipartFile answerTemplate,
                            @RequestParam DataSourceType dataSourceType,
                            @RequestParam DataType dataType,
                            @RequestParam ScriptType scriptType,
                            @RequestParam List<Tag> tags,
                            @RequestParam(required = false) String dataSourceResource){

        Question question = new Question(tags, dataSourceType, dataSourceResource, dataType, scriptType);
        questionHelper.saveQuestion(question, script, answerTemplate);

    }
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Answer evalQuestion(@PathVariable Integer id, @RequestParam(required = false) MultipartFile data){
        Question question = questionRepository.getById(id).orElseThrow(QuestionNotFoundException::new);

        if(data == null)
            questionHelper.evaluate(question);
        else
            questionHelper.evaluate(question, data);

        return answerRepository.findAnswer(question, LocalDate.now())
                .orElseThrow(() -> new ProcessingException("Answer is not evaluated. " +
                        "Maybe script is wrong."));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateQuestion(@PathVariable Integer id,
                               @RequestParam(required = false) MultipartFile script,
                               @RequestParam(required = false) MultipartFile answerTemplate,
                               @RequestParam(required = false) DataSourceType dataSourceType,
                               @RequestParam(required = false) DataType dataType,
                               @RequestParam(required = false) ScriptType scriptType,
                               @RequestParam(required = false) List<Tag> tags,
                               @RequestParam(required = false) String dataSourceResource){
        Question question = new Question(tags, dataSourceType, dataSourceResource, dataType, scriptType);
        questionHelper.updateQuestion(id, question, script, answerTemplate);
    }


}
