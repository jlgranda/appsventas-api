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

    @Column ( name="access_key" )
    @NaturalId
    private String accessKey;

    @Column ( name="sri_version" )
    private String sriVersion;

    @Column ( name="issue_date" )
    private Date issueDate;

    @Column ( name="xml_content" )
    @Type(type = "XMLType")
    private String xmlContent;

    @Column ( name="authorization_date" )
    private Timestamp authorizationDate;

    @Column ( name="internal_status_id" )
    private long internalStatusId;

    @Column ( name="xml_authorization" )
    @Type(type = "XMLType")
    private String xmlAuthorization;

    @Column ( name="is_deleted" )
    private boolean isDeleted;

}