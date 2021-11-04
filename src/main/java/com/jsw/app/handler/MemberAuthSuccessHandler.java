package com.jsw.app.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.app.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberService memberService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Update Last Login Date after Login Success
        memberService.updateLastLoginSuccessDate(authentication.getName());

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", true);
        resultMap.put("member", authentication.getDetails());

        response.setStatus(HttpStatus.OK.value()); // 200
        response.setContentType("applicatoin/json;charset=utf-8");

        OutputStream out = response.getOutputStream();
        out.write(mapper.writeValueAsString(resultMap).getBytes());
        out.flush();
        out.close();
    }

}