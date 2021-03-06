package com.jsw.app.repository;

import com.jsw.app.entity.Url;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UrlRepository extends JpaRepository<Url, Integer> {
    
    @Query(value = "SELECT id FROM Url ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Integer getNextUrlId();
    
    Url findByUrl(String url);
    
    Url findByEncodeId(String encodeId);

    @Query(value = "SELECT * FROM Url u WHERE EXISTS (SELECT 1 FROM Member_Url mu WHERE mu.url_id = u.id AND mu.member_id = :memberId)",
        countQuery = "SELECT count(1) FROM Url u WHERE EXISTS (SELECT 1 FROM Member_Url mu WHERE mu.url_id = u.id AND mu.member_id = :memberId)",
        nativeQuery = true)
    Page<Url> findMemberUrl(@Param("memberId") Integer memberId, Pageable pageable);

}