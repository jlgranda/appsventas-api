/*
 * Copyright (C) 2022 jlgranda
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jlgranda.appsventas.api.controller.app;

import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import net.tecnopro.util.Dates;
import net.tecnopro.util.Lists;
import net.tecnopro.util.Strings;
import net.tecnopro.util.VelocityHelper;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Detail;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.jlgranda.appsventas.domain.app.view.InvoiceView;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.jlgranda.appsventas.dto.ResultData;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.VeronicaAPIData;
import org.jlgranda.appsventas.dto.app.CertificadoDigitalData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.TokenData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.services.DataService;
import org.jlgranda.appsventas.services.app.ComprobantesService;
import org.jlgranda.appsventas.services.app.DetailService;
import org.jlgranda.appsventas.services.app.InternalInvoiceService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.SubjectCustomerService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.jlgranda.appsventas.services.mailing.MessageService;
import org.jlgranda.appsventas.services.mailing.NotificationService;
import org.jlgranda.appsventas.services.secuencias.SerialService;
import org.jlgranda.util.AESUtil;
import org.jlgranda.util.EmailUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jlgranda
 */
@RestController
@RequestMapping(path = "/comprobantes")
public class SRIComprobantesController {

    private String veronicaAPI;
    private final String ignoreProperties;

    @Autowired
    private ComprobantesService comprobantesService;
    private SubjectService subjectService;
    private OrganizationService organizationService;
    private SerialService serialService;
    private InternalInvoiceService internalInvoiceService;
    private DetailService detailService;
    private SubjectCustomerService subjectCustomerService;
    private DataService dataService;

    private NotificationService notificationService;
    private MessageService messageService;

    @Autowired
    public SRIComprobantesController(
            @Value("${appsventas.veronica.api.url}") String veronicaAPI,
            SubjectService subjectService,
            OrganizationService organizationService,
            SerialService serialService,
            InternalInvoiceService internalInvoiceService,
            DetailService detailService,
            SubjectCustomerService subjectCustomerService,
            DataService dataService,
            NotificationService notificationService,
            MessageService messageService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.veronicaAPI = veronicaAPI;
        this.subjectService = subjectService;
        this.organizationService = organizationService;
        this.serialService = serialService;
        this.internalInvoiceService = internalInvoiceService;
        this.detailService = detailService;
        this.subjectCustomerService = subjectCustomerService;
        this.dataService = dataService;
        this.notificationService = notificationService;
        this.messageService = messageService;
        this.ignoreProperties = ignoreProperties;
    }

    /**
     * Obtiene el token para el usuario administrador para temas de operaciones
     *
     * @return
     */
    private String getVeronicaAdminToken() {
        UserData user = new UserData();
        user.setUsername(Constantes.ADMIN_USERNAME);
        return this.getVeronicaToken(user);
    }

    /**
     * Obtiene el token para el usuario administrador para temas de operaciones
     *
     * @return
     */
    private String getPublicUserToken() {
        UserData user = new UserData();
        user.setUsername(Constantes.PUBLIC_USERNAME);
        return this.getVeronicaToken(user);
    }

    /**
     * Obtiene el token para el usuario administrador para temas de operaciones
     *
     * @return
     */
    private String getUserToken(String username) {
        UserData user = new UserData();
        user.setUsername(username);
        return this.getVeronicaToken(user);
    }

    private String getVeronicaToken(UserData user) {

        final String uri = this.veronicaAPI + Constantes.URI_API_AUTH;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + "dmVyb25pY2E6dmVyb25pY2E=");

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", user.getUsername());
        requestBody.add("password", "veronica");
        requestBody.add("grant_type", "password");

        HttpEntity request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TokenData> response = null;

        TokenData token = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.POST, request, TokenData.class);
            if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {

                token = response.getBody();
            }
            /*else {

            }*/
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
            } else {
            }
            httpClientOrServerExc.printStackTrace();
        }

        return token != null ? token.getAccess_token() : Constantes.VERONICA_NO_TOKEN;
    }

    @GetMapping("/{tipo}")
    public ResponseEntity getComprobantesPorTipo(@AuthenticationPrincipal UserData user,
            @PathVariable("tipo") String tipo) {

        return ResponseEntity.ok(comprobantesService.findAllBySupplierId(tipo));
    }

    @GetMapping("/{tipo}/{claveAcceso}")
    public ResponseEntity getComprobante(@AuthenticationPrincipal UserData user,
            @PathVariable("tipo") String tipo, @PathVariable("claveAcceso") String claveAcceso) {

        final String path = this.veronicaAPI + Constantes.URI_API_V1;
        final String uri = !path.endsWith("/") ? (path + "/" + tipo) : (path + tipo);

        String token = this.getVeronicaToken(user);
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            return Api.responseError("No se pud?? obtener un token del API Ver??nica", null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
            if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {
                //Convertir la respuesta 
                return response;
            } else {

//                System.out.println(">>>>>>>>>>>>>>>>>>");
//                System.out.println("" + response.getStatusCode());
//                System.out.println(">>>>>>>>>>>>>>>>>>");
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
            } else {
            }

            httpClientOrServerExc.printStackTrace();
        }
        return Api.responseError("No se pud?? recuperar comprobantes para el tipo indicado.", tipo);
    }

    /**
     * Envia la petici??n de facturaci??n. Los datos se registran en appsventas,
     * luego de la respuesta de veronica y el SRI
     *
     * @param user
     * @param invoiceData
     * @param bindingResult
     * @return
     */
    @PostMapping(path = "/" + Constantes.INVOICE)
    public ResponseEntity crearEnviarFactura(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody InvoiceData invoiceData,
            BindingResult bindingResult
    ) {

        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        //Invocar servicio veronica API
        String token = this.getVeronicaToken(user);

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());

        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? una entidad Subject v??lida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontr?? una organizaci??n v??lida para el usuario autenticado.");
        }

        String estab = Strings.isNullOrEmpty(invoiceData.getEstab()) ? Constantes.SRI_ESTAB_DEFAULT : Strings.toUpperCase(invoiceData.getEstab());

        String ptoEmi = Constantes.SRI_PTO_EMISION_FACTURAS_ELECTRONICAS;

        String secuencial = "";

        if (Strings.isNullOrEmpty(invoiceData.getSecuencial())) {
            secuencial = serialService.getSecuencialGenerator(organizacion.getRuc(), Constantes.INVOICE, estab, ptoEmi, organizacion.getAmbienteSRI()).next();
        } else {
            secuencial = invoiceData.getSecuencial();
        }

