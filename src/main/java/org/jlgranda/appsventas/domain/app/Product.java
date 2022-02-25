/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.DeletableObject;
import org.jlgranda.appsventas.domain.util.ProductType;
import org.jlgranda.appsventas.domain.util.Statistics;
import org.jlgranda.appsventas.domain.util.TaxType;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "Product", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Product extends DeletableObject<Product> implements Serializable {

    @Column
    private String icon;
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal priceB;
    @Column
    private BigDecimal priceC;
    @Column
    private BigDecimal priceCost;
    @Column
    private ProductType productType;
    @Column
    private TaxType taxType;
    @Column(length = 1024)
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;
    @Column(name = "organization_id", insertable = true, updatable = true, nullable = true)
    private Long organizacionId;
    @Column(name = "group_id", insertable = true, updatable = true, nullable = true)
    private Long categoryId;
    @Transient
    private Statistics statistics = new Statistics();

    public Product() {
        icon = "fa fa-question-circle "; //icon by default
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }

}
