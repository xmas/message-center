package com.xmas.util.script;

import com.xmas.exceptions.BadRequestException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static junit.framework.TestCase.assertEquals;


public class ScriptFileUtilTest {

    MultipartFile emptyScriptFile;
    MultipartFile scriptWithoutHeaderFile;
    MultipartFile scriptWithWrongHeaderFile;

    private final String scriptWithoutHeaderContent = "var jsforce = require('jsforce');\n" +
            "var fs = require('fs');\n\n" +
            "var args = [];\n\n" +
            "process.argv.forEach(function (val, index, array) {\n" +
            "    args[index] = val;\n});";
    private final String scriptWithWrongHeaderContent = "#!/usr/bin/lib/node\nvar jsforce = require('jsforce');\n" +
            "var fs = require('fs');\n\n" +
            "var args = [];\n\n" +
            "process.argv.forEach(function (val, index, array) {\n" +
            "    args[index] = val;\n});";
    private final String scriptWithRightHeaderContent = "#!/usr/bin/nodejs\nvar jsforce = require('jsforce');\n" +
            "var fs = require('fs');\n\n" +
            "var args = [];\n\n" +
            "process.argv.forEach(function (val, index, array) {\n" +
            "    args[index] = val;\n});";

    @Before
    public void setUp() throws Exception {
        emptyScriptFile = new MockMultipartFile("script.js", "".getBytes());
        scriptWithoutHeaderFile = new MockMultipartFile("script.js", scriptWithoutHeaderContent.getBytes());
        scriptWithWrongHeaderFile = new MockMultipartFile("script.js", scriptWithWrongHeaderContent.getBytes());
    }

    @Test
    public void testGetScript() throws Exception {

    }

    @Test
    public void testReplaceScript() throws Exception {

    }

    @Test(expected = BadRequestException.class)
    public void testSaveScriptEmptyFile() throws Exception {
        ScriptFileUtil.saveScript("scriptTestDir", emptyScriptFile, ScriptType.NODE);
    }

    @Test
    public void testSaveScriptWithoutHeaderFile() throws Exception {
        ScriptFileUtil.saveScript("scriptTestDir", scriptWithoutHeaderFile, ScriptType.NODE);
        assertEquals(scriptWithRightHeaderContent, FileUtils.readFileToString(new File("scriptTestDir/script/script.sc")));
    }
    @Test
    public void testSaveScriptWithWrongHeaderFile() throws Exception {
        ScriptFileUtil.saveScript("scriptTestDir", scriptWithWrongHeaderFile, ScriptType.NODE);
        assertEquals(scriptWithRightHeaderContent, FileUtils.readFileToString(new File("scriptTestDir/script/script.sc")));
    }
}