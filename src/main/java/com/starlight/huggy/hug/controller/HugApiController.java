package com.starlight.huggy.hug.controller;

import com.starlight.huggy.hug.service.HugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HugApiController {
    @Autowired
    private HugService hugService;

}
