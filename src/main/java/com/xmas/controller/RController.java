package com.xmas.controller;

import com.xmas.R.service.RService;
import com.xmas.entity.Script;
import com.xmas.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.util.List;

@RestController
@RequestMapping("/R")
public class RController {

    @Autowired
    RService rService;

    @Autowired
    ServletContext context;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Script> getScripts(){
        return rService.getScripts();
    }

    @RequestMapping(value = "/{id}")
    public Script getScript(@PathVariable Integer id){
        return rService.getScript(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Script addScript(@RequestParam MultipartFile file){
        return rService.createScript(file);
    }

    @RequestMapping(value = "/{id}/evaluate", method = RequestMethod.POST)
    public String evaluate(@PathVariable Integer id, @RequestParam MultipartFile input){
        return rService.evaluateScript(id, input);
    }

    @RequestMapping(value = "/data/{dir}/output", method = RequestMethod.GET)
    public List<String> getFiles(@PathVariable String dir){
        return FileUtil.getFiles(context.getRealPath("R/data/" + dir + "/output"));
    }
}
