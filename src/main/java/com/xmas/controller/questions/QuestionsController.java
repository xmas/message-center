package com.xmas.controller.questions;

import com.xmas.dao.questions.QuestionRepository;
import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import com.xmas.exceptions.NotFoundException;
import com.xmas.service.questions.QuestionHelper;
import com.xmas.service.questions.data.DataType;
import com.xmas.service.questions.datasource.DataSourceType;
import com.xmas.service.questions.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping
    public Iterable<Question> getQuestions(){
        return questionRepository.findAll();
    }

    @RequestMapping("/{id}")
    public Question getQuestion(@PathVariable Integer id){
        return questionRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("There is no question with such id."));
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addQuestion(@RequestParam MultipartFile script,
                            @RequestParam MultipartFile answerTemplate,
                            @RequestParam DataSourceType dataSourceType,
                            @RequestParam DataType dataType,
                            @RequestParam ScriptType scriptType,
                            @RequestParam List<Tag> tags,
                            @RequestParam String dataSourceResource){

        Question question = new Question(tags, dataSourceType, dataSourceResource, dataType, scriptType);
        questionHelper.saveQuestion(question, script, answerTemplate);

    }
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public void evalQuestion(@PathVariable Integer id, @RequestParam(required = false) MultipartFile data){
        Question question = questionRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("There is no question with such id."));

        if(data == null)
            questionHelper.evaluate(question);
        else
            questionHelper.evaluate(question, data);
    }


}
