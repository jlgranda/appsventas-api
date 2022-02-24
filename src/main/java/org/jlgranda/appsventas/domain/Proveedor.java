/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "Proveedor", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Proveedor extends DeletableObject implements Comparable<Proveedor>, Serializable {

    private static final long serialVersionUID = -1016927888119107404L;

    @Column(name = "credito_maximo_monto")
    private Long creditoMaximoMonto;

    @Column(name = "credito_maximo_dias")
    private Long creditoMaximoDias;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_preferida_pago")
    private Date fechaPreferidaPago;
    
    @Column(name = "organization_id", insertable = true, updatable = true, nullable = true)
    private Long organizacionId;

//    @ManyToOne(optional = true)
//    @JoinColumn(name = "organization_id", insertable = true, updatable = true, nullable = true)
//    private Organization organization;
//
    @Override
    public int compareTo(Proveedor t) {
        if (getId() == null && t != null && t.getId() != null) {
            return -1;
        }
        if (getId() != null && t == null) {
            return 1;
        }
        return getId().compareTo(t.getId());
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }

}
