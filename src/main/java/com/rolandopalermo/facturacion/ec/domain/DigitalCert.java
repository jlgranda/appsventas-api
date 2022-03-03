package com.rolandopalermo.facturacion.ec.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "sri_digital_cert")
public class DigitalCert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "digital_cert_generator")
    @SequenceGenerator(name = "digital_cert_generator", sequenceName = "sri_digital_cert_seq", allocationSize = 50)
    @Column(name = "digital_cert_id", updatable = false, nullable = false)
    private long id;

    @Column
    @ColumnTransformer(
            read = "pgp_sym_decrypt("
                    + "password::bytea, "
                    + "current_setting('encrypt.key')"
                    + ")",
            write = "pgp_sym_encrypt("
                    + "?, "
                    + "current_setting('encrypt.key')"
                    + ")"
    )
    private String password;

    @Column
    private byte[] digitalCert;

    @Column
    private String owner;

    @Column
    private boolean active;

    @Column
    private Date insertDate;

}