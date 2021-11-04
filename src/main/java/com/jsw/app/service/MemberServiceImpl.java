package com.jsw.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.jsw.app.entity.Member;
import com.jsw.app.entity.Url;
import com.jsw.app.exception.UserAlreadyExistException;
import com.jsw.app.repository.MemberRepository;
import com.jsw.app.repository.UrlRepository;
import com.jsw.app.util.facade.MemberAuthenticationFacade;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    //@PersistenceContext
    //EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UrlRepository urlRepository;

    //@Autowired
    //private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private MemberAuthenticationFacade authenticationFacade;

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
        memberRepository.save(member);

        return member;
    }

    @Transactional
    @Override
    public Page<Url> getMemberUrlList(int page, int size) {
        // Member Login Check
        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            log.info("Not Login Status");
            return null;
        }

        log.info("page:{}, size:{}", page, size);

        Member member = memberRepository.findByEmail(authentication.getName()).get();
        Page<Url> urlList = urlRepository.findMemberUrl(member.getId(), PageRequest.of(page, size));

        log.info("Url of {} / SIZE: {}", member.getEmail(), urlList.getNumberOfElements());

        return urlList;
    }

}
