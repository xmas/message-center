package com.xmas.controller.questions;

import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import com.xmas.service.questions.QuestionHelper;
import com.xmas.service.questions.data.DataType;
import com.xmas.service.questions.datasource.DataSourceType;
import com.xmas.service.questions.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController {

    @Autowired
    private QuestionHelper questionHelper;

    @RequestMapping(method = RequestMethod.POST)
    public void addQuestion(@RequestParam MultipartFile script,
                            @RequestParam MultipartFile answerTemplate,
                            @RequestParam DataSourceType dataSourceType,
                            @RequestParam DataType dataType,
                            @RequestParam ScriptType scriptType,
                            @RequestParam List<Tag> tags,
                            @RequestParam String dataSourceResource){

        Question question = new Question(tags, dataSourceType, dataType, scriptType);
        questionHelper.saveQuestion(question, script, answerTemplate);

    }
}
