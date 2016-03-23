package com.xmas.util.script.pyton;

import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

@Service
public class PythonInterpreterManager {

    private PythonInterpreter interpreter;

    public PythonInterpreter getInterpreter(){
        if(interpreter == null)
            createInterpreter();
        return interpreter;
    }

    private void createInterpreter(){
        interpreter = new PythonInterpreter();
    }

}
