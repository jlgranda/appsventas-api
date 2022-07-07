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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import net.tecnopro.util.Strings;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.CuentaBancaria;
import org.jlgranda.appsventas.domain.app.EmissionPoint;
import org.jlgranda.appsventas.domain.app.Establishment;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.domain.secuencias.Secuencia;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.UserImageData;
import org.jlgranda.appsventas.dto.app.CuentaBancariaData;
import org.jlgranda.appsventas.dto.app.EmissionPointData;
import org.jlgranda.appsventas.dto.app.EstablishmentData;
import org.jlgranda.appsventas.dto.app.EstablishmentListData;
import org.jlgranda.appsventas.dto.app.OrganizationData;
import org.jlgranda.appsventas.dto.app.SecuenciaData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.exception.ResourceNotFoundException;
import org.jlgranda.appsventas.services.app.CuentaBancariaService;
import org.jlgranda.appsventas.services.app.EmissionPointService;
import org.jlgranda.appsventas.services.app.EstablishmentService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.app.SubjectService;
import org.jlgranda.appsventas.services.auth.UserService;
import org.jlgranda.appsventas.services.secuencias.SerialService;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(path = "/organization")
public class OrganizacionController {

    private UserService userService;
    private SubjectService subjectService;
    private OrganizationService organizationService;
    private CuentaBancariaService cuentaBancariaService;
    private EstablishmentService establishmentService;
    private EmissionPointService emissionPointService;
    private SerialService serialService;
    private final String ignoreProperties;

    @Autowired
    public OrganizacionController(
            UserService userService,
            SubjectService subjectService,
            OrganizationService organizationService,
            CuentaBancariaService cuentaBancariaService,
            EstablishmentService establishmentService,
            EmissionPointService emissionPointService,
            SerialService serialService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.organizationService = organizationService;
        this.cuentaBancariaService = cuentaBancariaService;
        this.establishmentService = establishmentService;
        this.emissionPointService = emissionPointService;
        this.serialService = serialService;
        this.ignoreProperties = ignoreProperties;
    }

