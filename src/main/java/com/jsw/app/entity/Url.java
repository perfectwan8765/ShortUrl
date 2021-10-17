package com.jsw.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="URL")
@SequenceGenerator (
    name = "URL_ID_GENERATOR",
    sequenceName = "URL_SEQ",
    initialValue = 1,
    allocationSize = 1 // default가 50임. 사용할때 조심!!!
)
public class Url {
    
    public Url () {}
    
    public Url (String url, String encodeId, Date createdDate) {
        this.url = url;
        this.encodeId = encodeId;
        this.createdDate = createdDate;
    }
    
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "URL_ID_GENERATOR" )
    private Integer id;
    
    @Column(name="URL", nullable=false, length=4000)
    private String url;
    
    @Column(name="ENC_ID", nullable=false, length=4000)
    private String encodeId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

}