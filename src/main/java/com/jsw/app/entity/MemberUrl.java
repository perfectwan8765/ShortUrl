package com.jsw.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="MEMBER_URL")
@SequenceGenerator (
    name = "MEMBER_URL_ID_GENERATOR",
    sequenceName = "MEMBER_URL_SEQ",
    initialValue = 1,
    allocationSize = 1 // default가 50임. 사용할때 조심!!!
)
public class MemberUrl {

    public MemberUrl () {}

    public MemberUrl (Member member, Url url, Date createdDate) {
        this.member = member;
        this.url = url;
        this.createdDate = createdDate;
    }
    
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_URL_ID_GENERATOR" )
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="URL_ID", nullable = false)
    private Url url;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}