package com.jsw.app.service;

public interface ShortService {
    
    String encodeUrl (String url) throws IllegalArgumentException;
    
    String getUrl (String id);
}
