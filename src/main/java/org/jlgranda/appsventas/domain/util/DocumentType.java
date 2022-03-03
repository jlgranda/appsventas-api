/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.util;

/**
 *
 * @author usuario
 */
public enum DocumentType {

    /**
     * Factura
     */
    UNDEFINED,
    PRE_INVOICE,
    CANCELED_INVOICE,
    INVOICE,
    OVERDUE,
    CREDIT_NOTE,
    DEBIT_NOTE,
    REMISSION_GUIDE,
    RETENTION_SUPPORTING,
    FAX,
    CATALOGO,
    REVISTA,
    OFICIO,
    SOLICITUD,
    DIGITALIZADO,
    COURTESY;
}
