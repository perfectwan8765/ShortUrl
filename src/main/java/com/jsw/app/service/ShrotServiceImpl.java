package com.jsw.app.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsw.app.entity.Url;
import com.jsw.app.repository.UrlRepository;
import com.jsw.app.util.Base10Util;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShrotServiceImpl implements ShortService {
    
    @PersistenceContext
    EntityManager em;
    
    @Autowired
    private UrlRepository urlRepo;
    
    @Autowired
    private Base10Util encodeUtil;
    
    @Transactional
    public String encodeUrl (String url) {
        Integer lastId = urlRepo.getNextUrlId();
        lastId = lastId == null ? 1 : lastId + 1;
        
        String encodeId = encodeUtil.fromBase10(lastId);
        
        log.info("Id: {}, Url:{}, encodeId:{}", lastId, url, encodeId);
        Url urlEntity = new Url(url, encodeId, new Date());
        
        em.persist(urlEntity);
        
        return encodeId;
    }

}
