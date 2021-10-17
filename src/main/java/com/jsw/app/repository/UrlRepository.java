package com.jsw.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jsw.app.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Integer> {
    
    @Query(value = "SELECT id FROM Url ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public Integer getNextUrlId();
}