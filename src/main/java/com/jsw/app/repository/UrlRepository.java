package com.jsw.app.repository;

import com.jsw.app.entity.Url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UrlRepository extends JpaRepository<Url, Integer> {
    
    @Query(value = "SELECT id FROM Url ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Integer getNextUrlId();
    
    Url findByUrl(String url);
    
    Url findByEncodeId(String encodeId);
}