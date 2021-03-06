package com.xmas.controller.push;

import com.xmas.dao.push.MediumsRepository;
import com.xmas.entity.push.Medium;
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
