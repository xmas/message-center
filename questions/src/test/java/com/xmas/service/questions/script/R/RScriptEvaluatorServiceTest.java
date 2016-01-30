package com.xmas.service.questions.script.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RScriptEvaluatorServiceTest {

    String script;
    String dir;

    RConnectionManager connectionManager;

    @InjectMocks
    RScriptEvaluatorService evaluatorService;

    @Before
    public void setUp() throws Exception {
        script ="dat <- readLines(paste(dir, \"input/input.txt\", sep=\"/\"))\n" +
                "png(paste(dir, \"output/output.png\", sep=\"/\"), width = 800, height = 1100)\n" +
                "barplot(table(dat))\n" +
                "dev.off()";

        dir = this.getClass().getResource("/R").getPath() + "/data/123456";

        connectionManager = new RConnectionManager();
        connectionManager.setRserveHomeBin("/usr/lib/R/site-library/Rserve/libs//Rserve");
        evaluatorService.setConnectionManager(connectionManager);
    }

    @Test
    public void testEvaluateScript() throws Exception {
        evaluatorService.evaluate(script, dir, Collections.emptyMap());

        assertTrue(new File(dir + "/output/output.png").exists());
    }
}