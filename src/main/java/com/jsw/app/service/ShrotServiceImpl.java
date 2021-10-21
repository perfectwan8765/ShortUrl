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
    
    /**      
     *  User가 원하는 Url를 저장 -> Id encoding한 후 저장
     *  @author jsw
     *  @param url User가 Short하길 원하는 Url
     *  @return String Id를 Encoding한 값
     */
    @Transactional
    public String encodeUrl (String url) {
        Url urlByUrl = urlRepo.findByUrl(url);
        
        if (urlByUrl == null) {
            Integer lastId = urlRepo.getNextUrlId();
            lastId = lastId == null ? 1 : lastId + 1;
            
            String encodeId = encodeUtil.fromBase10(lastId);
            
            log.info("Insert Id: {}, Url:{}, encodeId:{}", lastId, url, encodeId);
            Url urlEntity = new Url(url, encodeId, new Date());
            
            em.persist(urlEntity);
            
            return encodeId;
        }

        log.info("Already Exists, Id: {}, Url:{}, encodeId:{}", urlByUrl.getId(), urlByUrl.getUrl(), urlByUrl.getEncodeId());
        
        return urlByUrl.getEncodeId();
    }
    
    public String getUrl (String id) {
        Url url = urlRepo.findByEncodeId(id);
        
        if (url == null) {
            log.error("No Have Id");
            return null;
        }

        log.info("Exists Url, Id:{}, Url:{}, encodeId:{}", url.getId(), url.getUrl(), url.getEncodeId());
        
        return url.getUrl();
    }

}