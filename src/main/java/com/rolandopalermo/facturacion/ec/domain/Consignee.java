package com.rolandopalermo.facturacion.ec.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "sri_consignne")
public class Consignee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consignne_generator")
    @SequenceGenerator(name = "consignne_generator", sequenceName = "sri_consignne_seq", allocationSize = 50)
    @Column(name = "consignneId", updatable = false, nullable = false)
    private long consignneId;

    @Column
    private String consignneNumber;

    @Column
    private String customDocNumber;

    @Column
    private String referenceDocCod;

    @Column
    private String referenceDocNumber;

    @Column
    private String referenceDocAuthNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_key", referencedColumnName = "accessKey")
    private Bol bol;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Consignee))
            return false;
        return consignneId == (((Consignee) o).consignneId);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}