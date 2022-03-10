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

import io.jsonwebtoken.lang.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.CodeType;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.app.SubjectCustomerData;
import org.jlgranda.appsventas.dto.app.SubjectData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.exception.NotFoundException;
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
import org.springframework.web.bind.annotation.PostMapping;
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
        List<SubjectCustomerData> subjectCustomersData = buildResultListSubjectCustomer(user.getId(), subjectCustomerService.encontrarPorSubjectIdYKeyword(user.getId(), keyword.trim()));
        if (subjectCustomersData.isEmpty()) {
            subjectCustomersData.addAll(buildResultListSubjectCustomerBaseCustomer(user.getId(), subjectService.encontrarPorKeyword(keyword.trim())));
        }
        Api.imprimirGetLogAuditoria("contactos/activos/keyword" + keyword, user.getId());
        return ResponseEntity.ok(subjectCustomersData);
    }

    @GetMapping("activos/{customerId}")
    public ResponseEntity encontrarPorID(
            @AuthenticationPrincipal UserData user,
            @PathVariable("customerId") Long customerId,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        SubjectData customerData = new SubjectData();

        Optional<Subject> customerOpt = subjectService.encontrarPorId(customerId);
        if (!customerOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }
        if (customerOpt.isPresent()) {
            customerData = subjectService.buildSubjectData(customerOpt.get());
        }
        Api.imprimirGetLogAuditoria("contactos/activos", user.getId());
        return ResponseEntity.ok(customerData);
    }

    @GetMapping("usuario/activos")
    public ResponseEntity encontrarPorSubjectId(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("contactos/usuario/activos", user.getId());
        return ResponseEntity.ok(buildResultListSubjectCustomer(user.getId(), subjectCustomerService.encontrarPorSubjectId(user.getId())));
    }

    @GetMapping("usuario/activos/keyword/{keyword}")
    public ResponseEntity encontrarPorSubjectIdYKeyword(
            @AuthenticationPrincipal UserData user,
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("contactos/usuario/activos/keyword", user.getId());
        return ResponseEntity.ok(buildResultListSubjectCustomer(user.getId(), subjectCustomerService.encontrarPorSubjectIdYKeyword(user.getId(), keyword.trim())));
    }

    @GetMapping("activos/initials/keyword/{keyword}")
    public ResponseEntity encontrarInitialsPorKeyword(
            @AuthenticationPrincipal UserData user,
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("activos/initials/keyword/" + keyword, user.getId());
        return ResponseEntity.ok(subjectService.encontrarInitialsPorKeyword(keyword.trim()));
    }

    @PostMapping()
    public ResponseEntity crearSubjectCustomer(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody SubjectCustomerData subjectCustomerData,
            BindingResult bindingResult
    ) {
        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        Subject customer = null;

        SubjectCustomer subjectCustomer = null;
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        if (subjectOpt.isPresent()) {
            //Guardar el customer
            customer = subjectService.crearInstancia(subjectOpt.get());
            BeanUtils.copyProperties(subjectCustomerData.getCustomer(), customer, Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            customer.setCode(subjectCustomerData.getCustomer().getCode());
            if (customer.getCode() != null && customer.getCode().length() == 10) {
                customer.setCodeType(CodeType.CEDULA);
            } else if (customer.getCode() != null && customer.getCode().length() == 13) {
                customer.setCodeType(CodeType.RUC);
            } else {
                customer.setCodeType(CodeType.NONE);
            }
            customer.setUsername(customer.getCode());
            customer.setPassword(Constantes.PASSWORD);
            customer.setSubjectType(Subject.Type.NATURAL);
            customer.setEmailSecret(Boolean.TRUE);
            customer.setContactable(Boolean.FALSE);
            subjectService.guardar(customer);
            //Guardar el subjectCustomer
            subjectCustomer = subjectCustomerService.crearInstancia(subjectOpt.get());
            BeanUtils.copyProperties(subjectCustomerData, subjectCustomer, Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            subjectCustomer.setSubjectId(user.getId());
            subjectCustomer.setCustomerId(customer.getId());
            subjectCustomerService.guardar(subjectCustomer);
            //Devolver subjectCustomerData
            subjectCustomerData = subjectCustomerService.buildSubjectCustomerData(subjectCustomer, customer);
        }

        Api.imprimirPostLogAuditoria("/contactos", user.getId());
        return ResponseEntity.ok(Api.response("subjectCustomer", subjectCustomerData));
    }

    @PutMapping()
    public ResponseEntity editarSubjectCustomer(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody SubjectCustomerData subjectCustomerData
    ) {
        SubjectCustomer subjectCustomer = null;
        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }
        Subject customer = null;
        if (subjectOpt.isPresent() && subjectCustomerData.getCustomer() != null && subjectCustomerData.getCustomer().getId() != null) {
            //Guardar el customer
            Optional<Subject> customerOpt = subjectService.encontrarPorId(subjectCustomerData.getCustomer().getId());

            if (customerOpt.isPresent()) {
                customer = customerOpt.get();
                BeanUtils.copyProperties(subjectCustomerData.getCustomer(), customer, Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                customer.setCode(subjectCustomerData.getCustomer().getCode());
                if (customer.getCode() != null && customer.getCode().length() == 10) {
                    customer.setCodeType(CodeType.CEDULA);
                } else if (customer.getCode() != null && customer.getCode().length() == 13) {
                    customer.setCodeType(CodeType.RUC);
                } else {
                    customer.setCodeType(CodeType.NONE);
                }
                subjectService.guardar(customer);
            }

            //Devolver subjectCustomerData
            subjectCustomerData = subjectCustomerService.buildSubjectCustomerData(subjectCustomer, customer);
        }

        Api.imprimirUpdateLogAuditoria("/contactos", user.getId(), subjectCustomerData.getCustomer());
        return ResponseEntity.ok(Api.response("subjectCustomer", subjectCustomerData));
    }

    private List<SubjectCustomerData> buildResultListSubjectCustomer(Long subjectId, List<SubjectCustomer> subjectCustomers) {
        List<SubjectCustomerData> subjectCustomersData = new ArrayList<>();
        if (!subjectCustomers.isEmpty()) {
            Map<Long, Subject> customersMap = new HashMap<>();
            subjectService.encontrarPorSubjectCustomerSubjectId(subjectId).forEach(c -> customersMap.put(c.getId(), c));
            subjectCustomers.forEach(sc -> {
                subjectCustomersData.add(subjectCustomerService.buildSubjectCustomerData(sc, customersMap.get(sc.getCustomerId())));
            });
        }
        return subjectCustomersData;
    }

    private List<SubjectCustomerData> buildResultListSubjectCustomerBaseCustomer(Long subjectId, List<Subject> customers) {
        List<SubjectCustomerData> subjectCustomersData = new ArrayList<>();
        if (!customers.isEmpty()) {
            customers.forEach(c -> {
                subjectCustomersData.add(subjectCustomerService.buildSubjectCustomerData(subjectId, c));
            });
        }
        return subjectCustomersData;
    }

}
