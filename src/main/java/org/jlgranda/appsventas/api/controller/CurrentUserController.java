package org.jlgranda.appsventas.api.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.validation.Valid;
import net.tecnopro.util.Strings;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.EmissionPoint;
import org.jlgranda.appsventas.domain.app.Establishment;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.UserImageData;
import org.jlgranda.appsventas.dto.UserModelData;
import org.jlgranda.appsventas.dto.UserWithToken;
import org.jlgranda.appsventas.dto.app.EmissionPointData;
import org.jlgranda.appsventas.dto.app.EstablishmentData;
import org.jlgranda.appsventas.dto.app.OrganizationData;
import org.jlgranda.appsventas.exception.NoAuthorizationException;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.exception.ResourceNotFoundException;
import org.jlgranda.appsventas.services.AuthorizationService;
import org.jlgranda.appsventas.services.DataService;
import org.jlgranda.appsventas.services.app.EmissionPointService;
import org.jlgranda.appsventas.services.app.EstablishmentService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.auth.UserService;
import org.jlgranda.appsventas.util.UserDataBuilder;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "/user")
public class CurrentUserController {

    private UserService userService;

    private OrganizationService organizationService;

    private EstablishmentService establishmentService;

    private EmissionPointService emissionPointService;

    private DataService dataService;

    private final String ignoreProperties;

    @Autowired
    public CurrentUserController(
            UserService userService,
            OrganizationService organizationService,
            EstablishmentService establishmentService,
            EmissionPointService emissionPointService,
            DataService dataService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
        this.userService = userService;
        this.organizationService = organizationService;
        this.establishmentService = establishmentService;
        this.emissionPointService = emissionPointService;
        this.dataService = dataService;
        this.ignoreProperties = ignoreProperties;
    }

