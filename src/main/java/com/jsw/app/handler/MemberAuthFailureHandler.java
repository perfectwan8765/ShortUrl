package com.jsw.app.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper mapper;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", false);
        resultMap.put("message", exception.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
        response.setContentType("applicatoin/json;charset=utf-8");

        OutputStream out = response.getOutputStream();
        out.write(mapper.writeValueAsString(resultMap).getBytes());
        out.flush();
        out.close();
    }

}