    /**
     * Organization
     *
     * @param userData
     * @param offset
     * @param limit
     * @return
     */
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
            if (organizationData.getAmbientePro() != null && Objects.equals(Boolean.TRUE, organizationData.getAmbientePro())) {
                organizacion.setAmbienteSRI(Constantes.AMBIENTE_PRODUCCION);
            } else {
                organizacion.setAmbienteSRI(Constantes.AMBIENTE_DESARROLLO);
            }
            organizationService.guardar(organizacion);
        }

        //Recargar data
        OrganizationData organizationDataUpdate = new OrganizationData();
        BeanUtils.copyProperties(organizacion, organizationDataUpdate);
        organizationDataUpdate.setImage(organizationData.getImage());

        Api.imprimirUpdateLogAuditoria("organization", user.getId(), organizacion);
        return ResponseEntity.ok(Api.response("organization", organizationData));
    }

    /**
     * CuentaBancaria
     *
     * @param user
     * @param offset
     * @param limit
     * @return
     */
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

        Optional<CuentaBancaria> cuentaBancariaOpt = cuentaBancariaService.encontrarPorCode(cuentaBancariaData.getCode());
        if (!cuentaBancariaOpt.isPresent()) {//No existe esa cuenta
            CuentaBancaria cuentaBancaria = cuentaBancariaService.crearInstancia(subjectOpt.get());
            BeanUtils.copyProperties(cuentaBancariaData, cuentaBancaria, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            cuentaBancaria.setCode(cuentaBancariaData.getCode());
            cuentaBancaria.setOrganizacionId(organizacion.getId());
            cuentaBancariaService.guardar(cuentaBancaria);
            //Cargar datos de retorno al frontend
            cuentaBancariaData = cuentaBancariaService.buildCuentaBancaria(cuentaBancaria);
        }

        Api.imprimirPostLogAuditoria("organization/cuentabancaria", user.getId());
        return ResponseEntity.ok(Api.response("cuentaBancaria", cuentaBancariaData));
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
            return ResponseEntity.ok(Api.response("cuentaBancaria", cuentaBancariaData));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @DeleteMapping("/cuentabancaria/{uuid}")
    public ResponseEntity eliminarCuentaBancaria(
            @AuthenticationPrincipal UserData user,
            @PathVariable("uuid") String uuid) {
        return cuentaBancariaService.encontrarPorUuid(uuid).map(cuentaBancaria -> {
            cuentaBancaria.setDeleted(true);
            cuentaBancariaService.guardar(cuentaBancaria);
            Api.imprimirUpdateLogAuditoria("organization/cuentabancaria/uuid", user.getId(), cuentaBancaria);
            return ResponseEntity.ok(Api.response("cuentaBancaria", cuentaBancaria));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    private List<CuentaBancariaData> buildResultListCuentaBancaria(List<CuentaBancaria> cuentasBancarias) {
        List<CuentaBancariaData> cuentasBancariasData = new ArrayList<>();
        if (!cuentasBancarias.isEmpty()) {
            cuentasBancarias.forEach(p -> {
                cuentasBancariasData.add(cuentaBancariaService.buildCuentaBancaria(p));
            });
        }
        return cuentasBancariasData;
    }

    /**
     * Establishment
     *
     * @param user
     * @param establishmentData
     * @param bindingResult
     * @return
     */
    @PostMapping("/establishment")
    public ResponseEntity crearEstablecimiento(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody EstablishmentData establishmentData,
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

        Optional<Establishment> establishmentOpt = establishmentService.encontrarPorOrganizacionIdYNombre(organizacion.getId(), establishmentData.getName());
        if (!establishmentOpt.isPresent()) {//No existe ese establecimiento
            Establishment establishment = establishmentService.crearInstancia(subjectOpt.get());
            BeanUtils.copyProperties(establishmentData, establishment, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            establishment.setOrganizacionId(organizacion.getId());
            establishmentService.guardar(establishment);
            //Guardar puntos de emision
            List<EmissionPoint> emissionPoints = new ArrayList<>();
            if (establishmentData.getEmisionPointsData() != null && !establishmentData.getEmisionPointsData().isEmpty()) {
                EmissionPoint emissionPoint = null;
                for (EmissionPointData empt : establishmentData.getEmisionPointsData()) {
                    if (empt.getId() == null) {
                        emissionPoint = emissionPointService.crearInstancia(subjectOpt.get());
                        BeanUtils.copyProperties(empt, emissionPoint, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                        emissionPoint.setEstablishmentId(establishment.getId());
                        emissionPoints.add(emissionPoint);
                    } else {
                        BeanUtils.copyProperties(empt, emissionPoint, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                        emissionPoint.setLastUpdate(new Date());
                        emissionPoints.add(emissionPoint);
                    }
                }
                emissionPointService.guardarLista(emissionPoints);
                //Cargar datos de retorno al frontend
                establishmentData = establishmentService.buildEstablishment(establishment, buildResultListEmissionPoint(emissionPoints));
            } else {
                //Cargar datos de retorno al frontend
                establishmentData = establishmentService.buildEstablishment(establishment);
            }
        }
        Api.imprimirPostLogAuditoria("organization/establishment", user.getId());
        return ResponseEntity.ok(Api.response("establishment", establishmentData));
    }

    private List<EmissionPointData> buildResultListEmissionPoint(List<EmissionPoint> emissionPoints) {
        List<EmissionPointData> emissionPointsData = new ArrayList<>();
        if (!emissionPoints.isEmpty()) {
            emissionPoints.forEach(p -> {
                emissionPointsData.add(emissionPointService.buildEmissionPoint(p));
            });
        }
        return emissionPointsData;
    }

    @PutMapping("/establishment/serial")
    public ResponseEntity actualizarSerialEstablisment(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody SecuenciaData secuenciaData,
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
        
        Optional<Secuencia> secuenciaOpt = serialService.encontrarPorEntidadEstabPtoEmiAmbienteValor(organizacion.getRuc(), secuenciaData.getEntidad(), secuenciaData.getEstab(), secuenciaData.getPtoEmi(), organizacion.getAmbienteSRI(), secuenciaData.getValorActual());

        Api.imprimirUpdateLogAuditoria("organization/establishment", user.getId(), secuenciaOpt.get());
        return ResponseEntity.ok(Api.response("secuencia", secuenciaOpt.get()));
    }

    @PutMapping("/establishments")
    public ResponseEntity guardarEstablecimientos(
            @AuthenticationPrincipal UserData user,
            @Valid @RequestBody EstablishmentListData establishmentListData,
            BindingResult bindingResult) {
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

        List<EstablishmentData> establishmentsData = new ArrayList<>();
        if (establishmentListData.getEstablishmentsData() != null && !establishmentListData.getEstablishmentsData().isEmpty()) {
            for (EstablishmentData establishmentData : establishmentListData.getEstablishmentsData()) {
                Establishment establishment = null;
                Optional<Establishment> establishmentOpt = establishmentService.encontrarPorOrganizacionIdYNombre(organizacion.getId(), establishmentData.getName());
                if (!establishmentOpt.isPresent()) {//No existe ese establecimiento
                    establishment = establishmentService.crearInstancia(subjectOpt.get());
                    BeanUtils.copyProperties(establishmentData, establishment, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                    establishment.setOrganizacionId(organizacion.getId());
                    establishmentService.guardar(establishment);
                } else {
                    BeanUtils.copyProperties(establishmentData, establishment, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                    establishment.setLastUpdate(new Date());
                    establishmentService.guardar(establishment);
                }
                //Guardar puntos de emision
                if (establishmentData.getEmisionPointsData() != null && !establishmentData.getEmisionPointsData().isEmpty()) {
                    List<EmissionPoint> emissionPoints = new ArrayList<>();
                    EmissionPoint emissionPoint = null;
                    for (EmissionPointData emissionPointData : establishmentData.getEmisionPointsData()) {
                        if (emissionPointData.getId() == null) {
                            emissionPoint = emissionPointService.crearInstancia(subjectOpt.get());
                            BeanUtils.copyProperties(emissionPointData, emissionPoint, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                            emissionPoint.setEstablishmentId(establishment.getId());
                            emissionPoints.add(emissionPoint);
                        } else {
                            BeanUtils.copyProperties(emissionPointData, emissionPoint, io.jsonwebtoken.lang.Strings.tokenizeToStringArray(this.ignoreProperties, ","));
                            emissionPoint.setLastUpdate(new Date());
                            emissionPoints.add(emissionPoint);
                        }
                    }
                    emissionPointService.guardarLista(emissionPoints);
                    //Cargar datos de retorno al frontend
                    establishmentData = establishmentService.buildEstablishment(establishment, buildResultListEmissionPoint(emissionPoints));
                } else {
                    //Cargar datos de retorno al frontend
                    establishmentData = establishmentService.buildEstablishment(establishment);
                }
                //Cargar datos de retorno al frontend
                establishmentsData.add(establishmentData);
            }
        }
        Api.imprimirUpdateLogAuditoria("organization/establishment", user.getId(), establishmentsData);
        return ResponseEntity.ok(Api.response("establishments", establishmentsData));
    }

}