    @GetMapping("image")
    public ResponseEntity currentUserImage(
            @AuthenticationPrincipal UserData userData,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {

        UserImageData userImageData = new UserImageData();

        Optional<Subject> userOpt = userService.getUserRepository().findByUsername(userData.getUsername());
        if (!userOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? una entidad Subject v??lida para el usuario autenticado.");
        }
        if (userOpt.isPresent()) {
            userImageData.setImageUser(userOpt.get().getPhoto() != null ? "data:image/png;base64," + Base64.toBase64String(userOpt.get().getPhoto()) : null);
        }

        Api.imprimirGetLogAuditoria("user/image/", userData.getId());
        return ResponseEntity.ok(userImageData);
    }

    /**
     * Soporte para descargar imagen de organizaci??n
     *
     * @param userData
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping("organization/image")
    public ResponseEntity currentUserOrganizationImage(
            @AuthenticationPrincipal UserData userData,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {

        UserImageData userImageData = new UserImageData();

        Optional<Subject> userOpt = userService.getUserRepository().findByUsername(userData.getUsername());
        if (!userOpt.isPresent()) {
            throw new NotFoundException("No se encontr?? una entidad Subject v??lida para el usuario autenticado.");
        }
        if (userOpt.isPresent()) {
            Organization organizacion = organizationService.encontrarPorSubjectId(userOpt.get().getId());
            if (organizacion != null) {
                userImageData.setImageOrganization(organizacion.getPhoto() != null ? "data:image/png;base64," + Base64.toBase64String(organizacion.getPhoto()) : null);
            }
        }

        Api.imprimirGetLogAuditoria("user/organization/image", userData.getId());
        return ResponseEntity.ok(userImageData);
    }

    @GetMapping
    public ResponseEntity currentUser(
            @AuthenticationPrincipal UserData currentUser,
            @RequestHeader(value = "Authorization") String authorization) throws JsonProcessingException {

        String token = authorization.split(" ")[1];

        UserData userData = UserDataBuilder.append(token);

        Optional<Subject> userOpt = userService.getUserRepository().findByUsername(userData.getUsername());
        Subject user = null; //El usuario local
        //Obtener desde la base de datos local para completar la informaci??n
        if (userOpt.isPresent()) {
            user = userOpt.get();
            userData.setId(user.getId());
            userData.setUuid(user.getUuid());
            userData.setNombre(user.getFullName());
            userData.setFirstname(user.getFirstname());
            userData.setSurname(user.getSurname());
            userData.setEmail(user.getEmail());
            userData.setDireccion(user.getDescription());
            userData.setMobileNumber(user.getMobileNumber());
            userData.setBio(user.getBio());
//            userData.setImage(user.getPhoto() != null ? "data:image/png;base64," + Base64.toBase64String(user.getPhoto()) : null);

            userData.setCode(user.getCode());

            //Datos de facturaci??n, forzar creaci??n de organizaci??n si tiene ruc.
            //No dispone de certificado digital
            userData.setTieneCertificadoDigital(Boolean.FALSE);

            String ruc = Strings.isNullOrEmpty(user.getRuc()) ? "" : user.getRuc().trim();
            if (Strings.isNullOrEmpty(ruc)) {
                if (user.getCode() != null && user.getCode().length() == 10) {
                    ruc = user.getCode().concat(Constantes.SRI_ESTAB_DEFAULT);
                    if (Strings.validateTaxpayerDocument(ruc)) {
                        user.setRuc(ruc);
                        userService.getUserRepository().save(user);//Guardar el ruc generado
                    }
                }
            }
            if (Strings.validateTaxpayerDocument(ruc)) {
                Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
                if (organizacion != null) {
                    //Datos para el usuario
                    userData.setRuc(organizacion.getRuc());
                    userData.setNombre(organizacion.getName());
                    userData.setInitials(organizacion.getInitials());
                    userData.setDireccion(organizacion.getDireccion());
                    //Cargar establishment
                    List<EmissionPointData> emissionPointsData = new ArrayList<>();
                    emissionPointService.encontrarActivosPorOrganizacionId(organizacion.getId()).forEach(empt -> emissionPointsData.add(emissionPointService.buildEmissionPoint(empt)));
                    Map<Long, List<EmissionPointData>> establishmentPoints = emissionPointsData.stream().collect(Collectors.groupingBy(EmissionPointData::getEstablishmentId));
                    List<EstablishmentData> establishmentsData = new ArrayList<>();
                    establishmentService.encontrarPorOrganizacionId(organizacion.getId()).forEach(est -> {
                        establishmentsData.add(establishmentService.buildEstablishment(est, establishmentPoints.get(est.getId())));
                    });
                    //Cargar data de organizacion
                    userData.setOrganization(organizationService.buildOrganization(organizacion, establishmentsData));

                    String sql = "select count(*) from public.sri_digital_cert where owner = '" + ruc + "' and active = true;";
                    if (BigInteger.ONE.equals(dataService.ejecutarCount(sql))) {
                        userData.setTieneCertificadoDigital(Boolean.TRUE);
                    }
                } else {
                    userData.setInitials(Constantes.NO_RUC);
                    userData.setRuc(user.getRuc());
                }
            } else {
                userData.setInitials(Constantes.NO_RUC);
                userData.setRuc(user.getRuc());
            }
        }

        List<String> roles = userData.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Logger.getLogger(CurrentUserController.class.getName()).log(Level.INFO, "Current user uuid: {0}", userData.getUuid());

        return ResponseEntity.ok(Api.response("user",
                new UserWithToken(userData, token, roles)
        ));
    }

    @GetMapping("/internal")
    public ResponseEntity currentUserInternal(@AuthenticationPrincipal UserData currentUser,
            @RequestHeader(value = "Authorization") String authorization) throws JsonProcessingException {

        String token = authorization.split(" ")[1];

        UserData userData = UserDataBuilder.append(token);

        Optional<Subject> userOpt = userService.getUserRepository().findByUsername(userData.getUsername());
        Subject user = null; //El usuario local
        //Obtener desde la base de datos local para completar la informaci??n
        if (userOpt.isPresent()) {
            user = userOpt.get();
            userData.setUuid(user.getUuid());
            userData.setNombre(user.getName());
            userData.setEmail(user.getEmail());
            userData.setBio(user.getBio());
        }
        List<String> roles = userData.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Logger.getLogger(CurrentUserController.class.getName()).log(Level.INFO, "Current user uuid: {0}", userData.getUuid());

        return ResponseEntity.ok(new UserWithToken(userData, token, roles));
    }

    @PutMapping()
    public ResponseEntity updateUser(
            @AuthenticationPrincipal UserData userAuth,
            @Valid @RequestBody UserModelData userData,
            BindingResult bindingResult) {
        return userService.getUserRepository().findByUUID(userData.getUuid()).map(user -> {
            if (!AuthorizationService.canWrite(userAuth, user)) {
                throw new NoAuthorizationException();
            }

            BeanUtils.copyProperties(userData, user, io.jsonwebtoken.lang.Strings.tokenizeToStringArray("id, uuid, code, email, username, password, authorities", ","));

            user.setUsuarioAPP(Boolean.TRUE);
            user.setDescription(userData.getDireccion());
            if (userData.getCode() != null) {
                user.setCode(userData.getCode());
            }

            if (!Strings.isNullOrEmpty(userData.getImage())) {
                String base64ImageString = userData.getImage().replace("data:image/jpeg;base64,", "");
                byte[] decodedImg = java.util.Base64.getDecoder()
                        .decode(base64ImageString.getBytes(StandardCharsets.UTF_8));
                user.setPhoto(decodedImg);
            }
//            } else {
//                user.setPhoto(null);
//            }

            userService.getUserRepository().save(user); //Guarda los cambios

            Api.imprimirUpdateLogAuditoria("/user", user.getId(), userData);
            return ResponseEntity.ok(Api.response("user", userData));
        }).orElseThrow(ResourceNotFoundException::new);
    }

}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class UpdateUserParam {

    @Email(message = "should be an email")
    private String email = "";
    private String password = "";
    private String username = "";
    private String bio = "";
    private String image = "";
}
