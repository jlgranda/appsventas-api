/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.dto.app;

import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.domain.util.EmissionType;
import org.jlgranda.appsventas.domain.util.EnvironmentType;

/**
 *
 * @author usuario
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class InvoiceData {

    private Long id;
    private String uuid;

    public static final double IVA = 0d;
    private String boardNumber;
    private EnvironmentType environmentType;
    private EmissionType emissionType;
    private Date emissionOn;
    private Long organizacionId;
    private DocumentType documentType;
    private DocumentType documentTypeSource;
    private Long establishmentId;
    private Long emissionpointId;
    private String sequencial;
    private Boolean printAlias;
    protected String printAliasSummary;
    protected Long pax;
    private Long recordId;

    private List<DetailData> details;
    private List<PaymentData> payments;

}
