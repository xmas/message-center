package com.xmas.controller.questions;

import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import com.xmas.service.questions.QuestionService;
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
    private QuestionService questionService;

    @RequestMapping
    public Iterable<Question> getQuestions(@RequestParam(required = false) List<String> tags) {
        return questionService.getQuestions(tags);
    }

    @RequestMapping("/{id}")
    public Question getQuestion(@PathVariable Integer id) {
        return questionService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addQuestion(@RequestParam MultipartFile script,
                            @RequestParam MultipartFile answerTemplate,
                            @RequestParam DataSourceType dataSourceType,
                            @RequestParam DataType dataType,
                            @RequestParam String cron,
                            @RequestParam ScriptType scriptType,
                            @RequestParam List<Tag> tags,
                            @RequestParam(required = false) String dataSourceResource) {

        Question question = new Question(tags, dataSourceType, dataSourceResource, dataType, scriptType, cron);
        questionService.addQuestion(question, script, answerTemplate);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Answer evalQuestion(@PathVariable Integer id, @RequestParam(required = false) MultipartFile data) {
        return questionService.evalQuestion(id, data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateQuestion(@PathVariable Integer id,
                               @RequestParam(required = false) MultipartFile script,
                               @RequestParam(required = false) MultipartFile answerTemplate,
                               @RequestParam(required = false) DataSourceType dataSourceType,
                               @RequestParam(required = false) DataType dataType,
                               @RequestParam(required = false) ScriptType scriptType,
                               @RequestParam String cron,
                               @RequestParam(required = false) List<Tag> tags,
                               @RequestParam(required = false) String dataSourceResource) {
        Question question = new Question(tags, dataSourceType, dataSourceResource, dataType, scriptType, cron);
        questionService.updateQuestion(id, question, script, answerTemplate);
    }


}
