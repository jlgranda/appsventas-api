/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.api.controller.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.app.Product;
import org.jlgranda.appsventas.domain.util.ProductType;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.ProductData;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.ProductService;
import org.jlgranda.appsventas.services.app.SubjectService;
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
@RequestMapping(path = "/servicios")
public class ServiciosController {

    private ProductService productService;
    private OrganizationService organizationService;
    private SubjectService subjectService;
    private final String ignoreProperties;

    @Autowired
    public ServiciosController(
            ProductService productService,
            OrganizationService organizationService,
            SubjectService subjectService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.productService = productService;
        this.organizationService = organizationService;
        this.subjectService = subjectService;
        this.ignoreProperties = ignoreProperties;
    }

    @GetMapping("organizacion/activos")
    public ResponseEntity encontrarPorOrganizacionId(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<ProductData> productsData = new ArrayList<>();
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (subjectOpt.isPresent()) {
            List<Organization> organization = organizationService.encontrarPorOwner(subjectOpt.get());
            if (!organization.isEmpty()) {
                productsData = buildResultListProduct(productService.encontrarPorOrganizacionId(organization.get(0).getId()));
            }
        }
        Api.imprimirGetLogAuditoria("servicios/organizacion/activos", user.getId());
        return ResponseEntity.ok(productsData);
    }

    @GetMapping("organizacion/tipo/{productType}/activos")
    public ResponseEntity encontrarPorOrganizacionIdYProductType(
            @AuthenticationPrincipal UserData user,
            @PathVariable("productType") ProductType productType,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<ProductData> productsData = new ArrayList<>();
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (subjectOpt.isPresent()) {
            List<Organization> organization = organizationService.encontrarPorOwner(subjectOpt.get());
            if (!organization.isEmpty()) {
                productsData = buildResultListProduct(productService.encontrarPorOrganizacionIdYProductType(organization.get(0).getId(), productType));
            }
        }
        Api.imprimirGetLogAuditoria("servicios/organizacion/tipo/productType/activos", user.getId());
        return ResponseEntity.ok(productsData);
    }

    private List<ProductData> buildResultListProduct(List<Product> products) {
        List<ProductData> productsData = new ArrayList<>();
        if (!products.isEmpty()) {
            products.forEach(p -> {
                productsData.add(productService.buildProductData(p));
            });
        }
        return productsData;
    }

}
