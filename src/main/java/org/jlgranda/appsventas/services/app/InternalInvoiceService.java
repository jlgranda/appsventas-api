/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.StatusType;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.jlgranda.appsventas.domain.app.view.InvoiceCountView;
import org.jlgranda.appsventas.domain.app.view.InvoiceView;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.domain.util.EmissionType;
import org.jlgranda.appsventas.domain.util.EnvironmentType;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.InvoiceDetailData;
import org.jlgranda.appsventas.dto.app.SubjectCustomerData;
import org.jlgranda.appsventas.repository.app.InternalInvoiceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class InternalInvoiceService {

    @Autowired
    private InternalInvoiceRepository repository;

    public InternalInvoiceRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>InternalInvoice</tt> para el uuid dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param uuid
     * @return
     */
    public Optional<InternalInvoice> encontrarPorUuid(String uuid) {
        return this.getRepository().encontrarPorUuid(uuid);
    }

    /**
     * Devolver las instancias <tt>InternalInvoice</tt> para el author, la
     * organizacionId y documentType dados como parámentros, discriminando el
     * campo eliminado
     *
     * @param author
     * @param organizacionId
     * @param documentType
     * @return
     */
    public List<InternalInvoice> encontrarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(Subject author, Long organizacionId, DocumentType documentType) {
        return this.getRepository().encontrarPorAuthorYOrganizacionIdYDocumentType(author, organizacionId, documentType);
    }

    /**
     * Devolver las instancias <tt>InternalInvoice</tt> para el owner, la
     * organizacionId y documentType dados como parámentros, discriminando el
     * campo eliminado
     *
     * @param owner
     * @param organizacionId
     * @param documentType
     * @return
     */
    public List<InternalInvoice> encontrarPorOwnerYOrganizacionIdYDocumentType(Subject owner, Long organizacionId, DocumentType documentType) {
        return this.getRepository().encontrarPorOwnerYOrganizacionIdYDocumentType(owner, organizacionId, documentType);
    }

    /**
     * Devolver las instancias <tt>InternalInvoice</tt> para el author y
     * documentType dados como parámentros, discriminando el campo eliminado
     *
     * @param author
     * @param documentType
     * @return
     */
    public List<InternalInvoice> encontrarPorAuthorYDocumentType(Subject author, DocumentType documentType) {
        return this.getRepository().encontrarPorAuthorYDocumentType(author, documentType);
    }

    /**
     * Devolver las instancias <tt>InternalInvoice</tt> para el owner y
     * documentType dados como parámentros, discriminando el campo eliminado
     *
     * @param owner
     * @param documentType
     * @return
     */
    public List<InternalInvoice> encontrarPorOwnerYDocumentType(Subject owner, DocumentType documentType) {
        return this.getRepository().encontrarPorOwnerYDocumentType(owner, documentType);
    }

    public InvoiceData buildInvoiceData(InternalInvoice inv) {
        InvoiceData internalInvoiceData = new InvoiceData();
        BeanUtils.copyProperties(inv, internalInvoiceData);
        internalInvoiceData.setSubjectFullName(inv.getAuthor() == null ? "No definido" : inv.getAuthor().getFullName());
        return internalInvoiceData;
    }

    public InvoiceData buildInvoiceData(InvoiceView inv) {
        InvoiceData internalInvoiceData = new InvoiceData();
        BeanUtils.copyProperties(inv, internalInvoiceData);
        return internalInvoiceData;
    }
    
    public InvoiceData buildInvoiceData(InternalInvoice inv, Subject c) {
        InvoiceData internalInvoiceData = new InvoiceData();
        BeanUtils.copyProperties(inv, internalInvoiceData);
        internalInvoiceData.setCustomerFullName(c == null ? "No definido" : c.getFullName());
        return internalInvoiceData;
    }

    public InternalInvoice crearInstancia() {
        InternalInvoice _instance = new InternalInvoice();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setEnvironmentType(EnvironmentType.TEST);
        _instance.setEmissionType(EmissionType.SALE);
        _instance.setDocumentType(DocumentType.PRE_INVOICE);
        _instance.setDocumentTypeSource(DocumentType.PRE_INVOICE);
        _instance.setEmissionOn(Dates.now());
        _instance.setUuid(UUID.randomUUID().toString());
        return _instance;
    }

    public InternalInvoice crearInstancia(Subject user) {
        InternalInvoice _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        return _instance;
    }

    public InternalInvoice guardar(InternalInvoice InternalInvoice) {
        return this.getRepository().save(InternalInvoice);
    }

    /**
     * Devolver las instancias <tt>InvoiceCountView</tt> para el author, la
     * organizacionId y documentType dados como parámentros, discriminando el
     * campo eliminado
     *
     * @param authorId
     * @param organizacionId
     * @param documentType
     * @return
     */
    public List<InvoiceCountView> countPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(Long authorId, Long organizacionId, DocumentType documentType) {
        return this.getRepository().countPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(authorId, organizacionId, documentType.ordinal());
    }

    /**
     * Devolver las instancias <tt>InvoiceView</tt> para el author, la
     * organizacionId y documentType dados como parámentros, discriminando el
     * campo eliminado
     *
     * @param authorId
     * @param organizacionId
     * @param documentType
     * @param internalStatus
     * @return
     */
    public List<InvoiceView> listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(Long authorId, Long organizacionId, DocumentType documentType, String internalStatus) {
        if (Constantes.SRI_STATUS_NO_APPLIED.equalsIgnoreCase(internalStatus)) {
            return this.getRepository().listarPorAuthorYOrganizacionIdYDocumentTypeNoApplied(authorId, organizacionId, documentType.ordinal());
        } else {
            return this.getRepository().listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(authorId, organizacionId, documentType.ordinal(), internalStatus);
        }
    }

    /**
     * Devolver las instancias <tt>InvoiceView</tt> para el owner, la
     * organizacionId y documentType dados como parámentros, discriminando el
     * campo eliminado
     *
     * @param authorId
     * @param organizacionId
     * @param documentType
     * @param internalStatus
     * @return
     */
    public List<InvoiceView> listarPorOwnerYDocumentTypeInternalStatus(Long authorId, DocumentType documentType, String internalStatus) {
        return this.getRepository().listarPorOwnerYDocumentTypeInternalStatus(authorId, documentType.ordinal(), internalStatus);
    }
//    /**
//     * Devolver las instancias <tt>InvoiceView</tt> para el owner, la
//     * organizacionId y documentType dados como parámentros, discriminando el
//     * campo eliminado
//     *
//     * @param authorId
//     * @param organizacionId
//     * @param documentType
//     * @param internalStatus
//     * @return
//     */
//    public List<InvoiceView> listarPorOwnerYOrganizacionIdYDocumentTypeInternalStatus(Long authorId, Long organizacionId, DocumentType documentType, String internalStatus) {
//        return this.getRepository().listarPorOwnerYOrganizacionIdYDocumentTypeInternalStatus(authorId, organizacionId, documentType.ordinal(), internalStatus);
//    }

    public Optional<InvoiceView> encontrarPorClaveAcceso(String claveAcceso) {
        return this.getRepository().encontrarPorClaveAcceso(claveAcceso);
    }

}
