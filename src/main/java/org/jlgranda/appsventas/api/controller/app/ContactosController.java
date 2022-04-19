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
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
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
import org.jlgranda.appsventas.services.auth.UserService;
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
@RequestMapping(path = "/contactos")
public class ContactosController {

    private SubjectCustomerService subjectCustomerService;
    private SubjectService subjectService;
    private UserService userService;
    private final String ignoreProperties;

    @Autowired
    public ContactosController(
            SubjectCustomerService subjectCustomerService,
            SubjectService subjectService,
            UserService userService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.subjectCustomerService = subjectCustomerService;
        this.subjectService = subjectService;
        this.userService = userService;
        this.ignoreProperties = ignoreProperties;
    }

    @GetMapping("activos/keyword/{keyword}")
    public ResponseEntity encontrarPorKeyword(
            @AuthenticationPrincipal UserData user,
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        System.out.println("\n\n>>>>>>>>>>>><<");
        System.out.println("\n"+user.getId());
        System.out.println("\n"+keyword.trim());
        List<SubjectCustomerData> subjectCustomersData = buildResultListSubjectCustomer(user.getId(), subjectCustomerService.encontrarPorSubjectIdYKeyword(user.getId(), keyword.trim()));
        System.out.println(">>>>>>>>>>>><<\n\n");
        if (subjectCustomersData.isEmpty()) {
            subjectCustomersData.addAll(buildResultListSubjectCustomerBaseCustomer(user.getId(), subjectService.encontrarPorKeyword(keyword.trim())));
        }
        Api.imprimirGetLogAuditoria("contactos/activos/keyword/" + keyword, user.getId());
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

    @GetMapping("usuario/activos/code/{code}")
    public ResponseEntity getByCode(
            @AuthenticationPrincipal UserData user,
            @PathVariable("code") String code
    ) {
        SubjectData customerData = null;

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());

        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }

        if (subjectOpt.isPresent()) {
            Optional<Subject> customerOpt = userService.getUserRepository().findByCode(code);//Buscar por campo code
            if (!customerOpt.isPresent()) {
                customerOpt = userService.getUserRepository().findByRuc(code);//Buscar por campo ruc
            }
            if (customerOpt.isPresent()) {
                customerData = subjectService.buildSubjectData(customerOpt.get());
            }
        }
        Api.imprimirGetLogAuditoria("contactos/usuario/activos/code/", user.getId());
        return ResponseEntity.ok(customerData);
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

    @PutMapping()
    public ResponseEntity modificarSubjectCustomer(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody SubjectCustomerData subjectCustomerData,
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
        if (subjectCustomerData.getCustomer() == null) {
            throw new NotFoundException("No se encontró una entidad Subject válidad para el contacto a registrar.");
        }

        SubjectCustomer subjectCustomer = null;
        Subject newCustomer = new Subject();

        if (subjectOpt.isPresent() && subjectCustomerData.getCustomer() != null) {

            SubjectData newCustomerData = subjectCustomerData.getCustomer();
            Boolean isCI = net.tecnopro.util.Strings.validateNationalIdentityDocument(newCustomerData.getCode());
            Boolean isRUC = net.tecnopro.util.Strings.validateTaxpayerDocument(newCustomerData.getCode());

            if (Objects.equals(isCI, Boolean.FALSE) && Objects.equals(isRUC, Boolean.FALSE)) {
                throw new NotFoundException("C.I/RUC no es válido.");
            }
            if (newCustomerData.getId() != null) {
                //Verificar si el newCustomer existe.
                Optional<Subject> newCustomerOpt = subjectService.encontrarPorId(newCustomerData.getId());
                if (newCustomerOpt.isPresent()) {
                    newCustomer = newCustomerOpt.get();
                    if (!Objects.equals(newCustomer.getUsuarioAPP(), Boolean.TRUE)) {
                        //Verificar si el newCustomer no es usuario del APP para que sus datos pueden ser modificados parcialmente
                        newCustomer.setDescription(newCustomerData.getDescription());
                        if (!net.tecnopro.util.Strings.isNullOrEmpty(newCustomerData.getMobileNumber())) {
                            newCustomer.setMobileNumber(newCustomerData.getMobileNumber());
                        }
                        if (!net.tecnopro.util.Strings.isNullOrEmpty(newCustomerData.getWorkPhoneNumber())) {
                            newCustomer.setWorkPhoneNumber(newCustomerData.getWorkPhoneNumber());
                        }
                        subjectService.guardar(newCustomer);
                    }
                }
            } else {
                //Crear una nueva instancia de subject para el newCustomer
                newCustomer = subjectService.crearInstancia(subjectOpt.get());
                BeanUtils.copyProperties(newCustomerData, newCustomer, Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                newCustomer.setCode(newCustomerData.getCode());
                newCustomer.setCodeType(isCI ? CodeType.CEDULA : isRUC ? CodeType.RUC : CodeType.NONE);
                user.setRuc(isRUC ? newCustomer.getCode() : "");
                newCustomer.setDescription(newCustomerData.getDescription());
                newCustomer.setUsername(newCustomer.getEmail());
                newCustomer.setInitials(newCustomer.getFullName() != null ? newCustomer.getFullName() : Constantes.NO_ORGANIZACION);
                PasswordService svc = new DefaultPasswordService();
                newCustomer.setPassword(svc.encryptPassword("EL_PASSWORD_DEL_FRONT"));
                subjectService.guardar(newCustomer);
            }
            if (newCustomer.getId() != null) {
                //Guardar el subjectCustomer
                Optional<SubjectCustomer> subjectCustomerOpt = subjectCustomerService.encontrarPorSubjectIdCustomerId(user.getId(), newCustomer.getId());
                if (!subjectCustomerOpt.isPresent()) {
                    subjectCustomer = subjectCustomerService.crearInstancia(subjectOpt.get());
                    BeanUtils.copyProperties(subjectCustomerData, subjectCustomer, Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                    subjectCustomer.setSubjectId(user.getId());
                    subjectCustomer.setCustomerId(newCustomer.getId());
                    subjectCustomerService.guardar(subjectCustomer);

                    //Cargar datos de retorno al frontend
                    subjectCustomerData = subjectCustomerService.buildSubjectCustomerData(subjectCustomer, newCustomer);
                }
            }
        }

        Api.imprimirUpdateLogAuditoria("/contactos", user.getId(), subjectCustomerData);
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
