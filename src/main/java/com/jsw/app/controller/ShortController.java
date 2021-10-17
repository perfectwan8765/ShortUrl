package com.jsw.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsw.app.service.ShortService;

@RestController
public class ShortController {
    
    @Autowired
    private ShortService shortService;
    
    @RequestMapping("/")
    public String hello() {
        return "Hello";
    }
    
    @GetMapping("/short/{url}")
    public ResponseEntity<String> getUrl(@PathVariable("url") String url) {
        return ResponseEntity.ok(shortService.encodeUrl(url));
    }

}