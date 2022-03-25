/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.api.controller.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.app.view.InvoiceCountView;
import org.jlgranda.appsventas.domain.app.view.InvoiceView;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.InvoiceCountData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.InvoiceGlobalData;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.services.app.DetailService;
import org.jlgranda.appsventas.services.app.InternalInvoiceService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.PaymentService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/facturas/emitidas/activos")
    public ResponseEntity encontrarPorOrganizacionIdYDocumentType(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        InvoiceGlobalData invoiceGlobal = new InvoiceGlobalData();

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        if (subjectOpt.isPresent() && organizacion != null) {
            invoiceGlobal.setInvoicesData(buildResultListFromInvoiceView(invoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_APPLIED)));
            invoiceGlobal.setInvoicesCountData(buildResultListFromInvoiceCountView(invoiceService.countPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE)));
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/emitidas/activos", user.getId());
        return ResponseEntity.ok(invoiceGlobal);
    }

    @GetMapping("/facturas/emitidas/estado/{estado}/activos")
    public ResponseEntity encontrarPorOrganizacionIdYDocumentTypeInternalStatus(
            @PathVariable("estado") String internalStatus,
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<InvoiceData> invoicesData = new ArrayList<>();

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        if (subjectOpt.isPresent() && organizacion != null) {
            invoicesData = buildResultListFromInvoiceView(invoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, internalStatus));
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/emitidas/estado/{estado}/activos", user.getId());
        return ResponseEntity.ok(invoicesData);
    }

    @GetMapping("/facturas/emitidas/rechazados")
    public ResponseEntity encontrarPorOrganizacionIdYDocumentTypeInternalStatus(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<InvoiceData> invoicesData = new ArrayList<>();

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }
        if (subjectOpt.isPresent() && organizacion != null) {
//            invoicesData = buildResultListInvoice(user.getId(), invoiceService.encontrarPorAuthorYOrganizacionIdYDocumentType(subjectOpt.get(), organizacion.getId(), DocumentType.INVOICE));
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>< //Fin SQL");
//            invoicesData.forEach(invd -> {
//                Optional<BigDecimal> importeTotal = detailService.encontrarTotalPorInvoiceId(invd.getId());
//                invd.setImporteTotal(importeTotal.isPresent() ? importeTotal.get() : BigDecimal.ZERO);
//
//            });

            invoicesData.addAll(buildResultListFromInvoiceView(invoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_INVALID)));
            invoicesData.addAll(buildResultListFromInvoiceView(invoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_REJECTED)));
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/emitidas/rechazados", user.getId());
        return ResponseEntity.ok(invoicesData);
    }

    @GetMapping("/facturas/recibidas/activos")
    public ResponseEntity encontrarParaOwnerYDocumentType(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<InvoiceData> invoicesData = new ArrayList<>();

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        if (subjectOpt.isPresent()) {
//            invoicesData = buildResultListInvoice(invoiceService.encontrarPorOwnerYDocumentType(subjectOpt.get(), DocumentType.INVOICE));
//            invoicesData.forEach(invd -> {
//                Optional<BigDecimal> importeTotal = detailService.encontrarTotalPorInvoiceId(invd.getId());
//                invd.setImporteTotal(importeTotal.isPresent() ? importeTotal.get() : BigDecimal.ZERO);
//
//            });
            invoicesData = buildResultListFromInvoiceView(invoiceService.listarPorOwnerYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_APPLIED));
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/recibidas/activos", user.getId());
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

    private List<InvoiceData> buildResultListInvoice(List<InternalInvoice> invoices) {
        List<InvoiceData> invoicesData = new ArrayList<>();
        if (!invoices.isEmpty()) {
            invoices.forEach(inv -> {
                invoicesData.add(invoiceService.buildInvoiceData(inv));
            });
        }
        return invoicesData;

    }

    private List<InvoiceData> buildResultListFromInvoiceView(List<InvoiceView> invoiceViews) {
        List<InvoiceData> invoicesData = new ArrayList<>();
        if (!invoiceViews.isEmpty()) {
            invoiceViews.forEach(inv -> {
                invoicesData.add(invoiceService.buildInvoiceData(inv));
            });
        }
        return invoicesData;

    }

    private List<InvoiceCountData> buildResultListFromInvoiceCountView(List<InvoiceCountView> invoiceCountViews) {
        List<InvoiceCountData> invoicesCountData = new ArrayList<>();
        if (!invoiceCountViews.isEmpty()) {
            invoiceCountViews.forEach(inv -> {
                InvoiceCountData invoiceCount = new InvoiceCountData();
                BeanUtils.copyProperties(inv, invoiceCount);
                String colorStatus = Constantes.SRI_STATUS_CREATED.equals(invoiceCount.getInternalStatus()) ? "secondary"
                        : Constantes.SRI_STATUS_POSTED.equals(invoiceCount.getInternalStatus()) ? "warning"
                        : Constantes.SRI_STATUS_REJECTED.equals(invoiceCount.getInternalStatus()) ? "tertiary"
                        : Constantes.SRI_STATUS_INVALID.equals(invoiceCount.getInternalStatus()) ? "danger"
                        : Constantes.SRI_STATUS_CANCELLED.equals(invoiceCount.getInternalStatus()) ? "ligth" : "medium";
                invoiceCount.setColor(colorStatus);
                String nameStatus = Constantes.SRI_STATUS_CREATED.equals(invoiceCount.getInternalStatus()) ? "Por enviar al SRI"
                        : Constantes.SRI_STATUS_POSTED.equals(invoiceCount.getInternalStatus()) ? "Enviadas al SRI sin autorizar"
                        : Constantes.SRI_STATUS_REJECTED.equals(invoiceCount.getInternalStatus()) ? "Rechazadas"
                        : Constantes.SRI_STATUS_INVALID.equals(invoiceCount.getInternalStatus()) ? "Inválidas"  
                        : Constantes.SRI_STATUS_CANCELLED.equals(invoiceCount.getInternalStatus()) ? "Anuladas" : "Otras";
                invoiceCount.setStatus(nameStatus);
                invoicesCountData.add(invoiceCount);
            });
        }
        return invoicesCountData;

    }

}
