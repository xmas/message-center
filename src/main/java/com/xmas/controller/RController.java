package com.xmas.controller;

import com.xmas.R.service.RService;
import com.xmas.entity.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/R/")
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
    public void addScript(@RequestBody Script script){
        rService.saveScript(script);
    }

    public void evaluate(){

    }
}
