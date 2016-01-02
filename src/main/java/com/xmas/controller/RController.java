package com.xmas.controller;

import com.xmas.R.service.RService;
import com.xmas.entity.Script;
import com.xmas.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/R")
public class RController {

    @Autowired
    RService rService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Script> getScripts(){
        return rService.getScripts();
    }

    @RequestMapping(value = "/{id}")
    public Script getScript(@PathVariable Integer id){
        return rService.getScript(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Script addScript(@RequestParam MultipartFile file,
                            @RequestParam(required = false, defaultValue = "script") String name,
                            @RequestParam(required = false, defaultValue = "") String description){
        return rService.createScript(file, name, description);
    }

    @RequestMapping(value = "/{id}/evaluate", method = RequestMethod.POST)
    public String evaluate(@PathVariable Integer id, @RequestParam MultipartFile input){
        return rService.evaluateScript(id, input);
    }

    @RequestMapping(value = {"/data/{dir}", "/data/{dir}/output", "/data/{dir}/input"}, method = RequestMethod.GET)
    public List<String> getFiles(HttpServletRequest request){
        return FileUtil.getFiles(this.getClass().getResource(request.getServletPath()).getPath());
    }
}
