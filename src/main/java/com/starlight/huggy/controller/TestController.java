package com.starlight.huggy.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("token")
    public String token(){
        return "<h1>home</h1>";
    }
}
