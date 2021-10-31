package com.jsw.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jsw.app.entity.Member;
import com.jsw.app.exception.UserAlreadyExistException;
import com.jsw.app.repository.MemberRepository;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailValidator emailValidator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> memberWrapper = memberRepository.findByEmail(email);
        Member member = memberWrapper.orElseThrow(() -> new UsernameNotFoundException("No found Email:" + email));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        member.setAuthorities(authorities);

        return member;
    }

    @Transactional
    @Override
    public Member save(Member member) throws UserAlreadyExistException, IllegalStateException {
        // Email Valid Check
        if (!emailValidator.isValid(member.getEmail())) {
            throw new IllegalStateException("There is invalid the format of email: " + member.getEmail());
        }

        // Exists Email Check
        if (emailExist(member.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + member.getEmail());
        }

        log.info("Insert new Member:{}", member);
        member.setLastAccessDate(new Date());
        member.setCreatedDate(new Date());

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    private boolean emailExist(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional
    @Override
    public Member updateLastLoginSuccessDate(String email) {
        Member member = memberRepository.findByEmail(email).get();

        member.setLastAccessDate(new Date());
        em.persist(member);

        return member;
    }
}
