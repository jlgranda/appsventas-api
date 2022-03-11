package org.jlgranda.appsventas.api.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.validation.Valid;
import net.tecnopro.util.Strings;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.UserWithToken;
import org.jlgranda.appsventas.exception.NoAuthorizationException;
import org.jlgranda.appsventas.exception.ResourceNotFoundException;
import org.jlgranda.appsventas.services.AuthorizationService;
import org.jlgranda.appsventas.services.app.OrganizationService;
import org.jlgranda.appsventas.services.auth.UserService;
import org.jlgranda.appsventas.util.UserDataBuilder;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/user")
public class CurrentUserController {

    private UserService userService;

    private OrganizationService organizationService;

    @Autowired
    public CurrentUserController(UserService userService,
            OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity currentUser(@AuthenticationPrincipal UserData currentUser,
            @RequestHeader(value = "Authorization") String authorization) throws JsonProcessingException {

        String token = authorization.split(" ")[1];

        UserData userData = UserDataBuilder.append(token);

        Optional<Subject> userOpt = userService.getUserRepository().findByUsername(userData.getUsername());
        Subject user = null; //El usuario local
        //Obtener desde la base de datos local para completar la información
        if (userOpt.isPresent()) {
            user = userOpt.get();
            userData.setId(user.getId());
            userData.setUuid(user.getUuid());
            userData.setNombre(user.getFullName());
            userData.setEmail(user.getEmail());
            userData.setBio(user.getBio());
            userData.setImage(user.getPhoto() != null ? "data:image/png;base64," + Base64.toBase64String(user.getPhoto()) : null);

            //Datos de facturación, forzar creación de organización si tiene ruc.
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
            System.out.println("Obtener organización para: " + user.getRuc());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<");
            if (Strings.validateTaxpayerDocument(user.getRuc())) {
                Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
                if (organizacion != null) {
                    userData.setRuc(organizacion.getRuc());
                    userData.setNombre(organizacion.getName());
                    userData.setInitials(organizacion.getInitials());
                    userData.setDireccion(organizacion.getDireccion());
                } else {
                    userData.setInitials(Constantes.NO_ORGANIZACION);
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
        //Obtener desde la base de datos local para completar la información
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

    @PutMapping
    public ResponseEntity updateUser(@AuthenticationPrincipal UserData userAuth,
            @Valid @RequestBody UserData userData,
            BindingResult bindingResult) {
        //checkInput(userData, bindingResult);
        return userService.getUserRepository().findById(userData.getId()).map(user -> {
            if (!AuthorizationService.canWrite(userAuth, user)) {
                throw new NoAuthorizationException();
            }
            BeanUtils.copyProperties(userData, user, io.jsonwebtoken.lang.Strings.tokenizeToStringArray("id, photo, uuid, authorities", ","));
            userService.getUserRepository().save(user); //Guarda los cambios

            //Obtener/Crear la organización para el usuario
            if (Strings.validateTaxpayerDocument(user.getRuc())) {
                Organization organizacion = organizationService.encontrarPorSubjectId(user.getId());
                if (organizacion != null) {
                    userData.setRuc(organizacion.getRuc());
                    userData.setNombre(organizacion.getName());
                    userData.setInitials(organizacion.getInitials());
                    userData.setDireccion(organizacion.getDireccion());
                } else {
                    userData.setInitials(Constantes.NO_ORGANIZACION);
                }
            } else {
                userData.setInitials(Constantes.NO_RUC);
                userData.setRuc(user.getRuc());
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
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
