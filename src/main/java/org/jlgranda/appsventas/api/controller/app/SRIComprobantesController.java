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

import java.util.HashMap;
import java.util.Map;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.InvoiceData;
import org.jlgranda.appsventas.dto.app.TokenData;
import org.jlgranda.appsventas.services.app.ComprobantesService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    public SRIComprobantesController(@Value("${appsventas.veronica.api.url}") String veronicaAPI) {
        this.veronicaAPI = veronicaAPI;
    }

    private String getVeronicaToken(UserData user){
        
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
        System.out.println("Username: " + user.getUsername());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
        
        final String uri = this.veronicaAPI + "/oauth/token";
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
            } /*else {

            }*/
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
            } else {
            }
            httpClientOrServerExc.printStackTrace();
        }
        
        return token != null ?  token.getAccess_token() : Constantes.VERONICA_NO_TOKEN;
    }
    
    @GetMapping("/{tipo}")
    public ResponseEntity getComprobantesPorTipo(@AuthenticationPrincipal UserData user,
            @PathVariable("tipo") String tipo){
        
        return ResponseEntity.ok(comprobantesService.findAllBySupplierId(tipo));
    }
    
    @GetMapping("/{tipo}/{claveAcceso}")
    public ResponseEntity getComprobante(@AuthenticationPrincipal UserData user,
            @PathVariable("tipo") String tipo, @PathVariable("claveAcceso") String claveAcceso){
        
        
        final String path = this.veronicaAPI + "/api/v1.0/";
        final String uri = !path.endsWith("/") ? (path + "/" + tipo) : (path + tipo);
        
        
        String token = this.getVeronicaToken(user);
        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< uri: " + uri);
        System.out.println(">>>>>>>>>>>>>>>>>>>><<<<<<< token: " + token);
        
        if (Constantes.VERONICA_NO_TOKEN.equalsIgnoreCase(token)){
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
    
    
    @PostMapping
    public ResponseEntity crearEnviarFactura(@AuthenticationPrincipal UserData user, InvoiceData invoiceData){
        return null;
    }
        
    
    
}
