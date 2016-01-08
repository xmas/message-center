package com.xmas.service.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ScriptEvaluatorServiceTest {

    String script;
    String dir;

    RConnectionManager connectionManager;

    @InjectMocks
    ScriptEvaluatorService evaluatorService;

    @Before
    public void setUp() throws Exception {
        script ="dat <- readLines(paste(dir, \"input/input.txt\", sep=\"/\"))\n" +
                "png(paste(dir, \"output/output.png\", sep=\"/\"), width = 800, height = 1100)\n" +
                "barplot(table(dat))\n" +
                "dev.off()";

        dir = "/media/vdanyliuk/PROGRAMING/PROJECTS/UpWork/message-service/target/test-classes/R/data/123456";

        connectionManager = new RConnectionManager();
        connectionManager.setRserveHomeBin("/usr/lib/R/site-library/Rserve/libs//Rserve");
        evaluatorService.setConnectionManager(connectionManager);
    }

    @Test
    public void testEvaluateScript() throws Exception {
        evaluatorService.evaluateScript(script, dir);

        assertTrue(new File(dir + "/output/output.png").exists());
    }
}