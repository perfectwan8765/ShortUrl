package com.jsw.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsw.app.service.ShortService;

@RestController
public class ShortController {
    
    @Autowired
    private ShortService shortService;

    @PostMapping("/short/{url}")
    public ResponseEntity<String> getUrl(@PathVariable("url") String url) {
        return ResponseEntity.ok(shortService.encodeUrl(url));
    }

}