/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jlgranda.appsventas.domain.DeletableObject;

/**
 *
 * @author usuario
 */

@Entity
@Table(name = "PAYMENT", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Payment extends DeletableObject<Payment> implements Comparable<Payment>, Serializable {

    @Column(name = "invoice_id", insertable = true, updatable = true, nullable = true)
    private Long invoiceId;
    @Column(name = "factura_electronica_id", insertable = true, updatable = true, nullable = true)
    private Long facturaElectronicaId;
    private String method;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal cash;
    private BigDecimal change;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = true)
    protected Date datePaymentCancel;
    @Column(name = "record_id", nullable = true)
    private Long recordId;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Payment other = (Payment) obj;
        EqualsBuilder eb = new EqualsBuilder();

        eb.append(getId(), other.getId());
//        eb.append(getId(), other.getId()).
//                    append(getInvoice(), other.getInvoice());
        return eb.isEquals();
    }

    public int compareTo(Payment other) {
        return this.createdOn.compareTo(other.getCreatedOn());
    }
}
