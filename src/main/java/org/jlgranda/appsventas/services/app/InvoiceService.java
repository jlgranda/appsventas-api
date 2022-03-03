/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.ArrayList;
import java.util.List;
import org.jlgranda.appsventas.domain.app.Detail;
import org.jlgranda.appsventas.domain.app.Invoice;
import org.jlgranda.appsventas.domain.app.Payment;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.dto.app.DetailData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.PaymentData;
import org.jlgranda.appsventas.repository.app.InvoiceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository repository;

    public InvoiceRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>Invoice</tt> para la organizacionId dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<Invoice> encontrarPorOrganizacionId(Long organizacionId) {
        return this.getRepository().encontrarPorOrganizacionId(organizacionId);
    }

    /**
     * Devolver las instancias <tt>Invoice</tt> para la organizacionId y
     * documentType dados como parámentros, discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<Invoice> encontrarPorOrganizacionIdYDocumentType(Long organizacionId, DocumentType documentType) {
        return this.getRepository().encontrarPorOrganizacionIdYDocumentType(organizacionId, documentType);
    }

    public InvoiceData buildInvoiceData(Invoice inv) {
        InvoiceData invoiceData = new InvoiceData();
        BeanUtils.copyProperties(inv, invoiceData);
        return invoiceData;
    }

}
