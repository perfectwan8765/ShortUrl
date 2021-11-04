package com.jsw.app.service;

import com.jsw.app.entity.Member;
import com.jsw.app.entity.Url;
import com.jsw.app.exception.UserAlreadyExistException;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    
    Member save (Member member) throws UserAlreadyExistException, IllegalStateException;

    Member updateLastLoginSuccessDate(String email);

    Page<Url> getMemberUrlList(int page, int size);
}
