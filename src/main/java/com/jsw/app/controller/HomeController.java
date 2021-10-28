package com.jsw.app.controller;

import java.net.URI;
import java.net.URISyntaxException;

import com.jsw.app.entity.Member;
import com.jsw.app.exception.UserAlreadyExistException;
import com.jsw.app.service.MemberService;
import com.jsw.app.service.ShortService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    
    @Autowired
    private ShortService shortService;

    @Autowired
    private MemberService memberService;
    
    @RequestMapping("/redirect/{id}")
    public ResponseEntity<Object> redirect(@PathVariable("id") String id) throws URISyntaxException {
        String url = shortService.getUrl(id);

        if (url == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Have Url");
        }
        
        URI redirectUri = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(httpHeaders).build();
    }

    @PostMapping("/member/login")
    public String login() {
        return "/";
    }

    @PostMapping("/member/new")
    public ResponseEntity<Object> addMember(Member member) {
        try {
            memberService.save(member);
        } catch (UserAlreadyExistException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        
        return ResponseEntity.ok(member.getEmail());
    }
}
