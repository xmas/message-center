package com.xmas.service.answer;

import com.xmas.dao.AnswerRepository;
import com.xmas.entity.Answer;
import com.xmas.entity.EntityHelper;
import com.xmas.entity.Question;
import com.xmas.util.FileUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtil.class)
public class AnswerHelperTest {

    public static final String answersFileContent = "[\n" +
            "  \n" +
            "  {\n" +
            "    \"title\": \"Hello USER\",\n" +
            "    \"details\": \"Hi, it new value 456\",\n" +
            "    \"guid\": \"\"\n" +
            "  },\n" +
            "  \n" +
            "  {\n" +
            "    \"title\": \"Hello User\",\n" +
            "    \"details\": \"Hi, it new value 789\",\n" +
            "    \"guid\": \"\"\n" +
            "  },\n" +
            "  \n" +
            "  {\n" +
            "    \"title\": \"Hello US\",\n" +
            "    \"details\": \"Hi, it new value 123}\",\n" +
            "    \"guid\": \"\"\n" +
            "  }\n" +
            "  \n" +
            "]";


    @Mock
    Question question;

    @Mock
    AnswerRepository answerRepository;

    @InjectMocks
    EntityHelper<Answer, Question> answerHelper;

    @Captor
    private ArgumentCaptor<List<Answer>> argumentCaptor;

    @Before
    public void init(){
        mockStatic(FileUtil.class);

        when(FileUtil.getFile(anyString())).thenReturn(answersFileContent.getBytes());
        when(question.getDirectoryPath()).thenReturn("testdir");
    }

    @Test
    public void testSaveAnswers() throws Exception {
        answerHelper.save(question.getDirectoryPath(), question);

        verify(answerRepository, times(1)).save(argumentCaptor.capture());

        assertEquals(3, argumentCaptor.getValue().size());
    }
}