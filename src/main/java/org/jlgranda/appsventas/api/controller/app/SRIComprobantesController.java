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

import com.rolandopalermo.facturacion.ec.domain.Invoice;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import net.tecnopro.util.Dates;
import net.tecnopro.util.Strings;
import net.tecnopro.util.VelocityHelper;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Detail;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.jlgranda.appsventas.dto.ResultData;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.VeronicaAPIData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.TokenData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.services.app.ComprobantesService;
import org.jlgranda.appsventas.services.app.DetailService;
import org.jlgranda.appsventas.services.app.InternalInvoiceService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.SubjectCustomerService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.jlgranda.appsventas.services.secuencias.SerialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private InternalInvoiceService invoiceService;
    private DetailService detailService;
    private SubjectCustomerService subjectCustomerService;

    @Autowired
    public SRIComprobantesController(
            @Value("${appsventas.veronica.api.url}") String veronicaAPI,
            SubjectService subjectService,
            OrganizationService organizationService,
            SerialService serialService,
            InternalInvoiceService invoiceService,
            DetailService detailService,
            SubjectCustomerService subjectCustomerService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.veronicaAPI = veronicaAPI;
        this.subjectService = subjectService;
        this.organizationService = organizationService;
        this.serialService = serialService;
        this.invoiceService = invoiceService;
        this.detailService = detailService;
        this.subjectCustomerService = subjectCustomerService;
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

    private String getVeronicaToken(UserData user) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
        System.out.println("Username: " + user.getUsername());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");

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
        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            return Api.responseError("No se pudó obtener un token del API Verónica", null);
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

                System.out.println(">>>>>>>>>>>>>>>>>>");
                System.out.println("" + response.getStatusCode());
                System.out.println(">>>>>>>>>>>>>>>>>>");
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
            } else {
            }

            httpClientOrServerExc.printStackTrace();
        }
        return Api.responseError("No se pudó recuperar comprobantes para el tipo indicado.", tipo);
    }

    @PostMapping("/factura")
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
//        String token = this.getVeronicaToken(user);
        String token = Constantes.VERONICA_NO_TOKEN;
        VeronicaAPIData data = crearComprobante(token, Constantes.URI_API_V1_INVOICE, invoiceData, user);

        //Enviar a InternalInvoice (entidad invoice en appsventas), agregar un indicador de si ya se generó en el SRI
        InternalInvoice invoice = null;
        Detail detail = null;

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());

        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        Optional<Subject> customerOpt = subjectService.encontrarPorId(invoiceData.getSubjectCustomer().getCustomerId());
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("No se encontró un cliente válido para el usuario autenticado.");
        }

        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        if (subjectOpt.isPresent() && customerOpt.isPresent() && organizacion != null) {

            //Guardar el invoice
            invoice = invoiceService.crearInstancia(subjectOpt.get());
            BeanUtils.copyProperties(invoiceData, invoice, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            invoice.setOrganizacionId(organizacion.getId());
            invoice.setSequencial(Constantes.INVOICE_SEQUENCIAL);
            invoice.setBoardNumber(Constantes.INVOICE_BOARD);
            invoice.setPax(Long.valueOf(Constantes.INVOICE_PAX));
            invoice.setPrintAlias(Boolean.FALSE);
            if (data.getResult() != null) {
                invoice.setClaveAcceso(data.getResult().getClaveAcceso());
            }
            invoice.setOwner(customerOpt.get());
            invoiceService.guardar(invoice);

            //Guardar el detail
            detail = detailService.crearInstancia(subjectOpt.get());
            detail.setProductId(invoiceData.getProduct().getId());
            detail.setPrice(invoiceData.getSubTotal());
            detail.setAmount(invoiceData.getAmount());
            detail.setIva12(invoiceData.getIva12());
            detailService.guardar(detail);


            //Guardar el subjectCustomer en caso aún no sea parte del usuario
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
                data = enviarComprobante(token, Constantes.URI_API_V1_INVOICE, data.getResult().getClaveAcceso(), accion);
                //VeronicaAPIData data2 = enviarComprobante( token, Constantes.URI_API_V1_INVOICE, "0503202201110382696000110010010000000055093058218"); //verificado
            }
        }

        return ResponseEntity.ok(data);
    }

    /**
     *
     * @param tipo el tipo de comprobante
     * @return
     */
    private VeronicaAPIData crearComprobante(String token, String tipo, InvoiceData invoiceData, UserData user) {

        VeronicaAPIData data = new VeronicaAPIData();

        final String path = this.veronicaAPI + tipo;
        final String uri = path;
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
//        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            String message = "No se consiguió el token desde Veronica API";
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

        //Datos de la Organización (infoTributaria)
        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        Optional<Subject> customerOpt = subjectService.encontrarPorId(invoiceData.getSubjectCustomer().getCustomerId());

        if (organizacion == null) {
            String message = "No se encontró una organización válida para el usuario autenticado.";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);

            return data;
            ///throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }
        if (!subjectOpt.isPresent()) {
            String message = "No se encontró una sujeto emisor válido para el usuario autenticado.";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);

            return data;
            //throw new NotFoundException("No se encontró una sujeto emisor válido para el usuario autenticado.");
        }
        if (!customerOpt.isPresent()) {
            String message = "No se encontró una sujeto destinatario válido para el usuario autenticado.";
            ResultData errorData = new ResultData(false, message, new Error());
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
            //throw new NotFoundException("No se encontró una sujeto destinatario válido para el usuario autenticado.");
        }

        //Cargar generadores de serie para facturas
        Subject subject = subjectOpt.get();
        Subject customer = customerOpt.get();
        String estab = "001";
        String ptoEmi = "001";
        String secuencial = serialService.getSecuencialGenerator(Constantes.INVOICE, estab, ptoEmi).next();
        Map<String, Object> values = new HashMap<>();
        values.put("ambiente", "" + "1");
        values.put("tipoEmision", "" + "1");
        values.put("razonSocial", "" + organizacion.getName());
        values.put("nombreComercial", "" + organizacion.getInitials());
        values.put("ruc", "" + organizacion.getRuc());
        values.put("codDoc", "" + "01");
        values.put("estab", estab); //Por defecto un establecimiento
        values.put("ptoEmi", ptoEmi); ////Por defecto un punto de emision
        values.put("secuencial", Strings.extractLast(secuencial, "-"));

        values.put("dirMatriz", "" + subject.getDescription());
        values.put("dirEstablecimiento", "" + subject.getDescription());
        values.put("contribuyenteEspecial", Strings.isNullOrEmpty(subject.getNumeroContribuyenteEspecial()) ? "5368" : subject.getNumeroContribuyenteEspecial());
        values.put("obligadoContabilidad", "" + "NO");

        //Datos del invoiceData (infoFactura) //comprador
        values.put("fechaEmision", Dates.toString(invoiceData.getEmissionOn(), Constantes.FORMATO_FECHA_SRI));
        //[0-9]{3}-[0-9]{3}-[0-9]{9}
        //values.put("guiaRemision", "000-000-000000000"); //Según Karina no es necesario para servicios

        //Ver Pag 13, Tabla 6 para  más tipos de Identificación 04 -> RUc, 05 -> CEDULA
        values.put("tipoIdentificacionComprador", customer.getCode().length() == 13 ? "04" : "05");
        values.put("razonSocialComprador", "" + customer.getFullName());
        values.put("identificacionComprador", "" + customer.getCode());
        values.put("direccionComprador", "" + customer.getDescription());

        values.put("totalSinImpuestos", "" + invoiceData.getSubTotal());
        values.put("totalDescuento", "" + invoiceData.getDescuento());

        //IVA
        values.put("totalImpuestoCodigo", "" + "2");
        values.put("totalImpuestoCodigoPorcentaje", "" + "2");
        values.put("descuentoAdicional", "" + 0.00);
        values.put("totalImpuestoBaseImponible", invoiceData.getSubTotal());
        values.put("totalImpuestoValor", invoiceData.getIva12Total());

        values.put("propina", "" + invoiceData.getPropina());
        values.put("importeTotal", "" + invoiceData.getImporteTotal());
        values.put("moneda", "" + "DOLAR");

        //Falta el array de pagos
        values.put("formaPago", "" + "01");
        values.put("total", "" + "" + invoiceData.getImporteTotal());
        values.put("plazo", "" + "30");
        values.put("unidadTiempo", "dias");

        values.put("valorRetIva", "" + 0.00);
        values.put("valorRetRenta", "" + 0.00);

        //detalle
        values.put("codigoPrincipal", "" + invoiceData.getProduct().getId());
        values.put("codigoAuxiliar", "" + invoiceData.getProduct().getId());
        values.put("descripcion", "" + invoiceData.getProduct().getName());
        values.put("cantidad", "" + 1.00);
        values.put("precioUnitario", "" + invoiceData.getSubTotal());
        values.put("descuento", "" + 0.00);
        values.put("precioTotalSinImpuesto", invoiceData.getSubTotal());

        values.put("detAdicionalNombre1", "" + invoiceData.getProduct().getName());
        values.put("detAdicionalValor1", "" + invoiceData.getSubTotal());

        values.put("impuestoCodigo", "2");
        values.put("impuestoCodigoPorcentaje", "2");
        values.put("tarifa", "12");
        values.put("impuestoBaseImponible", invoiceData.getSubTotal());
        values.put("impuestoValor", "" + invoiceData.getImporteTotal());

        //Falta el array de campoAdicional
        values.put("campoAdicional", "" + "campoAdicional");

        StringBuilder json = new StringBuilder("$");

        try {
            json = new StringBuilder(VelocityHelper.getRendererMessage(Constantes.JSON_FACTURA_TEMPLATE, values));
        } catch (Exception ex) {

            Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
            String message = ex.getLocalizedMessage();
            ResultData errorData = new ResultData(false, message, ex);
            errorData.setSuccess(false);

            data.setSuccess(false);
            data.setErrors(errorData);
            return data;
        }

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

    /**
     *
     * @param token el tipo de comprobante
     * @param tipo el tipo de comprobante
     * @param claveAcceso el tipo de comprobante
     * @param accion la acción sobre el comprobante: enviar, autorizar, emitir,
     * anular
     * @return
     */
    private VeronicaAPIData enviarComprobante(String token, String tipo, String claveAcceso, String accion) {

        VeronicaAPIData data = new VeronicaAPIData();

        final String uri = this.veronicaAPI + tipo + "/" + claveAcceso + "/" + accion;

        if (Strings.isNullOrEmpty(claveAcceso)) {
            String message = "No se recibió una clave de acceso para enviar el comprobante";
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
            } else {
                data = response.getBody();
                data.getResult().setClaveAcceso(claveAcceso); //Por si se necesita en el cliente
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

}
