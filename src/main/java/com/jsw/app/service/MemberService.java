package com.jsw.app.service;

import com.jsw.app.entity.Member;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    
    Integer save (Member member);
}
