package com.starlight.huggy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class IndexController {
    @GetMapping("/")
    public String home() {
        return "<h1>HUGGY HOME</h1>";
    }
}
