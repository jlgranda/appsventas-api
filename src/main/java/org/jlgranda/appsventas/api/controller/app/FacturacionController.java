/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.api.controller.app;

import com.rolandopalermo.facturacion.ec.domain.Invoice;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Detail;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.app.Payment;
import org.jlgranda.appsventas.domain.app.Product;
import org.jlgranda.appsventas.domain.app.view.InvoiceCountView;
import org.jlgranda.appsventas.domain.app.view.InvoiceView;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.InvoiceCountData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.InvoiceDetailData;
import org.jlgranda.appsventas.dto.app.InvoiceGlobalData;
import org.jlgranda.appsventas.dto.app.PaymentData;
import org.jlgranda.appsventas.dto.app.ProductData;
import org.jlgranda.appsventas.dto.app.SubjectCustomerData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.exception.ResourceNotFoundException;
import org.jlgranda.appsventas.services.app.ComprobantesService;
import org.jlgranda.appsventas.services.app.DetailService;
import org.jlgranda.appsventas.services.app.InternalInvoiceService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.PaymentService;
import org.jlgranda.appsventas.services.app.ProductService;
import org.jlgranda.appsventas.services.app.SubjectCustomerService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

    private InternalInvoiceService internalInvoiceService;
    private ComprobantesService comprobantesService;
    private DetailService detailService;
    private ProductService productService;
    private PaymentService paymentService;
    private OrganizationService organizationService;
    private SubjectService subjectService;
    private SubjectCustomerService subjectCustomerService;
    private final String ignoreProperties;

    @Autowired
    public FacturacionController(
            InternalInvoiceService internalInvoiceService,
            ComprobantesService comprobantesService,
            DetailService detailService,
            ProductService productService,
            PaymentService paymentService,
            OrganizationService organizationService,
            SubjectService subjectService,
            SubjectCustomerService subjectCustomerService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.internalInvoiceService = internalInvoiceService;
        this.comprobantesService = comprobantesService;
        this.productService = productService;
        this.detailService = detailService;
        this.paymentService = paymentService;
        this.organizationService = organizationService;
        this.subjectService = subjectService;
        this.subjectCustomerService = subjectCustomerService;
        this.ignoreProperties = ignoreProperties;
    }

    @GetMapping
    public ResponseEntity encontrarPorOrganizacion(
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
            invoiceGlobal.setInvoicesEmitidasData(buildResultListFromInvoiceView(internalInvoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_APPLIED)));
            invoiceGlobal.setInvoicesEmitidasCountData(buildResultListFromInvoiceCountView(internalInvoiceService.countPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE)));

            //invoiceGlobal.setInvoicesRecibidasData(buildResultListFromInvoiceView(internalInvoiceService.listarPorOwnerYDocumentTypeInternalStatus(subjectOpt.get().getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_APPLIED)));
        }
        Api.imprimirGetLogAuditoria("facturacion", user.getId());
        return ResponseEntity.ok(invoiceGlobal);
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
            invoiceGlobal.setInvoicesData(buildResultListFromInvoiceView(internalInvoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_APPLIED)));
            invoiceGlobal.setInvoicesCountData(buildResultListFromInvoiceCountView(internalInvoiceService.countPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE)));
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/emitidas/activos", user.getId());
        return ResponseEntity.ok(invoiceGlobal);
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
            invoicesData = buildResultListFromInvoiceView(internalInvoiceService.listarPorOwnerYDocumentTypeInternalStatus(subjectOpt.get().getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_APPLIED));
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/recibidas/activos", user.getId());
        return ResponseEntity.ok(invoicesData);
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
            invoicesData = buildResultListFromInvoiceView(internalInvoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, internalStatus));
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
            invoicesData.addAll(buildResultListFromInvoiceView(internalInvoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_INVALID)));
            invoicesData.addAll(buildResultListFromInvoiceView(internalInvoiceService.listarPorAuthorYOrganizacionIdYDocumentTypeInternalStatus(subjectOpt.get().getId(), organizacion.getId(), DocumentType.INVOICE, Constantes.SRI_STATUS_REJECTED)));
        }
        Api.imprimirGetLogAuditoria("facturacion/facturas/emitidas/rechazados", user.getId());
        return ResponseEntity.ok(invoicesData);
    }

    @PutMapping("/facturas/pago")
    public ResponseEntity editarFacturaPago(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody InvoiceData internalInvoiceData,
            BindingResult bindingResult
    ) {
        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        Payment payment = null;
        PaymentData paymentData = null;
//
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
//
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }
//
        if (subjectOpt.isPresent() && internalInvoiceData.getId() != null && internalInvoiceData.getImporteTotal() != null) {
            List<Payment> payments = paymentService.encontrarPorInvoiceId(internalInvoiceData.getId());
            if (!payments.isEmpty()) {
                for (Payment p : payments) {
                    p.setDeleted(Boolean.TRUE);
                    p.setDeletedOn(Dates.now());
                }
                paymentService.guardarLista(payments);
            }

            //Registrar el pago
            payment = paymentService.crearInstancia(subjectOpt.get());
            payment.setInvoiceId(internalInvoiceData.getId());
            payment.setAmount(internalInvoiceData.getImporteTotal());
            payment.setCash(payment.getAmount());
            payment.setDatePaymentCancel(Dates.now());
            paymentService.guardar(payment);

            //Cargar datos de retorno al frontend
            paymentData = new PaymentData();
            BeanUtils.copyProperties(payment, paymentData);
        } else {
            throw new NotFoundException("Los datos del pago están incompletos.");
        }
        Api.imprimirUpdateLogAuditoria("/facturacion/facturas/pago", user.getId(), paymentData);
        return ResponseEntity.ok(Api.response("payment", paymentData));
    }

    @PutMapping("/facturas/anular")
    public ResponseEntity editarFacturaEstado(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody InvoiceData internalInvoiceData,
            BindingResult bindingResult
    ) {
        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        return internalInvoiceService.encontrarPorUuid(internalInvoiceData.getUuid()).map(internalInvoice -> {
            //Anular factura
            Optional<Invoice> sriInvoiceOpt = comprobantesService.encontrarPorClaveAcceso(internalInvoice.getClaveAcceso());
            if (sriInvoiceOpt.isPresent()) {
                Invoice sriInvoice = sriInvoiceOpt.get();
                sriInvoice.setInternalStatusId(6);//Cancelada
                comprobantesService.guardar(sriInvoice);
                //Cambiar estado ACTIVE de internalInvoice
                internalInvoice.setActive(Boolean.FALSE);
                internalInvoiceService.guardar(internalInvoice);
            }
            Api.imprimirUpdateLogAuditoria("/facturacion/facturas/anular", user.getId(), internalInvoiceData);
            return ResponseEntity.ok(Api.response("factura", internalInvoiceData));
        }).orElseThrow(ResourceNotFoundException::new);

    }

    private List<InvoiceData> buildResultListInvoice(Long subjectId, List<InternalInvoice> invoices) {
        List<InvoiceData> invoicesData = new ArrayList<>();
        if (!invoices.isEmpty()) {
            Map<Long, Subject> customersMap = new HashMap<>();
            subjectService.encontrarPorSubjectCustomerSubjectId(subjectId).forEach(c -> customersMap.put(c.getId(), c));
            invoices.forEach(inv -> {
                invoicesData.add(internalInvoiceService.buildInvoiceData(inv, customersMap.get(inv.getOwner().getId())));
            });
        }
        return invoicesData;

    }

    private List<InvoiceData> buildResultListInvoice(List<InternalInvoice> invoices) {
        List<InvoiceData> invoicesData = new ArrayList<>();
        if (!invoices.isEmpty()) {
            invoices.forEach(inv -> {
                invoicesData.add(internalInvoiceService.buildInvoiceData(inv));
            });
        }
        return invoicesData;

    }

    private List<InvoiceData> buildResultListFromInvoiceView(List<InvoiceView> invoiceViews) {
        List<InvoiceData> invoicesData = new ArrayList<>();
        if (!invoiceViews.isEmpty()) {
            invoiceViews.forEach(inv -> {
                invoicesData.add(internalInvoiceService.buildInvoiceData(inv));
            });
        }
        return invoicesData;

    }

    @PutMapping("/factura/data/reemitir")
    public ResponseEntity recargarDataFactura(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody InvoiceData internalInvoiceData,
            BindingResult bindingResult
    ) {
        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        return subjectCustomerService.encontrarPorSubjectIdCustomerId(user.getId(), internalInvoiceData.getCustomerId()).map(subjectCustomer -> {

            //Cargar customer
            Optional<Subject> customerOpt = subjectService.encontrarPorId(subjectCustomer.getCustomerId());
            if (customerOpt.isPresent()) {
                internalInvoiceData.setSubjectCustomer(subjectCustomerService.buildSubjectCustomerData(subjectCustomer, customerOpt.get()));
            }

            //Cargar details
            List<InvoiceDetailData> invoiceDetailsData = new ArrayList<>();
            Map<Long, ProductData> productsMap = new HashMap<>();
            productService.encontrarPorOrganizacionId(organizacion.getId()).forEach(p -> productsMap.put(p.getId(), productService.buildProductData(p)));
            detailService.encontrarPorInvoiceId(internalInvoiceData.getId()).forEach(dt -> {
                invoiceDetailsData.add(detailService.buildInvoiceDetailData(dt, productsMap.get(dt.getProductId())));
            });
            internalInvoiceData.setDetails(invoiceDetailsData);

            Api.imprimirUpdateLogAuditoria("/facturacion/factura", user.getId(), internalInvoiceData);
            return ResponseEntity.ok(Api.response("factura", internalInvoiceData));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @PutMapping("/factura/data/nueva")
    public ResponseEntity nuevaDataFacturaAPartirDe(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody InvoiceData internalInvoiceData,
            BindingResult bindingResult
    ) {
        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        return subjectCustomerService.encontrarPorSubjectIdCustomerId(user.getId(), internalInvoiceData.getCustomerId()).map(subjectCustomer -> {

            //Nueva factura
            InvoiceData newInternalInvoiceData = new InvoiceData();

            //Cargar customer
            Optional<Subject> customerOpt = subjectService.encontrarPorId(subjectCustomer.getCustomerId());
            if (customerOpt.isPresent()) {
                newInternalInvoiceData.setSubjectCustomer(subjectCustomerService.buildSubjectCustomerData(subjectCustomer, customerOpt.get()));
            }

            //Cargar details
            List<InvoiceDetailData> invoiceDetailsData = new ArrayList<>();
            Map<Long, ProductData> productsMap = new HashMap<>();
            productService.encontrarPorOrganizacionId(organizacion.getId()).forEach(p -> productsMap.put(p.getId(), productService.buildProductData(p)));
            detailService.encontrarPorInvoiceId(internalInvoiceData.getId()).forEach(dt -> {
                invoiceDetailsData.add(detailService.buildInvoiceDetailData(dt, productsMap.get(dt.getProductId())));
            });
            newInternalInvoiceData.setDetails(invoiceDetailsData);
            newInternalInvoiceData.setDescription(internalInvoiceData.getDescription());
            
            newInternalInvoiceData.setEstab(internalInvoiceData.getEstab());
            newInternalInvoiceData.setPtoEmi(internalInvoiceData.getPtoEmi());
            
            Api.imprimirUpdateLogAuditoria("/facturacion/factura", user.getId(), newInternalInvoiceData);
            return ResponseEntity.ok(Api.response("factura", newInternalInvoiceData));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Contruye la lista de entidades para respuesta en el frontend
     *
     * @param invoiceCountViews
     * @return
     */
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
                        : Constantes.SRI_STATUS_CANCELLED.equals(invoiceCount.getInternalStatus()) ? "medium" : "ligth";
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
