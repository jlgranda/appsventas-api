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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import net.tecnopro.util.VelocityHelper;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.TokenData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.services.app.ComprobantesService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.SubjectService;
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

    @Autowired
    private ComprobantesService comprobantesService;

    private SubjectService subjectService;
    private OrganizationService organizationService;

    @Autowired
    public SRIComprobantesController(
            @Value("${appsventas.veronica.api.url}") String veronicaAPI,
            SubjectService subjectService,
            OrganizationService organizationService) {
        this.veronicaAPI = veronicaAPI;
        this.subjectService = subjectService;
        this.organizationService = organizationService;
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

        //Enviar a InternalInvoice (entidad invoice en appsventas), agregar un indicador de si ya se generó en el SRI
        //Invocar servicio veronica API
        final String path = this.veronicaAPI + Constantes.URI_API_V1_INVOICE;
        final String uri = path;

//        String token = this.getVeronicaToken(user);
        String token = " ";
        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);

        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)) {
            return Api.responseError("No se pudó obtener un token del API Verónica", null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + token);

        //TODO recuperar datos para la plantilla de facturación
        Map<String, Object> values = new HashMap<>();
        //Datos de la Organización (infoTributaria)
        Organization organizacion = encontrarOrganizacionPorSubjectId(user.getId());
        if (organizacion != null) {
            values.put("ambiente", "" + "1");
            values.put("tipoEmision", "" + "1");
            values.put("razonSocial", "" + organizacion.getName());
            values.put("nombreComercial", "" + organizacion.getInitials());
            values.put("ruc", "" + organizacion.getRuc());
            values.put("codDoc", "" + "1001");
            values.put("estab", "" + "001");
            values.put("ptoEmi", "" + "1245");
            values.put("secuencial", "" + "0151656");
            Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
            if (subjectOpt.isPresent()) {
                values.put("dirMatriz", "" + subjectOpt.get().getDescription());
                values.put("dirEstablecimiento", "" + subjectOpt.get().getDescription());
                values.put("contribuyenteEspecial", "" + subjectOpt.get().getNumeroContribuyenteEspecial());
                values.put("obligadoContabilidad", "" + "NO");
                values.put("tipoIdentificacionComprador", "" + "04");
                values.put("guiaRemision", "" + "001-001-000000001");

            }
        }
        //Datos del invoiceData (infoFactura)
        values.put("fechaEmision", "" + invoiceData.getEmissionOn());
        Optional<Subject> customerOpt = subjectService.encontrarPorId(invoiceData.getSubjectCustomer().getCustomerId());
        if (customerOpt.isPresent()) {
            values.put("razonSocialComprador", "" + customerOpt.get().getFullName());
            values.put("identificacionComprador", "" + customerOpt.get().getCode());
            values.put("direccionComprador", "" + customerOpt.get().getDescription());
        }
        values.put("totalSinImpuestos", "" + invoiceData.getSubTotal());
        values.put("totalDescuento", "" + invoiceData.getDescuento());

        //Falta el array de totalImpuesto
        values.put("totalImpuestoCodigo", "" + "2");
        values.put("totalImpuestoCodigoPorcentaje", "" + "2");
        values.put("descuentoAdicional", "" + 0.00);
        values.put("totalImpuestoBaseImponible", "" + 0.00);
        values.put("totalImpuestoValor", "" + 0.00);

        values.put("propina", "" + invoiceData.getPropina());
        values.put("importeTotal", "" + invoiceData.getImporteTotal());
        values.put("moneda", "" + "DOLAR");

        //Falta el array de pagos
        values.put("formaPago", "" + "Efectivo");
        values.put("total", "" + 0.00);
        values.put("plazo", "" + "plazo");
        values.put("unidadTiempo", "" + "unidadTiempo");

        values.put("valorRetIva", "" + 0.00);
        values.put("valorRetRenta", "" + 0.00);

        //Falta el array de detalle
        values.put("codigoPrincipal", "" + "codigoPrincipal");
        values.put("codigoAuxiliar", "" + "codigoAuxiliar");
        values.put("descripcion", "" + "descripcion");
        values.put("cantidad", "" + 0.00);
        values.put("precioUnitario", "" + 0.00);
        values.put("descuento", "" + 0.00);
        values.put("precioTotalSinImpuesto", "" + 0.00);

        values.put("detAdicionalNombre1", "" + "Nombre1");
        values.put("detAdicionalValor1", "" + "Valor1");
        values.put("detAdicionalNombre2", "" + "Nombre2");
        values.put("detAdicionalValor2", "" + "Valor2");
        values.put("detAdicionalNombre3", "" + "Nombre3");
        values.put("detAdicionalValor3", "" + "Valor3");

        values.put("impuestoCodigo", "" + "impuestoCodigo");
        values.put("impuestoCodigoPorcentaje", "" + "impuestoCodigoPorcentaje");
        values.put("tarifa", "" + 0.00);
        values.put("impuestoBaseImponible", "" + 0.00);
        values.put("impuestoValor", "" + 0.00);

        //Falta el array de campoAdicional
        values.put("campoAdicional", "" + "campoAdicional");

        StringBuilder json = new StringBuilder("$");

        try {
            json = new StringBuilder(VelocityHelper.getRendererMessage(Constantes.JSON_FACTURA_TEMPLATE, values));
        } catch (Exception ex) {
            Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("JSON GENERADO " + json);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {

            response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
            System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("response:::" + response);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println("response.getBody() A:::" + response.getBody());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                //OK
                return ResponseEntity.ok(response.getBody());
            } else {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println("response.getBody() B:::" + response.getBody());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                return ResponseEntity.ok(response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (null != httpClientOrServerExc.getStatusCode()) {
                switch (httpClientOrServerExc.getStatusCode()) {
                    case NOT_FOUND:
                        return Api.responseError("Warning", "VERONICAAPI: " + httpClientOrServerExc.getMessage(), httpClientOrServerExc.getStatusText());
                    case BAD_REQUEST:
                        return Api.responseError("Warning", "VERONICAAPI:" + httpClientOrServerExc.getMessage(), httpClientOrServerExc.getStatusText());
                    case INTERNAL_SERVER_ERROR:
                        return Api.responseError("Warning", "VERONICAAPI: Registro ingresado ya existe", httpClientOrServerExc.getStatusText());
                    default:
                        break;
                }
            }
        }

        return Api.responseError("El servidor no pudo generar el comprobante", new Error());
    }

    private Organization encontrarOrganizacionPorSubjectId(Long userId) {
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(userId);
        if (subjectOpt.isPresent()) {
            List<Organization> organization = organizationService.encontrarPorOwner(subjectOpt.get());
            if (!organization.isEmpty()) {
                return organization.get(0);
            }
        }
        return null;
    }

}
