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
@Table(name = "sri_debit_memo")
@TypeDefs(value = {@TypeDef(name = "XMLType", typeClass = XMLType.class)})
public class DebitMemo extends BaseSRIEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "debit_memo_generator")
    @SequenceGenerator(name = "debit_memo_generator", sequenceName = "sri_debit_memo_seq", allocationSize = 50)
    @Column(name = "debit_memo_id", updatable = false, nullable = false)
    private long debitMemoId;

    @Column (name = "supplier_id")
    private String supplierId;

    @Column (name = "customer_id")
    private String customerId;

    @Column (name = "debit_memo_number")
    private String debitMemoNumber;

}