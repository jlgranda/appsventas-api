/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.repository.app.InternalInvoiceRepository;
import org.jlgranda.appsventas.repository.app.InvoiceRepository;
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
     * Devolver las instancias <tt>Invoice</tt> para la organizacionId dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<InternalInvoice> encontrarPorOrganizacionId(Long organizacionId) {
        return this.getRepository().encontrarPorOrganizacionId(organizacionId);
    }

    /**
     * Devolver las instancias <tt>Invoice</tt> para la organizacionId y
     * documentType dados como parámentros, discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<InternalInvoice> encontrarPorOrganizacionIdYDocumentType(Long organizacionId, DocumentType documentType) {
        return this.getRepository().encontrarPorOrganizacionIdYDocumentType(organizacionId, documentType);
    }

    public InvoiceData buildInvoiceData(InternalInvoice inv) {
        InvoiceData invoiceData = new InvoiceData();
        BeanUtils.copyProperties(inv, invoiceData);
        return invoiceData;
    }

}