//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
//        System.out.println("ruc: " + organizacion.getRuc());
//        System.out.println("estab: " + estab);
//        System.out.println("ptoEmi: " + ptoEmi);
//        System.out.println("secuencial: " + secuencial);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
        VeronicaAPIData data = crearComprobante(token, Constantes.URI_API_V1_INVOICE, secuencial, estab, ptoEmi, invoiceData, user);

        //Enviar a InternalInvoice (entidad invoice en appsventas), agregar un indicador de si ya se gener?? en el SRI
        InternalInvoice invoice = null;

        Optional<Subject> customerOpt = subjectService.encontrarPorId(invoiceData.getSubjectCustomer().getCustomerId());
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? un cliente v??lido para el usuario autenticado.");
        }

        if (subjectOpt.isPresent() && customerOpt.isPresent() && organizacion != null) {

            //Guardar el invoice en appsventas
            invoice = internalInvoiceService.crearInstancia(subjectOpt.get());
            BeanUtils.copyProperties(invoiceData, invoice, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            invoice.setOrganizacionId(organizacion.getId());
            invoice.setDocumentType(DocumentType.INVOICE);
            invoice.setSequencial(secuencial);
            invoice.setBoardNumber(Constantes.INVOICE_BOARD);
            invoice.setPax(Long.valueOf(Constantes.INVOICE_PAX));
            invoice.setPrintAlias(Boolean.FALSE);
            if (data.getResult() != null) {
                invoice.setClaveAcceso(data.getResult().getClaveAcceso());
            } else {
                invoice.setClaveAcceso(Constantes.PROFORMA + Constantes.SEPARADOR + UUID.randomUUID().toString());
            }
            invoice.setOwner(customerOpt.get());
            internalInvoiceService.guardar(invoice);

            final Long invoiceId = invoice.getId();
            List<Detail> details = new ArrayList<>();
            invoiceData.getDetails().forEach(dtl -> {
                Detail detail = detailService.crearInstancia(subjectOpt.get());
                detail.setInvoiceId(invoiceId);
                detail.setProductId(dtl.getProductId());
                detail.setAmount(dtl.getAmount()); //La cantidad que viene desde el front
                detail.setPrice(dtl.getPrice());
                detail.setIva12(dtl.isIVA12());
                details.add(detail);
            });

            detailService.guardar(details);

            //Guardar el subjectCustomer en caso a??n no sea parte del usuario
            if (invoiceData.getSubjectCustomer() != null && invoiceData.getSubjectCustomer().getId() == null) {
                SubjectCustomer subjectCustomer = null;
                subjectCustomer = subjectCustomerService.crearInstancia(subjectOpt.get());
                subjectCustomer.setSubjectId(user.getId());
                subjectCustomer.setCustomerId(customerOpt.get().getId());
                subjectCustomerService.guardar(subjectCustomer);
            }
        }

        if (invoiceData.getEnviarSRI()) {
            String accion = invoiceData.getAccionSRI();
            if (Constantes.ACCION_COMPROBANTE_ENVIAR.equalsIgnoreCase(accion)
                    || Constantes.ACCION_COMPROBANTE_AUTORIZAR.equalsIgnoreCase(accion)
                    || Constantes.ACCION_COMPROBANTE_EMITIR.equalsIgnoreCase(accion)) {

                String claveAcceso = data.getResult().getClaveAcceso();
                data = enviarComprobante(token, Constantes.URI_API_V1_INVOICE, claveAcceso, accion);

                //VeronicaAPIData data2 = enviarComprobante( token, Constantes.URI_API_V1_INVOICE, "0503202201110382696000110010010000000055093058218"); //verificado
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
//                System.out.println("Notificar via correo: APLLIED = " + data.getResult().getEstado());
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
                if (Constantes.SRI_STATUS_APPLIED.equalsIgnoreCase(data.getResult().getEstado())) {
                    //Notificar via correo
                    String titulo = "[Notificaci??n] FACTURA - SERVICIO $organizacionNombreCompleto";//catalogoService.obtenerValor("NOTIFICACION_CREACION_USUARIO_TITULO", "Notificaci??n de creaci??n de usuarios SMC");
                    String cuerpoMensaje = "<p>Estimada(o): <strong>$clienteNombreCompleto</strong></p>\n"
                            + "<p><br />Le informamos que su Factura n&uacute;mero <strong>$facturaSecuencia</strong> ha sido remitida por <strong>$organizacionNombreCompleto</strong> de forma Electr&oacute;nica.</p>\n"
                            + "<p>&nbsp;</p>\n"
                            + "<p>Si Usted es cliente del $organizacionNombreCompleto mantenga siempre a la mano las facturas que emite y recibe a trav&eacute;s de nuestra aplicaci&oacute;n <a title=\"FAZil facturaci&oacute;n electr&oacute;nica en 2 clics\" href=\"$url\" target=\"_blank\" rel=\"noopener noreferrer\">FAZil</a><br /><br />Atentamente,<br /><strong>$organizacionNombreCompleto.</strong></p>";//catalogoService.obtenerValor("NOTIFICACION_CREACION_USUARIO_MENSAJE", "Bienvenido $cedula - $nombres a SMC\nSus datos de acceso son:\nNombre de usuario: $nombreUsuario\nContrase??a: $contrasenia\n");
                    UserData destinatario = new UserData();
                    BeanUtils.copyProperties(customerOpt.get(), destinatario);
                    destinatario.setNombre(customerOpt.get().getFullName());
                    destinatario.setId(customerOpt.get().getId());

                    Map<String, Object> values = new HashMap<>();
                    values.put("facturaSecuencia", secuencial);
                    values.put("clienteNombreCompleto", Strings.toUpperCase(destinatario.getNombre()));
                    values.put("organizacionNombreCompleto", Strings.toUpperCase(organizacion.getName()));
                    values.put("url", "http://jlgranda.com/entry/fazil-facturacion-electronica-para-profesionales");
                    byte[] pdf = getPDF(Constantes.INVOICE, claveAcceso, user.getUsername());
                    byte[] xml = getXML(Constantes.INVOICE, claveAcceso, user.getUsername());

                    //La notificaciones siempre deben enviarse desde un mismo correo
                    user.setEmail("AppsVentas Plataforma <notificacion@jlgranda.com>"); //TODO obtener desde propiedades del sistema
                    EmailUtil.getInstance().enviarCorreo(user, destinatario, titulo, cuerpoMensaje, values, this.notificationService, this.messageService, claveAcceso, pdf, xml);
                }
            }
        }

        return ResponseEntity.ok(data);
    }

    @PutMapping(path = "/" + Constantes.INVOICE + "/{claveAcceso}/notificar")
    public ResponseEntity notificarFactura(
            @AuthenticationPrincipal UserData user,
            @PathVariable("claveAcceso") String claveAcceso,
            @Valid @RequestBody InvoiceData submitInvoiceData,
            BindingResult bindingResult
    ) {

        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        if (!claveAcceso.equalsIgnoreCase(submitInvoiceData.getClaveAcceso())) {
            throw new NotFoundException("La clave de acceso no coincide con la solucitud al servidor.");
        }
        //Invocar servicio veronica API
        String token = this.getVeronicaToken(user);

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());

        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? una entidad Subject v??lida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontr?? una organizaci??n v??lida para el usuario autenticado.");
        }

        InvoiceData invoiceData = internalInvoiceService.buildInvoiceData(internalInvoiceService.encontrarPorClaveAcceso(claveAcceso).get());

        Optional<Subject> customerOpt = subjectService.encontrarPorId(invoiceData.getCustomerId());
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? un cliente v??lido para la factura. customerId: " + invoiceData.getCustomerId());
        }

        if (Constantes.SRI_STATUS_APPLIED.equalsIgnoreCase(invoiceData.getInternalStatus())) {
            //Notificar via correo
            String titulo = "[Notificaci??n] FACTURA - $organizacionNombreCompleto";//catalogoService.obtenerValor("NOTIFICACION_CREACION_USUARIO_TITULO", "Notificaci??n de creaci??n de usuarios SMC");
            String cuerpoMensaje = "<p>Estimada(o): <strong>$clienteNombreCompleto</strong></p>\n"
                    + "<p><br />Le informamos que su Factura n&uacute;mero <strong>$facturaSecuencia</strong> ha sido remitida por <strong>$organizacionNombreCompleto</strong> de forma Electr&oacute;nica.</p>\n"
                    + "<p>&nbsp;</p>\n"
                    + "<p>Si Usted es cliente del $organizacionNombreCompleto mantenga siempre a la mano las facturas que emite y recibe a trav&eacute;s de nuestra aplicaci&oacute;n <a title=\"FAZil facturar EC en 2 clics\" href=\"$url\" target=\"_blank\" rel=\"noopener noreferrer\">FAZil</a><br /><br />Atentamente,<br /><strong>$organizacionNombreCompleto.</strong></p>";//catalogoService.obtenerValor("NOTIFICACION_CREACION_USUARIO_MENSAJE", "Bienvenido $cedula - $nombres a SMC\nSus datos de acceso son:\nNombre de usuario: $nombreUsuario\nContrase??a: $contrasenia\n");
            UserData destinatario = new UserData();
            BeanUtils.copyProperties(customerOpt.get(), destinatario);
            destinatario.setNombre(customerOpt.get().getFullName());
            destinatario.setId(customerOpt.get().getId());

            Map<String, Object> values = new HashMap<>();
            values.put("facturaSecuencia", invoiceData.getSecuencial());
            values.put("clienteNombreCompleto", Strings.toUpperCase(destinatario.getNombre()));
            values.put("organizacionNombreCompleto", Strings.toUpperCase(organizacion.getName()));
            values.put("url", "http://jlgranda.com/entry/fazil-facturacion-electronica-sri-para-profesionales-en-ecuador");
            byte[] pdf = getPDF(Constantes.INVOICE, claveAcceso, user.getUsername());
            byte[] xml = getXML(Constantes.INVOICE, claveAcceso, user.getUsername());

            //La notificaciones siempre deben enviarse desde un mismo correo
            user.setEmail("AppsVentas Plataforma <notificacion@jlgranda.com>"); //TODO obtener desde propiedades del sistema
            EmailUtil.getInstance().enviarCorreo(user, destinatario, titulo, cuerpoMensaje, values, this.notificationService, this.messageService, claveAcceso, pdf, xml);
        }

        return ResponseEntity.ok(claveAcceso);
    }

    /**
     *
     * @param tipo el tipo de comprobante
     * @return
     */
    private VeronicaAPIData crearComprobante(String token, String tipo, String secuencial, String estab, String ptoEmi, InvoiceData invoiceData, UserData user) {

        VeronicaAPIData data = new VeronicaAPIData();

        final String path = this.veronicaAPI + tipo;
        final String uri = path;
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            String message = "No se consigui?? el token desde Veronica API";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setType("error");

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
        }

        //Datos de la Organizaci??n (infoTributaria)
        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        Optional<Subject> customerOpt = subjectService.encontrarPorId(invoiceData.getSubjectCustomer().getCustomerId());

        if (organizacion == null) {
            String message = "No se encontr?? una organizaci??n v??lida para el usuario autenticado.";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);

            return data;
            ///throw new NotFoundException("No se encontr?? una organizaci??n v??lida para el usuario autenticado.");
        }
        if (!subjectOpt.isPresent()) {
            String message = "No se encontr?? una sujeto emisor v??lido para el usuario autenticado.";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);

            return data;
            //throw new NotFoundException("No se encontr?? una sujeto emisor v??lido para el usuario autenticado.");
        }
        if (!customerOpt.isPresent()) {
            String message = "No se encontr?? una sujeto destinatario v??lido para el usuario autenticado.";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
            //throw new NotFoundException("No se encontr?? una sujeto destinatario v??lido para el usuario autenticado.");
        }

        //Cargar generadores de serie para facturas
        Subject subject = subjectOpt.get();
        Subject customer = customerOpt.get();
        final String ambienteSRI = "PRODUCCION".equalsIgnoreCase(organizacion.getAmbienteSRI()) ? "2" : "1";
        Map<String, Object> values = new HashMap<>();

        values.put("id", "comprobante");
        values.put("version", "1.0.0");
        values.put("ambiente", ambienteSRI); //1 PRUEBAS, 2 PRODUCCION
        values.put("claveAcceso", ""); //1 PRUEBAS, 2 PRODUCCION
        values.put("tipoEmision", "" + "1");
        values.put("razonSocial", "" + organizacion.getName());
        values.put("nombreComercial", "" + organizacion.getInitials());
        values.put("ruc", "" + organizacion.getRuc());
        values.put("codDoc", "" + "04"); //NOTA DE CR??DITO
        values.put("estab", estab); //Por defecto un establecimiento
        values.put("ptoEmi", ptoEmi); ////Por defecto un punto de emision
        values.put("secuencial", Strings.extractLast(secuencial, "-"));

        values.put("dirMatriz", "" + subject.getDescription());
        values.put("dirEstablecimiento", Strings.isNullOrEmpty(organizacion.getDireccion()) ? Constantes.SIN_DIRECCION : organizacion.getDireccion());

        if (Objects.equals(Boolean.TRUE, organizacion.getContribuyenteEspecial())) {
            values.put("contribuyenteEspecial", Strings.isNullOrEmpty(organizacion.getContribuyenteEspecialNumeroResolucion()) ? "" : organizacion.getContribuyenteEspecialNumeroResolucion());
        }

        if (Objects.equals(Boolean.TRUE, organizacion.getObligadoLlevarContabilidad())) { //Obligado a llevar contabilidad
            values.put("obligadoContabilidad", "" + "SI");
        } else {
            values.put("obligadoContabilidad", "" + "NO");
        }
        
        if (Objects.equals(Boolean.TRUE, organizacion.getRegimenRimpe())) { //Obligado a llevar contabilidad
            values.put("contribuyenteRimpe", organizacion.getRegimenRimpeTipo()); //TODO verificar que saca
        } else {
            values.put("contribuyenteRimpe", "");
        }
        
        if (Objects.equals(Boolean.TRUE, organizacion.getRegimenMicroEmpresas())) { //Obligado a llevar contabilidad
            values.put("regimenMicroempresas", "" + "SI");
        } else {
            values.put("regimenMicroempresas", "" + "NO");
        }
        
        if (Objects.equals(Boolean.TRUE, organizacion.getRise())) { //Obligado a llevar contabilidad
            values.put("rise", "" + "Contribuyente R??gimen Simplificado RISE");
        } 

        //Datos del invoiceData (infoFactura) //comprador
        values.put("fechaEmision", Dates.toString(invoiceData.getEmissionOn(), Constantes.FORMATO_FECHA_SRI));
        //[0-9]{3}-[0-9]{3}-[0-9]{9}
        //values.put("guiaRemision", "000-000-000000000"); //Seg??n Karina no es necesario para servicios

        //Ver Pag 13, Tabla 6 para  m??s tipos de Identificaci??n 04 -> RUc, 05 -> CEDULA
        values.put("tipoIdentificacionComprador", customer.getCode().length() == 13 ? "04" : "05");
        values.put("razonSocialComprador", "" + customer.getFullName());
        values.put("identificacionComprador", "" + customer.getCode());
        values.put("direccionComprador", Strings.isNullOrEmpty(customer.getDescription()) ? Constantes.SIN_DIRECCION : customer.getDescription());

        values.put("totalSinImpuestos", "" + invoiceData.getSubTotal());
        values.put("totalDescuento", "" + invoiceData.getDescuento());

        //IVA
        values.put("totalImpuestoCodigo", "" + "2");
        values.put("totalImpuestoCodigoPorcentaje", "" + "2");
        values.put("descuentoAdicional", "" + 0.00);
        values.put("totalImpuestoBaseImponible", invoiceData.getSubTotal() != null ? invoiceData.getSubTotal().setScale(2, RoundingMode.HALF_UP) : "0.00");
        values.put("totalImpuestoValor", invoiceData.getIva12Total() != null ? invoiceData.getIva12Total().setScale(2, RoundingMode.HALF_UP) : "0.00");

        values.put("propina", "" + invoiceData.getPropina());
        values.put("importeTotal", "" + invoiceData.getImporteTotal() != null ? invoiceData.getImporteTotal().setScale(2, RoundingMode.HALF_UP) : "0.00");
        values.put("moneda", "" + "DOLAR");

        //Falta el array de pagos
        values.put("formaPago", "" + "01");
        values.put("total", "" + "" + invoiceData.getImporteTotal() != null ? invoiceData.getImporteTotal().setScale(2, RoundingMode.HALF_UP) : "0.00");
        values.put("plazo", "" + "30");
        values.put("unidadTiempo", "dias");

        values.put("valorRetIva", "" + 0.00);
        values.put("valorRetRenta", "" + 0.00);

        //detalles en funci??n del detalle de la factura
        List<String> detalles = new ArrayList<>();
        if (invoiceData.getDetails().size() == 1) { //Sobreescribir precios
            invoiceData.getDetails().forEach(dtl -> {
                dtl.getProduct().setPrice(invoiceData.getSubTotal()); //Cambiar el valor si es un s??lo producto y los valores son diferentes
            });
        }
        //Procesar detalles para generar la factura.
        invoiceData.getDetails().forEach(dtl -> {
            Map<String, Object> detailValues = new HashMap<>();
            try {
                detailValues.put("codigoPrincipal", "" + dtl.getProductId());
                detailValues.put("codigoAuxiliar", "" + dtl.getProductId());
                detailValues.put("descripcion", "" + (Strings.isNullOrEmpty(dtl.getProductName()) ? invoiceData.getDescription() : dtl.getProductName().concat(Strings.isNullOrEmpty(invoiceData.getDescription()) ? "" : "[".concat(invoiceData.getDescription()).concat("]"))));
                detailValues.put("cantidad", "" + dtl.getAmount());
                detailValues.put("precioUnitario", "" + dtl.getPrice() != null ? dtl.getPrice().setScale(2, RoundingMode.HALF_UP) : "0.00");
                detailValues.put("descuento", "" + 0.00);
                detailValues.put("precioTotalSinImpuesto", dtl.getSubTotal() != null ? dtl.getSubTotal().setScale(2, RoundingMode.HALF_UP) : "0.00");

                detailValues.put("detAdicionalNombre1", "" + dtl.getProductName());
                detailValues.put("detAdicionalValor1", "" + dtl.getSubTotal() != null ? dtl.getSubTotal().setScale(2, RoundingMode.HALF_UP) : "0.00");

                detailValues.put("impuestoCodigo", "2");
                detailValues.put("impuestoCodigoPorcentaje", "2");
                detailValues.put("tarifa", "12");
                detailValues.put("impuestoBaseImponible", dtl.getSubTotal() != null ? dtl.getSubTotal().setScale(2, RoundingMode.HALF_UP) : "0.00");
                detailValues.put("impuestoValor", "" + dtl.getImpuestoValor() != null ? dtl.getImpuestoValor().setScale(2, RoundingMode.HALF_UP) : "0.00");

                detalles.add(VelocityHelper.getRendererMessage(Constantes.JSON_DETAIL_TEMPLATE, detailValues));
            } catch (Exception ex) {
                Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //Establecer detalles
        values.put("detalles", Lists.toString(detalles, ","));
        //Falta el array de campoAdicional
        values.put("campoAdicional", "" + "campoAdicional");

        //TODO elegir template en funci??n del tipo de documento
        String jsonPlantilla = "";
        switch (tipo) {
            case Constantes.INVOICE:
                if (Objects.equals(Boolean.TRUE, organizacion.getContribuyenteEspecial())) {
                    jsonPlantilla = Constantes.JSON_FACTURA_TEMPLATE_CONTRIBUYENTE_ESPECIAL;
                } else {
                    jsonPlantilla = Constantes.JSON_FACTURA_TEMPLATE;
                }
                break;
            case Constantes.CM:
                jsonPlantilla = Constantes.JSON_NOTA_CREDITO_TEMPLATE;
                break;
            default:
                throw new AssertionError();
        }
        
        //Renderizado final del objeto factura antes de enviar a SRI
        StringBuilder json = new StringBuilder("$");
        try {
            json = new StringBuilder(VelocityHelper.getRendererMessage(jsonPlantilla, values));
        } catch (Exception ex) {

            Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
            String message = ex.getLocalizedMessage();
            ResultData errorData = new ResultData(false, message, ex);
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VeronicaAPIData> response = null;
        try {

            response = restTemplate.exchange(uri, HttpMethod.POST, request, VeronicaAPIData.class);
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                data = response.getBody();
                data.getResult().setEstado(Constantes.ESTADO_COMPROBANTE_CREADA); //Marcar un estado en la respuesta
            } else if (HttpStatus.CREATED.equals(response.getStatusCode()) && response.getBody() != null) {
                data = response.getBody();
                data.getResult().setEstado(Constantes.ESTADO_COMPROBANTE_CREADA); //Marcar un estado en la respuesta
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            httpClientOrServerExc.printStackTrace();
            if (null != httpClientOrServerExc.getStatusCode()) {
                String message = "VERONICA API: " + httpClientOrServerExc.getMessage();
                ResultData errorData = new ResultData(false, message, httpClientOrServerExc.getCause());
                errorData.setSuccess(false);

                data.setSuccess(false);
                data.setErrors(errorData);
                return data;
            }

        }

        return data;
    }

    /**
     *
     * @param token el tipo de comprobante
     * @param tipo el tipo de comprobante
     * @param claveAcceso el tipo de comprobante
     * @param accion la acci??n sobre el comprobante: enviar, autorizar, emitir,
     * anular
     * @return
     */
    private VeronicaAPIData enviarComprobante(String token, String tipo, String claveAcceso, String accion) {
        VeronicaAPIData data = new VeronicaAPIData();

        final String uri = this.veronicaAPI + tipo + "/" + claveAcceso + "/" + accion;
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);

        if (Strings.isNullOrEmpty(claveAcceso)) {
            String message = "No se recibi?? una clave de acceso para enviar el comprobante";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setType("error");

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VeronicaAPIData> response = null;
        try {

            response = restTemplate.exchange(uri, HttpMethod.PUT, request, VeronicaAPIData.class);

            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                data = response.getBody();
                data.getResult().setClaveAcceso(claveAcceso); //Por si se necesita en el cliente
            } else {
                data = response.getBody();
                data.getResult().setClaveAcceso(claveAcceso); //Por si se necesita en el cliente
            }

            //Actualizar estado del documento para manejo en el frontend
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
//            System.out.println("data: " + data);
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
            Optional<InvoiceView> invoiceViewOpt = internalInvoiceService.encontrarPorClaveAcceso(data.getResult().getClaveAcceso());
            if (invoiceViewOpt.isPresent()) {
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
//                System.out.println("invoiceViewOpt.get().getInternalStatus(): " + invoiceViewOpt.get().getInternalStatus());
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
                data.getResult().setEstado(invoiceViewOpt.get().getInternalStatus());
            }

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            httpClientOrServerExc.printStackTrace();
            if (null != httpClientOrServerExc.getStatusCode()) {
                String message = "VERONICA API: " + httpClientOrServerExc.getMessage();
                ResultData errorData = new ResultData(false, message, httpClientOrServerExc.getCause());
                errorData.setSuccess(false);

                data.setSuccess(false);
                data.setErrors(errorData);
                return data;
            }

        }

        return data;
    }

    @PostMapping(path = "/firmaelectronica")
    public ResponseEntity registrarCertificadoDigital(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody CertificadoDigitalData certificadoDigitalData,
            BindingResult bindingResult) {

        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        String token = this.getVeronicaAdminToken();

        return ResponseEntity.ok(enviarCertificadoDigital(token, user, certificadoDigitalData));

    }

    /**
     *
     * @param token el tipo de comprobante
     * @return
     */
    private VeronicaAPIData enviarCertificadoDigital(String token, UserData user, CertificadoDigitalData certificadoDigitalData) {

        VeronicaAPIData data = new VeronicaAPIData();

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            String message = "No se consigui?? el token desde Veronica API";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setType("error");

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
        }

        //Datos de la Organizaci??n (infoTributaria)
        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        //Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());

        if (organizacion == null) {
            String message = "No se encontr?? una organizaci??n v??lida para el usuario autenticado.";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);

            return data;
            ///throw new NotFoundException("No se encontr?? una organizaci??n v??lida para el usuario autenticado.");
        }
//        if (!subjectOpt.isPresent()) {
//            String message = "No se encontr?? una sujeto emisor v??lido para el usuario autenticado.";
//            ResultData errorData = new ResultData(false, message, new Error());
//            errorData.setSuccess(false);
//
//            data.setSuccess(false);
//            data.setErrors(errorData);
//
//            return data;
//            //throw new NotFoundException("No se encontr?? una sujeto emisor v??lido para el usuario autenticado.");
//        }

        //Cargar generadores de serie para facturas
        //Subject subject = subjectOpt.get();
        final String uri = this.veronicaAPI + Constantes.URI_OPERATIONS + "certificados-digitales/empresas/" + organizacion.getRuc();

        //La contrase??a ya viene encriptada, desencriptar y comparar con Shiro password service
        String plainText = "";
        try {
            plainText = AESUtil.decrypt("dXNyX2FwcGF0cGE6cHJ1ZWI0c19BVFBBXzIwMjA", certificadoDigitalData.getPassword());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException | BadPaddingException ex) {
            Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }

        Map<String, Object> values = new HashMap<>();
        values.put("certificado", "" + certificadoDigitalData.getCertificado());
        values.put("password", "" + plainText); //Se encrypta en ver??nica

        StringBuilder json = new StringBuilder("$");

        try {
            json = new StringBuilder(VelocityHelper.getRendererMessage(Constantes.JSON_CERTIFICADO_DIGITAL, values));
        } catch (Exception ex) {

            Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
            String message = ex.getLocalizedMessage();
            ResultData errorData = new ResultData(false, message, ex);
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VeronicaAPIData> response = null;
        try {

            response = restTemplate.exchange(uri, HttpMethod.POST, request, VeronicaAPIData.class);
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                data = response.getBody();
                data.getResult().setEstado(Constantes.ESTADO_COMPROBANTE_CREADA); //Marcar un estado en la respuesta
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            httpClientOrServerExc.printStackTrace();
            if (null != httpClientOrServerExc.getStatusCode()) {
                String message = "VERONICA API: " + httpClientOrServerExc.getMessage();
                ResultData errorData = new ResultData(false, message, httpClientOrServerExc.getCause());
                errorData.setSuccess(false);

                data.setSuccess(false);
                data.setErrors(errorData);
                return data;
            }

        }

        return data;
    }

    @GetMapping(value = "/{tipo}/{claveAcceso}/archivos/test")
    public ResponseEntity testRIDE(@PathVariable("tipo") String tipo, @PathVariable("claveAcceso") String claveAcceso) {

        //this.getPDF(tipo, claveAcceso, "jlgranda81@gmail.com");
        this.getXML(tipo, claveAcceso, "jlgranda81@gmail.com");

        return ResponseEntity.ok().body(claveAcceso);
    }

    //https://www.javacodemonk.com/download-a-file-using-spring-resttemplate-75723d97
    @GetMapping(value = "/{tipo}/{claveAcceso}/archivos/pdf")
    public ResponseEntity<byte[]> generateRIDE(@PathVariable("tipo") String tipo, @PathVariable("claveAcceso") String claveAcceso) {

        final String path = this.veronicaAPI + Constantes.URI_API_V1;
        final String uri = !path.endsWith("/") ? (path + "/" + tipo) : (path + tipo) + "/" + claveAcceso + "/archivos/pdf";

        String token = this.getPublicUserToken(); //Token para descargar comprobantes
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            return Api.responseError("No se pud?? obtener un token del API Ver??nica", null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, request, byte[].class);
            if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {
                //Convertir la respuesta 
                return response;
            } else {
//
//                System.out.println(">>>>>>>>>>>>>>>>>>");
//                System.out.println("" + response.getStatusCode());
//                System.out.println(">>>>>>>>>>>>>>>>>>");
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
            } else {
            }

            httpClientOrServerExc.printStackTrace();
        }
        return Api.responseError("No se pud?? recuperar comprobantes para el tipo indicado.", tipo);
    }

    //https://www.javacodemonk.com/download-a-file-using-spring-resttemplate-75723d97
    private byte[] getPDF(String tipo, String claveAcceso, String username) {
        final String path = this.veronicaAPI + Constantes.URI_API_V1;
        final String uri = !path.endsWith("/") ? (path + "/" + tipo) : (path + tipo) + "/" + claveAcceso + "/archivos/pdf";

        String token = this.getUserToken(username); //Token para descargar comprobantes
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        //headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {
                //Convertir la respuesta 
                return response.getBody();
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            httpClientOrServerExc.printStackTrace();

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
            } else {
            }

        }

        return null;
    }

    /**
     * //https://www.javacodemonk.com/download-a-file-using-spring-resttemplate-75723d97
     *
     * @param tipo
     * @param claveAcceso
     * @param username
     * @return
     */
    private byte[] getXML(String tipo, String claveAcceso, String username) {
        final String path = this.veronicaAPI + Constantes.URI_API_V1;
        final String uri = !path.endsWith("/") ? (path + "/" + tipo) : (path + tipo) + "/" + claveAcceso + "/archivos/xml";

        String token = this.getUserToken(username); //Token para descargar comprobantes
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("Authorization", "Bearer " + token);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
            if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {
                //Convertir la respuesta 
                String xml = (String) response.getBody();
                return xml.getBytes(); //El arreglo de bytes para adjuntar en el email
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
            } else {
            }

            httpClientOrServerExc.printStackTrace();
        }

        return null;
    }
    
    
    /*************************************************************************
     *************************************************************************
     * Notas de cr??dito
     *************************************************************************
     ************************************************************************/
    /**
     * Envia la petici??n de facturaci??n. Los datos se registran en appsventas,
     * luego de la respuesta de veronica y el SRI
     *
     * @param user
     * @param invoiceData
     * @param bindingResult
     * @return
     */
    @PostMapping(path = "/" + Constantes.CM)
    public ResponseEntity crearEnviarNotaCredito(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody InvoiceData invoiceData,
            BindingResult bindingResult
    ) {

        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        //Invocar servicio veronica API
        String token = this.getVeronicaToken(user);

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());

        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? una entidad Subject v??lida para el usuario autenticado.");
        }

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontr?? una organizaci??n v??lida para el usuario autenticado.");
        }

        String estab = Strings.isNullOrEmpty(invoiceData.getEstab()) ? Constantes.SRI_ESTAB_DEFAULT : Strings.toUpperCase(invoiceData.getEstab());

        String ptoEmi = Constantes.SRI_PTO_EMISION_FACTURAS_ELECTRONICAS;

        String secuencial = Constantes.VACIO;

        if (Strings.isNullOrEmpty(invoiceData.getSecuencial())) {
            secuencial = serialService.getSecuencialGenerator(organizacion.getRuc(), Constantes.CM, estab, ptoEmi, organizacion.getAmbienteSRI()).next();
        } else {
            secuencial = invoiceData.getSecuencial();
        }

