package com.AntiSolo.AntiSolo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestingController {
    @GetMapping("/hello")
    public String greet(){
        return "Hello world!";
    }
}
