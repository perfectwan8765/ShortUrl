package com.jsw.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsw.app.service.ShortService;

@Controller
public class ShortController {
    
    @Autowired
    private ShortService shortService;

    @ResponseBody
    @PostMapping("/short")
    public ResponseEntity<String> getUrl(@RequestParam("url") String url) {
        return ResponseEntity.ok(shortService.encodeUrl(url));
    }

}