//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
//        System.out.println("ruc: " + organizacion.getRuc());
//        System.out.println("estab: " + estab);
//        System.out.println("ptoEmi: " + ptoEmi);
//        System.out.println("secuencial: " + secuencial);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<");
        VeronicaAPIData data = crearComprobante(token, Constantes.URI_API_V1_CM, secuencial, estab, ptoEmi, invoiceData, user);

        //Enviar a InternalInvoice (entidad invoice en appsventas), agregar un indicador de si ya se gener?? en el SRI
        InternalInvoice invoice = null;

        Optional<Subject> customerOpt = subjectService.encontrarPorId(invoiceData.getSubjectCustomer().getCustomerId());
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? un cliente v??lido para el usuario autenticado.");
        }

        if (subjectOpt.isPresent() && customerOpt.isPresent() && organizacion != null) {

            //Guardar el invoice en appsventas
            invoice = internalInvoiceService.crearInstancia(subjectOpt.get());
            BeanUtils.copyProperties(invoiceData, invoice, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            invoice.setOrganizacionId(organizacion.getId());
            invoice.setDocumentType(DocumentType.INVOICE);
            invoice.setSequencial(secuencial);
            invoice.setBoardNumber(Constantes.INVOICE_BOARD);
            invoice.setPax(Long.valueOf(Constantes.INVOICE_PAX));
            invoice.setPrintAlias(Boolean.FALSE);
            if (data.getResult() != null) {
                invoice.setClaveAcceso(data.getResult().getClaveAcceso());
            } else {
                invoice.setClaveAcceso(Constantes.PROFORMA + Constantes.SEPARADOR + UUID.randomUUID().toString());
            }
            invoice.setOwner(customerOpt.get());
            internalInvoiceService.guardar(invoice);

            final Long invoiceId = invoice.getId();
            List<Detail> details = new ArrayList<>();
            invoiceData.getDetails().forEach(dtl -> {
                Detail detail = detailService.crearInstancia(subjectOpt.get());
                detail.setInvoiceId(invoiceId);
                detail.setProductId(dtl.getProductId());
                detail.setAmount(dtl.getAmount()); //La cantidad que viene desde el front
                detail.setPrice(dtl.getPrice());
                detail.setIva12(dtl.isIVA12());
                details.add(detail);
            });

            detailService.guardar(details);

            //Guardar el subjectCustomer en caso a??n no sea parte del usuario
            if (invoiceData.getSubjectCustomer() != null && invoiceData.getSubjectCustomer().getId() == null) {
                SubjectCustomer subjectCustomer = null;
                subjectCustomer = subjectCustomerService.crearInstancia(subjectOpt.get());
                subjectCustomer.setSubjectId(user.getId());
                subjectCustomer.setCustomerId(customerOpt.get().getId());
                subjectCustomerService.guardar(subjectCustomer);
            }
        }

        if (invoiceData.getEnviarSRI()) {
            String accion = invoiceData.getAccionSRI();
            if (Constantes.ACCION_COMPROBANTE_ENVIAR.equalsIgnoreCase(accion)
                    || Constantes.ACCION_COMPROBANTE_AUTORIZAR.equalsIgnoreCase(accion)
                    || Constantes.ACCION_COMPROBANTE_EMITIR.equalsIgnoreCase(accion)) {

                String claveAcceso = data.getResult().getClaveAcceso();
                data = enviarComprobante(token, Constantes.URI_API_V1_INVOICE, claveAcceso, accion);

                //VeronicaAPIData data2 = enviarComprobante( token, Constantes.URI_API_V1_INVOICE, "0503202201110382696000110010010000000055093058218"); //verificado
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
//                System.out.println("Notificar via correo: APLLIED = " + data.getResult().getEstado());
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
                if (Constantes.SRI_STATUS_APPLIED.equalsIgnoreCase(data.getResult().getEstado())) {
                    //Notificar via correo
                    String titulo = "[Notificaci??n] FACTURA - SERVICIO $organizacionNombreCompleto";//catalogoService.obtenerValor("NOTIFICACION_CREACION_USUARIO_TITULO", "Notificaci??n de creaci??n de usuarios SMC");
                    String cuerpoMensaje = "<p>Estimada(o): <strong>$clienteNombreCompleto</strong></p>\n"
                            + "<p><br />Le informamos que su Factura n&uacute;mero <strong>$facturaSecuencia</strong> ha sido remitida por <strong>$organizacionNombreCompleto</strong> de forma Electr&oacute;nica.</p>\n"
                            + "<p>&nbsp;</p>\n"
                            + "<p>Si Usted es cliente del $organizacionNombreCompleto mantenga siempre a la mano las facturas que emite y recibe a trav&eacute;s de nuestra aplicaci&oacute;n <a title=\"FAZil facturaci&oacute;n electr&oacute;nica en 2 clics\" href=\"$url\" target=\"_blank\" rel=\"noopener noreferrer\">FAZil</a><br /><br />Atentamente,<br /><strong>$organizacionNombreCompleto.</strong></p>";//catalogoService.obtenerValor("NOTIFICACION_CREACION_USUARIO_MENSAJE", "Bienvenido $cedula - $nombres a SMC\nSus datos de acceso son:\nNombre de usuario: $nombreUsuario\nContrase??a: $contrasenia\n");
                    UserData destinatario = new UserData();
                    BeanUtils.copyProperties(customerOpt.get(), destinatario);
                    destinatario.setNombre(customerOpt.get().getFullName());
                    destinatario.setId(customerOpt.get().getId());

                    Map<String, Object> values = new HashMap<>();
                    values.put("facturaSecuencia", secuencial);
                    values.put("clienteNombreCompleto", Strings.toUpperCase(destinatario.getNombre()));
                    values.put("organizacionNombreCompleto", Strings.toUpperCase(organizacion.getName()));
                    values.put("url", "http://jlgranda.com/entry/fazil-facturacion-electronica-para-profesionales");
                    byte[] pdf = getPDF(Constantes.INVOICE, claveAcceso, user.getUsername());
                    byte[] xml = getXML(Constantes.INVOICE, claveAcceso, user.getUsername());

                    //La notificaciones siempre deben enviarse desde un mismo correo
                    user.setEmail("AppsVentas Plataforma <notificacion@jlgranda.com>"); //TODO obtener desde propiedades del sistema
                    EmailUtil.getInstance().enviarCorreo(user, destinatario, titulo, cuerpoMensaje, values, this.notificationService, this.messageService, claveAcceso, pdf, xml);
                }
            }
        }

        return ResponseEntity.ok(data);
    }

}
