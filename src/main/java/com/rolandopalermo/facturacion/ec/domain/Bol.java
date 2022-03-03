package com.rolandopalermo.facturacion.ec.domain;

import com.rolandopalermo.facturacion.ec.domain.type.XMLType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sri_bol")
@TypeDefs(value = {@TypeDef(name = "XMLType", typeClass = XMLType.class)})
public class Bol extends BaseSRIEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bol_generator")
    @SequenceGenerator(name = "bol_generator", sequenceName = "sri_bol_seq", allocationSize = 50)
    @Column(name = "bol_id", updatable = false, nullable = false)
    private long bolId;

    @Column
    private String supplierId;

    @Column
    private String bolNumber;

    @Column
    private String shipperRuc;

    @Column
    private String registrationNumber;

    @OneToMany(mappedBy = "bol", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consignee> consignees = new ArrayList<>();

}