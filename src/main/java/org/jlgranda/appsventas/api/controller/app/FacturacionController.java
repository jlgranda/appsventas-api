/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.api.controller.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Detail;
import org.jlgranda.appsventas.domain.app.Invoice;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.app.Payment;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.DetailData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.PaymentData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.services.app.DetailService;
import org.jlgranda.appsventas.services.app.InvoiceService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.PaymentService;
import org.jlgranda.appsventas.services.app.ProductService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    private InvoiceService invoiceService;
    private DetailService detailService;
    private PaymentService paymentService;
    private OrganizationService organizationService;
    private SubjectService subjectService;
    private final String ignoreProperties;

    @Autowired
    public FacturacionController(
            InvoiceService invoiceService,
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
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (subjectOpt.isPresent()) {
            List<Organization> organization = organizationService.encontrarPorOwner(subjectOpt.get());
            if (!organization.isEmpty()) {
                invoicesData = buildResultListInvoice(invoiceService.encontrarPorOrganizacionIdYDocumentType(organization.get(0).getId(), DocumentType.INVOICE));
                invoicesData.forEach(invd -> {
                    invd.setDetails(buildResultListDetail(detailService.encontrarPorInvoiceId(invd.getId())));
                    invd.setPayments(buildResultListPayment(paymentService.encontrarPorInvoiceId(invd.getId())));
                });
            }
        }
        System.out.println("invoicesData::::" + invoicesData);
        Api.imprimirGetLogAuditoria("facturacion/facturas/organizacion/activos", user.getId());
        return ResponseEntity.ok(invoicesData);
    }

    private List<InvoiceData> buildResultListInvoice(List<Invoice> invoices) {
        System.out.println("invoices:::" + invoices);
        List<InvoiceData> invoicesData = new ArrayList<>();
        if (!invoices.isEmpty()) {
            invoices.forEach(inv -> {
                invoicesData.add(invoiceService.buildInvoiceData(inv));
            });
        }
        return invoicesData;

    }

    private List<DetailData> buildResultListDetail(List<Detail> details) {
        List<DetailData> detailsData = new ArrayList<>();
        if (!details.isEmpty()) {
            details.forEach(d -> {
                detailsData.add(detailService.buildDetailData(d));
            });
        }
        return detailsData;

    }

    private List<PaymentData> buildResultListPayment(List<Payment> payments) {
        List<PaymentData> paymentsData = new ArrayList<>();
        if (!payments.isEmpty()) {
            payments.forEach(d -> {
                paymentsData.add(paymentService.buildPaymentData(d));
            });
        }
        return paymentsData;

    }

    @PostMapping()
    public ResponseEntity guardarFactura(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody InvoiceData newInvoice,
            BindingResult bindingResult
    ) {
        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        return ResponseEntity.ok(Api.response("factura", newInvoice));
    }

}
