package com.rolandopalermo.facturacion.ec.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseSRIEntity {

    @Column
    @NaturalId
    private String accessKey;

    @Column
    private String sriVersion;

    @Column
    private Date issueDate;

    @Column
    @Type(type = "XMLType")
    private String xmlContent;

    @Column
    private Timestamp authorizationDate;

    @Column
    private long internalStatusId;

    @Column
    @Type(type = "XMLType")
    private String xmlAuthorization;

    @Column
    private boolean isDeleted;

}