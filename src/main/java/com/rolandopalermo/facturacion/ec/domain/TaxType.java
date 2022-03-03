package com.rolandopalermo.facturacion.ec.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "sri_tax_type")
public class TaxType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_type_id", updatable = false, nullable = false)
    private long id;

    @Column
    private String code;

    @Column
    private String description;

}