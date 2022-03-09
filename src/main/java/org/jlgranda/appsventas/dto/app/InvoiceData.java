/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.dto.app;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private Date emissionOn;
    private BigDecimal amount;
    private BigDecimal subTotal;
    private BigDecimal descuento;
    private BigDecimal iva0Total;
    private BigDecimal iva12Total;
    private Boolean iva12;
    private BigDecimal propina;
    private BigDecimal importeTotal;

    private SubjectCustomerData subjectCustomer;
    private ProductData product;

    private List<DetailData> details;
    private List<PaymentData> payments;

    private String customerFullName;
    private String subjectFullName;

    private Boolean enviarSRI = false;
    private String accionSRI = "enviar";

}
