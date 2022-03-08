/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.api.controller.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Detail;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.app.Payment;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.DetailData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.PaymentData;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.services.app.DetailService;
import org.jlgranda.appsventas.services.app.InternalInvoiceService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.PaymentService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author usuario
 */
@RestController
@RequestMapping(path = "/facturacion")
public class FacturacionController {

    private InternalInvoiceService invoiceService;
    private DetailService detailService;
    private PaymentService paymentService;
    private OrganizationService organizationService;
    private SubjectService subjectService;
    private final String ignoreProperties;

    @Autowired
    public FacturacionController(
            InternalInvoiceService invoiceService,
            DetailService detailService,
            PaymentService paymentService,
            OrganizationService organizationService,
            SubjectService subjectService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.invoiceService = invoiceService;
        this.detailService = detailService;
        this.paymentService = paymentService;
        this.organizationService = organizationService;
        this.subjectService = subjectService;
        this.ignoreProperties = ignoreProperties;
    }

    @GetMapping("facturas/organizacion/activos")
    public ResponseEntity encontrarPorOrganizacionIdYDocumentType(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<InvoiceData> invoicesData = new ArrayList<>();

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        if (organizacion != null) {
            invoicesData = buildResultListInvoice(user.getId(), invoiceService.encontrarPorOrganizacionIdYDocumentType(organizacion.getId(), DocumentType.INVOICE));
            invoicesData.forEach(invd -> {
                Optional<BigDecimal> importeTotal = detailService.encontrarTotalPorInvoiceId(invd.getId());
                invd.setImporteTotal(importeTotal.isPresent() ? importeTotal.get() : BigDecimal.ZERO);

            });
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/organizacion/activos", user.getId());
        return ResponseEntity.ok(invoicesData);
    }

    private List<InvoiceData> buildResultListInvoice(Long subjectId, List<InternalInvoice> invoices) {
        List<InvoiceData> invoicesData = new ArrayList<>();
        if (!invoices.isEmpty()) {
            Map<Long, Subject> customersMap = new HashMap<>();
            subjectService.encontrarPorSubjectCustomerSubjectId(subjectId).forEach(c -> customersMap.put(c.getId(), c));
            invoices.forEach(inv -> {
                invoicesData.add(invoiceService.buildInvoiceData(inv, customersMap.get(inv.getOwner().getId())));
            });
        }
        return invoicesData;

    }

}
