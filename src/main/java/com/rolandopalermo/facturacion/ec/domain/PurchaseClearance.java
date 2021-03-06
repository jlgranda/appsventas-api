package com.rolandopalermo.facturacion.ec.domain;

import com.rolandopalermo.facturacion.ec.domain.type.XMLType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "sri_purchase_clearance")
@TypeDefs(value = {@TypeDef(name = "XMLType", typeClass = XMLType.class)})
public class PurchaseClearance extends BaseSRIEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_clearance_generator")
    @SequenceGenerator(name = "purchase_clearance_generator", sequenceName = "sri_purchase_clearance_seq", allocationSize = 50)
    @Column(name = "purchase_clearance_id", updatable = false, nullable = false)
    private long purchaseClearanceId;

    @Column ( name="supplier_id" )
    private String supplierId;

    @Column ( name="customer_id" )
    private String customerId;

    @Column ( name="purchase_clearance_number" )
    private String purchaseClearanceNumber;

}