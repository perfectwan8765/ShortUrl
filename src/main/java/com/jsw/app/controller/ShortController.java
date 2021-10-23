package com.jsw.app.controller;

import com.jsw.app.service.ShortService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShortController {
    
    @Autowired
    private ShortService shortService;

    @ResponseBody
    @PostMapping("/short")
    public ResponseEntity<Object> getUrl(@RequestParam("url") String url) {
        try {
            return ResponseEntity.ok(shortService.encodeUrl(url));
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        }
    }

}