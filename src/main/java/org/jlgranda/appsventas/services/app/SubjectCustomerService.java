/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.SubjectCustomer;
import org.jlgranda.appsventas.dto.app.SubjectCustomerData;
import org.jlgranda.appsventas.repository.app.SubjectCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class SubjectCustomerService {

    @Autowired
    private SubjectCustomerRepository repository;

    public SubjectCustomerRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>SubjectCustomer</tt> para el subjectId dado
     * como parámentro, discriminando el campo eliminado
     *
     * @param subjectId
     * @return
     */
    public List<SubjectCustomer> encontrarPorSubjectId(Long subjectId) {
        return this.getRepository().encontrarPorSubjectId(subjectId);
    }

    /**
     * Devolver las instancias <tt>SubjectCustomer</tt> para el subjectId y
     * palabra clave dados como parámentros, discriminando el campo eliminado
     *
     * @param subjectId
     * @param keyword
     * @return
     */
    public List<SubjectCustomer> encontrarPorSubjectIdYKeyword(Long subjectId, String keyword) {
        return this.getRepository().encontrarPorSubjectIdYKeyword(subjectId, keyword);
    }

    public SubjectCustomerData buildSubjectCustomerData(Subject s) {
        SubjectCustomerData subjectCustomerData = new SubjectCustomerData();
        subjectCustomerData.setCustomerCode(s == null ? "No definido" : s.getCode());
        subjectCustomerData.setCustomerEmail(s == null ? "No definido" : s.getEmail());
        subjectCustomerData.setCustomerFullName(s == null ? "No definido" : s.getFullName());
        subjectCustomerData.setCustomerInitials(s == null ? "No definido" : s.getInitials());
        return subjectCustomerData;
    }

}
