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
        Organization organizacion = encontrarOrganizacionPorSubjectId(user.getId());
        if (organizacion != null) {
            productsData = buildResultListProduct(productService.encontrarPorOrganizacionId(organizacion.getId()));
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
        Organization organizacion = encontrarOrganizacionPorSubjectId(user.getId());
        if (organizacion != null) {
            productsData = buildResultListProduct(productService.encontrarPorOrganizacionIdYProductType(organizacion.getId(), productType));
        }
        Api.imprimirGetLogAuditoria("servicios/organizacion/tipo/productType/activos", user.getId());
        return ResponseEntity.ok(productsData);
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
