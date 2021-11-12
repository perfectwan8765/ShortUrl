package com.jsw.app.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import com.jsw.app.entity.Member;
import com.jsw.app.entity.MemberUrl;
import com.jsw.app.entity.Url;
import com.jsw.app.repository.MemberRepository;
import com.jsw.app.repository.MemberUrlRepository;
import com.jsw.app.repository.UrlRepository;
import com.jsw.app.util.Base10Util;
import com.jsw.app.util.facade.MemberAuthenticationFacade;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ShrotServiceImpl implements ShortService {
    
    //@PersistenceContext
    //EntityManager em;
    
    @Autowired
    private UrlRepository urlRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private MemberUrlRepository memberUrlRepo;
    
    @Autowired
    private Base10Util encodeUtil;

    @Autowired
    private UrlValidator urlValidator;

    @Autowired
    private MemberAuthenticationFacade authenticationFacade;
    
    /**      
     *  User가 원하는 Url를 저장 -> Id encoding한 후 저장
     *  @author jsw
     *  @param url User가 Short하길 원하는 Url
     *  @return String Id를 Encoding한 값
     */
    @Transactional
    public String encodeUrl (String urlStr) throws IllegalArgumentException {
        // Url Valid Check
        if (!urlValidator.isValid(urlStr)) {
            log.error("Input Invalid URL");
            throw new IllegalArgumentException("Invalid URL");
        }

        Url url = urlRepo.findByUrl(urlStr);
        
        if (url == null) {
            int newId = Optional.ofNullable(urlRepo.getNextUrlId()).orElseGet(() -> 0) + 1;          
            String encodeId = encodeUtil.fromBase10(newId);
            url = new Url(urlStr, encodeId, new Date());
            urlRepo.save(url);
        }
        // When member login, insert MemberUrl Table 
        insertMemberUrl(url);

        log.info("Get Url, Id: {}, Url:{}, encodeId:{}", url.getId(), url.getUrl(), url.getEncodeId());
        
        return url.getEncodeId();
    }
    
    /**      
     *  Id로 조회해서 Redirect해줄 URL 주소 Get
     *  @author jsw
     *  @param id Redirect Url 찾을 Id
     *  @return Redirect할 URL
     */
    public String getUrl (String id) {
        Url url = urlRepo.findByEncodeId(id);
        
        if (url == null) {
            log.error("No Have Id");
            return null;
        }

        log.info("Exists Url For Redirect, Id:{}, Url:{}, encodeId:{}", url.getId(), url.getUrl(), url.getEncodeId());
        
        return url.getUrl();
    }

    private void insertMemberUrl (Url url) {
        // Member Login Check
        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            log.info("Not Login Status");
            return;
        }
        
        // Check Exists Member
        Optional<Member> memberWrapper = memberRepo.findByEmail(authentication.getName());
        if (!memberWrapper.isPresent()) {
            log.error("Member Not Exists");
            return;
        }

        Member member = memberWrapper.get();

        // Check register Url of Member
        List<Member> memberList = memberRepo.findByMemberUrls_Id(url.getId());
        if (memberList.indexOf(member) != -1) {
            log.info("Already register Url of Member({}): {}", member.getEmail(), url.getUrl());
            return;
        }

        // Register Url of Member
        MemberUrl memberUrl = new MemberUrl(member, url, new Date());
        memberUrlRepo.save(memberUrl);
    }

}