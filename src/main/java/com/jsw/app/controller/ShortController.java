package com.jsw.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortController {
    
    @RequestMapping
    public String hello() {
        return "Hello";
    }

}