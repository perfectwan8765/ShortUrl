package com.jsw.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsw.app.util.Base10Util;

@Service
public class ShrotServiceImpl implements ShortService {
    
    private static final Logger log = LoggerFactory.getLogger(ShrotServiceImpl.class);
    
    @Autowired
    private Base10Util encodeUtil;
    
    public String encodeUrl (String url) {
        log.info("url:{}", url);
        String result = encodeUtil.fromBase10(url.length());
        log.info("resutl:{}", result);
        return result;
    }

}
