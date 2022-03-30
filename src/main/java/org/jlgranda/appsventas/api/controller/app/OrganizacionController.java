/*
 * Copyright (C) 2022 usuario
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import net.tecnopro.util.Strings;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.CuentaBancaria;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.UserImageData;
import org.jlgranda.appsventas.dto.app.CuentaBancariaData;
import org.jlgranda.appsventas.dto.app.OrganizationData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.exception.ResourceNotFoundException;
import org.jlgranda.appsventas.services.app.CuentaBancariaService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.jlgranda.appsventas.services.auth.UserService;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(path = "/organization")
public class OrganizacionController {

    private UserService userService;
    private SubjectService subjectService;
    private OrganizationService organizationService;
    private CuentaBancariaService cuentaBancariaService;
    private final String ignoreProperties;

    @Autowired
    public OrganizacionController(
            UserService userService,
            SubjectService subjectService,
            OrganizationService organizationService,
            CuentaBancariaService cuentaBancariaService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.organizationService = organizationService;
        this.cuentaBancariaService = cuentaBancariaService;
        this.ignoreProperties = ignoreProperties;
    }

    @GetMapping("/image")
    public ResponseEntity currentUserOrganizationImage(
            @AuthenticationPrincipal UserData userData,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {

        UserImageData userImageData = new UserImageData();

        Optional<Subject> subjectOpt = userService.getUserRepository().findByUsername(userData.getUsername());
        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }
        if (subjectOpt.isPresent()) {
            Organization organizacion = organizationService.encontrarPorSubjectId(subjectOpt.get().getId());
            if (organizacion != null) {
                userImageData.setImageOrganization(organizacion.getPhoto() != null ? "data:image/png;base64," + Base64.toBase64String(organizacion.getPhoto()) : null);
            }
        }

        Api.imprimirGetLogAuditoria("organization/image", userData.getId());
        return ResponseEntity.ok(userImageData);
    }

    @PutMapping()
    public ResponseEntity updateOrganization(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody OrganizationData organizationData,
            BindingResult bindingResult) {
        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion != null) {
            BeanUtils.copyProperties(organizationData, organizacion);
            if (!Strings.isNullOrEmpty(organizationData.getImage())) {
                String base64ImageString = organizationData.getImage().replace("data:image/jpeg;base64,", "");
                byte[] decodedImg = java.util.Base64.getDecoder()
                        .decode(base64ImageString.getBytes(StandardCharsets.UTF_8));
                organizacion.setPhoto(decodedImg);
            }
            organizationService.guardar(organizacion);
        }
        Api.imprimirUpdateLogAuditoria("organization", user.getId(), organizacion);
        return ResponseEntity.ok(Api.response("organization", organizationData));
    }

    @GetMapping("/cuentabancaria/activos")
    public ResponseEntity encontrarPorOrganizacionId(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        List<CuentaBancariaData> cuentasBancariasData = new ArrayList<>();

        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        if (organizacion != null) {
            cuentasBancariasData = buildResultListCuentaBancaria(cuentaBancariaService.encontrarPorOrganizacionId(organizacion.getId()));
        }
        Api.imprimirGetLogAuditoria("organization/cuentabancaria/activos", user.getId());
        return ResponseEntity.ok(cuentasBancariasData);
    }

    @PostMapping("/cuentabancaria")
    public ResponseEntity crearCuentaBancaria(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody CuentaBancariaData cuentaBancariaData,
            BindingResult bindingResult
    ) {
        //Verificar binding
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }

        Optional<Subject> subjectOpt = subjectService.encontrarPorId(user.getId());
        Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());

        if (!subjectOpt.isPresent()) {
            throw new NotFoundException("No se encontró una entidad Subject válida para el usuario autenticado.");
        }
        if (organizacion == null) {
            throw new NotFoundException("No se encontró una organización válida para el usuario autenticado.");
        }

        CuentaBancaria cuentaBancaria = cuentaBancariaService.crearInstancia(subjectOpt.get());
        BeanUtils.copyProperties(cuentaBancariaData, cuentaBancaria, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
        cuentaBancaria.setCode(cuentaBancariaData.getCode());
        cuentaBancaria.setOrganizacionId(organizacion.getId());
        cuentaBancariaService.guardar(cuentaBancaria);

        Api.imprimirPostLogAuditoria("organization/cuentabancaria", user.getId());
        return ResponseEntity.ok(Api.response("cuentaBancaria", cuentaBancaria));
    }

    @PutMapping("/cuentabancaria")
    public ResponseEntity modificarCuentaBancaria(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody CuentaBancariaData cuentaBancariaData,
            BindingResult bindingResult) {
        return cuentaBancariaService.encontrarPorUuid(cuentaBancariaData.getUuid()).map(cuentaBancaria -> {
            BeanUtils.copyProperties(cuentaBancariaData, cuentaBancaria, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            cuentaBancariaService.guardar(cuentaBancaria);
            Api.imprimirUpdateLogAuditoria("organization/cuentabancaria", user.getId(), cuentaBancariaData);
            return ResponseEntity.ok(Api.response("cuentaBancaria", cuentaBancaria));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    private List<CuentaBancariaData> buildResultListCuentaBancaria(List<CuentaBancaria> cuentasBancarias) {
        List<CuentaBancariaData> cuentasBancariasData = new ArrayList<>();
        if (!cuentasBancarias.isEmpty()) {
            cuentasBancarias.forEach(p -> {
                cuentasBancariasData.add(cuentaBancariaService.buildCuentaBancariaData(p));
            });
        }
        return cuentasBancariasData;
    }

}
