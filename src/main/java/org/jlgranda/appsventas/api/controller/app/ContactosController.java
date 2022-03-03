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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.SubjectCustomerData;
import org.jlgranda.appsventas.services.app.SubjectCustomerService;
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
@RequestMapping(path = "/contactos")
public class ContactosController {

    private SubjectCustomerService subjectCustomerService;
    private SubjectService subjectService;
    private final String ignoreProperties;

    @Autowired
    public ContactosController(
            SubjectCustomerService subjectCustomerService,
            SubjectService subjectService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.subjectCustomerService = subjectCustomerService;
        this.subjectService = subjectService;
        this.ignoreProperties = ignoreProperties;
    }

    @GetMapping("activos/keyword/{keyword}")
    public ResponseEntity encontrarPorKeyword(
            @AuthenticationPrincipal UserData user,
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<SubjectCustomerData> subjectCustomersData = buildResultListSubjectCustomer(subjectCustomerService.encontrarPorSubjectIdYKeyword(user.getId(), keyword));
        if (subjectCustomersData.isEmpty()) {
            subjectCustomersData.addAll(buildResultListSubjectCustomerBaseSubject(user.getId(), subjectService.encontrarPorKeyword(keyword)));
        }
        Api.imprimirGetLogAuditoria("contactos/activos/keyword", user.getId());
        return ResponseEntity.ok(subjectCustomersData);
    }

    @GetMapping("usuario/activos")
    public ResponseEntity encontrarPorSubjectId(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("contactos/usuario/activos", user.getId());
        return ResponseEntity.ok(buildResultListSubjectCustomer(subjectCustomerService.encontrarPorSubjectId(user.getId())));
    }

    @GetMapping("usuario/activos/keyword/{keyword}")
    public ResponseEntity encontrarPorSubjectIdYKeyword(
            @AuthenticationPrincipal UserData user,
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("contactos/usuario/activos/keyword", user.getId());
        return ResponseEntity.ok(buildResultListSubjectCustomer(subjectCustomerService.encontrarPorSubjectIdYKeyword(user.getId(), keyword)));
    }

    private List<SubjectCustomerData> buildResultListSubjectCustomer(List<SubjectCustomer> subjectCustomers) {
        List<SubjectCustomerData> subjectCustomersData = new ArrayList<>();
        if (!subjectCustomers.isEmpty()) {
            Map<Long, Subject> subjectsMap = new HashMap<>();
            subjectService.encontrarActivos().forEach(s -> subjectsMap.put(s.getId(), s));
            subjectCustomers.forEach(sc -> {
                subjectCustomersData.add(subjectCustomerService.buildSubjectCustomerData(sc, subjectsMap.get(sc.getSubjectId())));
            });
        }
        return subjectCustomersData;
    }

    private List<SubjectCustomerData> buildResultListSubjectCustomerBaseSubject(Long subjectId, List<Subject> subjects) {
        List<SubjectCustomerData> subjectCustomersData = new ArrayList<>();
        if (!subjects.isEmpty()) {
            subjects.forEach(s -> {
                subjectCustomersData.add(subjectCustomerService.buildSubjectCustomerData(subjectId, s));
            });
        }
        return subjectCustomersData;
    }

}
