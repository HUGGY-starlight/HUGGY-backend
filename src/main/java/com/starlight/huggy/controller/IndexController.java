package com.starlight.huggy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/home")
public class IndexController {
    @GetMapping("/")
    public String home() {
        return "<h1>HUGGY HOME</h1>";
    }
}
