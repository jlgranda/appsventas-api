/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jlgranda.appsventas.domain.PersistentObject;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "invoice_detail", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Detail extends PersistentObject implements Comparable<Detail>, Serializable {

    @Column(name = "invoice_id", insertable = true, updatable = true, nullable = true)
    private Long invoiceId;
    @Column(name = "product_id", insertable = true, updatable = true, nullable = false)
    private Long productId;
    private BigDecimal amount;
    private String unit;
    private BigDecimal price;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder(17, 31); // two randomly chosen prime numbers
        // if deriving: appendSuper(super.hashCode()).
        if (getProductId() != null) {
            hcb.append(getInvoiceId()).
                    append(getProductId());
        } else {
            hcb.append(getInvoiceId()).
                    append(getProductId());
        }

        return hcb.toHashCode();
    }

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
        Detail other = (Detail) obj;
        EqualsBuilder eb = new EqualsBuilder();

        if (other.getProductId() != null) {
            eb.append(getProductId(), other.getProductId()).
                    append(getProductId(), other.getProductId()).
                    append(getInvoiceId(), other.getInvoiceId());
        } else {
            eb.append(getProductId(), other.getProductId()).
                    append(getInvoiceId(), other.getInvoiceId());

        }
        return eb.isEquals();
    }

    @Transient
    private boolean showAmountInSummary;

    @Override
    public int compareTo(Detail other) {
        return this.getProductId().compareTo(other.getProductId());
    }

}
