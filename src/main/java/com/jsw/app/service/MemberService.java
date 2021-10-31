package com.jsw.app.service;

import com.jsw.app.entity.Member;
import com.jsw.app.exception.UserAlreadyExistException;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberService extends UserDetailsService {
    
    Member save (Member member) throws UserAlreadyExistException, IllegalStateException;

    Member updateLastLoginSuccessDate(String email);
}
