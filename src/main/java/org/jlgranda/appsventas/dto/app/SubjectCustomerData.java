/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.dto.app;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author usuario
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class SubjectCustomerData {

    private Long id;
    private String uuid;

    private Long subjectId;
    private Long customerId;

    //Auxiliares
    private SubjectData customer;
    private String customerCode;
    private String customerFullName;
    private String customerInitials;
    private String customerEmail;
    private String customerPhoto;

}
