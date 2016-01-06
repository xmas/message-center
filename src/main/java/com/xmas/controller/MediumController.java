package com.xmas.controller;

import com.xmas.dao.MediumsRepository;
import com.xmas.entity.Medium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mediums")
public class MediumController {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    MediumsRepository mediumsRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Medium> getMediums(){
        return mediumsRepository.findAll();
    }
}
