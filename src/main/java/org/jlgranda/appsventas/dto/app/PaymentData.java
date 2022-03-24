/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.dto.app;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author usuario
 */
public class PaymentData {

    private Long id;
    private String uuid;

    private Long invoiceId;
    private String method;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal cash;
    private BigDecimal change;
    protected Date datePaymentCancel;
}
