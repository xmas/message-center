package com.xmas.service.questions;

import com.xmas.dao.questions.AnswerRepository;
import com.xmas.dao.questions.QuestionRepository;
import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.ProcessingException;
import com.xmas.exceptions.questions.QuestionNotFoundException;
import com.xmas.service.questions.scheduller.JobDetailsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuestionService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionHelper questionHelper;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private JobDetailsFactory jobDetailsFactory;

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

    public Question getById(Integer id) {
        return questionRepository
                .getById(id)
                .orElseThrow(QuestionNotFoundException::new);
    }

    public void addQuestion(Question question, MultipartFile script, MultipartFile answerTemplate) {
        questionHelper.saveQuestion(question, script, answerTemplate);
        if (question.getCron() != null)
            scheduleQuestionEvaluating(question);
    }

    public Answer evalQuestion(Integer id, MultipartFile data) {
        Question question = questionRepository.getById(id).orElseThrow(QuestionNotFoundException::new);

        if (data == null)
            questionHelper.evaluate(question);
        else
            questionHelper.evaluate(question, data);

        return answerRepository.findAnswer(question, LocalDate.now())
                .orElseThrow(() -> new ProcessingException("Answer is not evaluated. " +
                        "Maybe script is wrong."));
    }

    public void updateQuestion(Integer id, Question question, MultipartFile script, MultipartFile answerTemplate) {
        questionHelper.updateQuestion(id, question, script, answerTemplate);
    }

    private void scheduleQuestionEvaluating(Question question) {
        if (!canBeScheduled(question)) {
            throw new BadRequestException("Question with such type can't be scheduled for evaluating.");
        } else if (question.getCron() != null) {
            jobDetailsFactory.addQuestionJob(question);
        }
    }

    private boolean canBeScheduled(Question question) {
        return question.getDataSourceType().supportScheduling();
    }


}
