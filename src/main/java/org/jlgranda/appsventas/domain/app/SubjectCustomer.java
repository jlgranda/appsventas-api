/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.DeletableObject;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "subject_customer", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class SubjectCustomer extends DeletableObject implements Comparable<SubjectCustomer>, Serializable {

    @Column(name = "subject_id") 
    private Long subjectId;
    @Column(name = "customer_id")
    private Long customerId;

    @Override
    public int compareTo(SubjectCustomer t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
