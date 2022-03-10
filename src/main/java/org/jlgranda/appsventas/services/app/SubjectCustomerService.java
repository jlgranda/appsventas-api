/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import java.util.UUID;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.domain.StatusType;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.jlgranda.appsventas.dto.app.SubjectCustomerData;
import org.jlgranda.appsventas.repository.app.SubjectCustomerRepository;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.BeanUtils;
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

    public SubjectCustomerData buildSubjectCustomerData(SubjectCustomer sc, Subject c) {
        SubjectCustomerData subjectCustomerData = new SubjectCustomerData();
        BeanUtils.copyProperties(sc, subjectCustomerData);
        subjectCustomerData.setCustomerId(c == null ? null : c.getId());
        subjectCustomerData.setCustomerCode(c == null ? "No definido" : c.getCode());
        subjectCustomerData.setCustomerEmail(c == null ? "No definido" : c.getEmail());
        subjectCustomerData.setCustomerFullName(c == null ? "No definido" : c.getFullName());
        subjectCustomerData.setCustomerInitials(c == null ? "No definido" : c.getInitials());
        subjectCustomerData.setCustomerPhoto(c == null ? null : c.getPhoto() != null ? "data:image/png;" + Base64.toBase64String(c.getPhoto()) : null);
        return subjectCustomerData;
    }

    public SubjectCustomerData buildSubjectCustomerData(Long subjectId, Subject c) {
        SubjectCustomerData subjectCustomerData = new SubjectCustomerData();
        subjectCustomerData.setSubjectId(subjectId);
        subjectCustomerData.setCustomerId(c == null ? null : c.getId());
        subjectCustomerData.setCustomerCode(c == null ? "No definido" : c.getCode());
        subjectCustomerData.setCustomerEmail(c == null ? "No definido" : c.getEmail());
        subjectCustomerData.setCustomerFullName(c == null ? "No definido" : c.getFullName());
        subjectCustomerData.setCustomerInitials(c == null ? "No definido" : c.getInitials());
        subjectCustomerData.setCustomerPhoto(c == null ? null : c.getPhoto() != null ? "data:image/png;base64," + Base64.toBase64String(c.getPhoto()) : null);
        return subjectCustomerData;
    }

    public SubjectCustomer crearInstancia() {
        SubjectCustomer _instance = new SubjectCustomer();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setUuid(UUID.randomUUID().toString());
        return _instance;
    }

    public SubjectCustomer crearInstancia(Subject user) {
        SubjectCustomer _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        _instance.setOwner(user); //Establecer al usuario actual
        return _instance;
    }

    public SubjectCustomer guardar(SubjectCustomer subjectCustomer) {
        return this.getRepository().save(subjectCustomer);
    }

}
