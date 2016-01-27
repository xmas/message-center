package com.xmas.controller.questions;

import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import com.xmas.exceptions.push.ResourceNotFoundException;
import com.xmas.service.questions.QuestionService;
import com.xmas.service.questions.data.DataType;
import com.xmas.service.questions.datasource.DataSourceType;
import com.xmas.service.questions.script.ScriptType;
import com.xmas.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;
import java.util.Optional;

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
                            @RequestParam DataSourceType dataSourceType,
                            @RequestParam DataType dataType,
                            @RequestParam(required = false) String cron,
                            @RequestParam ScriptType scriptType,
                            @RequestParam List<Tag> tags,
                            @RequestParam(required = false) String dataSourceResource) {

        Question question = new Question(tags, dataSourceType, dataSourceResource, dataType, scriptType, cron);
        questionService.addQuestion(question, script);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public List<Answer> evalQuestion(@PathVariable Integer id, @RequestParam(required = false) MultipartFile data) {
        return questionService.evalQuestion(id, data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateQuestion(@PathVariable Integer id,
                               @RequestParam(required = false) MultipartFile script,
                               @RequestParam(required = false) DataSourceType dataSourceType,
                               @RequestParam(required = false) DataType dataType,
                               @RequestParam(required = false) ScriptType scriptType,
                               @RequestParam(required = false) String cron,
                               @RequestParam(required = false) List<Tag> tags,
                               @RequestParam(required = false) String dataSourceResource) {
        Question question = new Question(tags, dataSourceType, dataSourceResource, dataType, scriptType, cron);
        questionService.updateQuestion(id, question, script);
    }

    @RequestMapping(value = {"/data/{a}", "/data/{a}/{b}", "/data/{a}/{b}/{c}", "/data/{a}/{b}/{c}/{d}"}, method = RequestMethod.GET)
    public Object getFiles(HttpServletRequest request) {
        return Optional.ofNullable(this.getClass()
                .getResource(mapRequestPathToResource(request)))
                .map(URL::getPath)
                .map(FileUtil::getFilesInDirOrFileContent)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private String mapRequestPathToResource(HttpServletRequest request) {
        return request.getServletPath().replace("/data", "");
    }

}
