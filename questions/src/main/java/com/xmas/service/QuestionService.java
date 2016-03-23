package com.xmas.service;

import com.xmas.dao.AnswerRepository;
import com.xmas.dao.QuestionRepository;
import com.xmas.entity.Answer;
import com.xmas.entity.Question;
import com.xmas.entity.Tag;
import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.QuestionNotFoundException;
import com.xmas.util.scheduler.JobDetailsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class QuestionService {

    @Autowired
    private QuestionHelper questionHelper;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private JobDetailsFactory<Question> jobDetailsFactory;

    @Autowired
    private TagsService tagsService;

    public List<Question> getQuestions(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return questionRepository.getAll();
        } else {
            List<Tag> tagsFromDb = tagsService.mapTagsToEntitiesFromDB(tags);
            return tagsFromDb.isEmpty() ? questionRepository.getAll() : questionRepository.getByTags(tagsFromDb);
        }
    }

    public Question getById(Long id) {
        return questionRepository
                .getById(id)
                .orElseThrow(QuestionNotFoundException::new);
    }

    public void addQuestion(Question question, MultipartFile script) {
        questionHelper.saveQuestion(question, script);
        if (question.getCron() != null && question.supportScheduling())
            scheduleQuestionEvaluating(question);
    }

    public List<Answer> evalQuestion(Long id, MultipartFile data) {
        Question question = questionRepository.getById(id).orElseThrow(QuestionNotFoundException::new);

        if (data == null)
            questionHelper.evaluate(question, question.getDataSourceResource());
        else
            questionHelper.evaluate(question, data);

        return answerRepository.findAnswers(question, LocalDate.now().atStartOfDay());
    }

    public void updateQuestion(Long id, Question question, MultipartFile script) {
        questionHelper.updateQuestion(id, question, script);
    }

    private void scheduleQuestionEvaluating(Question question) {
        if (!question.supportScheduling()) {
            throw new BadRequestException("Question with such type can't be scheduled for evaluating.");
        } else if (question.getCron() != null) {
            jobDetailsFactory.addQuestionJob(question);
        }
    }

}
