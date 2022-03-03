/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.DeletableObject;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.domain.util.EmissionType;
import org.jlgranda.appsventas.domain.util.EnvironmentType;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "invoice", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class InternalInvoice extends DeletableObject<InternalInvoice> implements Serializable {

    public static final double IVA = 0d;
    private String boardNumber;
    private EnvironmentType environmentType;
    private EmissionType emissionType;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "emissionOn")
    private Date emissionOn;
    @Column(name = "organization_id", insertable = true, updatable = true, nullable = true)
    private Long organizacionId;
    private DocumentType documentType;
    private DocumentType documentTypeSource;
    @Column(name = "establishment_id", insertable = false, updatable = false, nullable = true)
    private Long establishmentId;
    @Column(name = "emissionpoint_id", insertable = false, updatable = false, nullable = true)
    private Long emissionpointId;
    private String sequencial;
    @Column(nullable = true)
    private Boolean printAlias;
    @Column(nullable = true, length = 1024)
    protected String printAliasSummary;
    @Column(nullable = true, length = 1024)
    protected Long pax;
    @Column(name = "record_id", nullable = true)
    private Long recordId;

}