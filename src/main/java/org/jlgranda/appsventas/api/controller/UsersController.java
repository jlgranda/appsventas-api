package org.jlgranda.appsventas.api.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Strings;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.api.controller.app.SRIComprobantesController;
import org.jlgranda.appsventas.domain.CodeType;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.UserModelData;
import org.jlgranda.appsventas.exception.InvalidRequestException;
import org.jlgranda.appsventas.exception.NoAuthorizationException;
import org.jlgranda.appsventas.exception.NotFoundException;
import org.jlgranda.appsventas.exception.ResourceNotFoundException;
import org.jlgranda.appsventas.services.AuthorizationService;
import org.jlgranda.appsventas.services.EncryptService;
import org.jlgranda.appsventas.services.JwtService;
import org.jlgranda.appsventas.services.auth.UserService;
import org.jlgranda.appsventas.services.auth.UsersRolesService;
import org.jlgranda.util.AESUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UsersController {

    private UserService userService;
    private UsersRolesService usersRolesService;
    private final String ignoreProperties;
    private String defaultImage;
    private EncryptService encryptService;
    private JwtService jwtService;

    private static ObjectMapper mapper;

    PasswordService svc = new DefaultPasswordService();

    @Autowired
    public UsersController(
            UserService userService,
            UsersRolesService usersRolesService,
            EncryptService encryptService,
            JwtService jwtService,
            @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties) {
        this.userService = userService;
        this.usersRolesService = usersRolesService;
        this.encryptService = encryptService;
        this.jwtService = jwtService;

        //this.defaultImage = defaultImage;
        this.ignoreProperties = ignoreProperties;
    }

    @RequestMapping(path = "/users/code/{code}", method = GET)
    public ResponseEntity getByCode(
            @AuthenticationPrincipal UserData userData,
            @PathVariable("code") String code
    ) {
        UserModelData userModelData = new UserModelData();
        Optional<Subject> subjectOpt = userService.getUserRepository().findByCode(code);//Buscar por campo code
        if (!subjectOpt.isPresent()) {
            String ruc = code.length() == 10 ? code + "001" : code;
            subjectOpt = userService.getUserRepository().findByRuc(ruc);//Buscar por campo ruc
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><< Encontrado " + subjectOpt);
        if (subjectOpt.isPresent()) {
            Subject subject = subjectOpt.get();
            userModelData = new UserModelData();
            userModelData.setUuid(subject.getUuid());
            userModelData.setCode(subject.getCode());
            userModelData.setFirstname(subject.getFirstname());
            userModelData.setSurname(subject.getSurname());
            userModelData.setDireccion(subject.getDescription());
            userModelData.setEmail(subject.getEmail());
        }
        return ResponseEntity.ok(userModelData);
    }

    @RequestMapping(path = "/users/email/{email}", method = GET)
    public ResponseEntity getByEmail(
            @AuthenticationPrincipal UserData userData,
            @PathVariable("email") String email
    ) {
        Boolean correoExistente = Boolean.FALSE;
        Optional<Subject> subjectOpt = userService.getUserRepository().findByEmail(email.trim());//Buscar por campo code
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><< Encontrado " + subjectOpt);
        if (subjectOpt.isPresent()) {
            correoExistente = Boolean.TRUE;
        }
        return ResponseEntity.ok(correoExistente);
    }

    @RequestMapping(path = "/users", method = GET)
    public ResponseEntity getUsers(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        List<UserModelData> result = new ArrayList<>();
        UserModelData userModelData = null;

        //Armar lista de usuarios agregando el grado
        for (Subject usr : userService.encontrarActivos()) {
            userModelData = new UserModelData();
            BeanUtils.copyProperties(usr, userModelData); //Todo proteger datos o simplificar en UXData
            result.add(userModelData);
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(path = "/users/{uuid}", method = GET)
    public ResponseEntity getUserPorUUID(
            @AuthenticationPrincipal UserData user,
            @PathVariable("uuid") String uuid
    ) {
        Api.imprimirGetLogAuditoria("users/", user.getId());
        return ResponseEntity.ok(userService.findByUUID(uuid));
    }

    @RequestMapping(path = "/users/codigo/{codigo}", method = GET)
    public ResponseEntity getUserPorCodigo(
            @AuthenticationPrincipal UserData user,
            @PathVariable("codigo") String codigo) {
        Api.imprimirGetLogAuditoria("users/codigo", user.getId());
        return ResponseEntity.ok(userService.findByCodigo(codigo));
    }

    @RequestMapping(path = "/users/usuario/{uuid}", method = GET)
    public ResponseEntity getUsuarioPorUUID(@PathVariable("uuid") String uuid, @AuthenticationPrincipal UserData user) {
        Api.imprimirGetLogAuditoria("users/", user.getId());
        return ResponseEntity.ok(userService.encontrarPorUUID(uuid));
    }

    @RequestMapping(path = "/users", method = POST)
    public ResponseEntity createUser(
            @AuthenticationPrincipal UserData userData,
            @Valid @RequestBody UserModelData registerParam,
            BindingResult bindingResult
    ) {
        checkInput(registerParam, bindingResult);

        Boolean isCI = net.tecnopro.util.Strings.validateNationalIdentityDocument(registerParam.getCode());
        Boolean isRUC = net.tecnopro.util.Strings.validateTaxpayerDocument(registerParam.getCode());

        if (Objects.equals(isCI, Boolean.FALSE) && Objects.equals(isRUC, Boolean.FALSE)) {
            throw new NotFoundException("C.I/RUC no es válido.");
        }

        //Crear una nueva instancia de subject
        Subject user = userService.crearInstancia();
        BeanUtils.copyProperties(registerParam, user, Strings.tokenizeToStringArray(this.ignoreProperties, ","));
        user.setId(null);
        user.setCode(registerParam.getCode());
        user.setRuc(isRUC ? user.getCode() : "");
        user.setCodeType(isCI ? CodeType.CEDULA : isRUC ? CodeType.RUC : CodeType.NONE);
        user.setDescription(registerParam.getDireccion());
        user.setUsername(user.getEmail());
        user.setUsuarioAPP(Boolean.TRUE);
        user.setInitials(user.getFullName() != null ? user.getFullName() : Constantes.NO_ORGANIZACION);
        //La contraseña viene encriptada, desencriptar y encriptar para Shiro autenticación
        String plainText = "";
        try {
            plainText = AESUtil.decrypt("dXNyX2FwcGF0cGE6cHJ1ZWI0c19BVFBBXzIwMjA", registerParam.getPassword());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException | BadPaddingException ex) {
            Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }
        user.setPassword(svc.encryptPassword(plainText));
        userService.getUserRepository().save(user);

        //Cargar datos de retorno al frontend
        UserModelData userModelData = userService.findById(user.getId()).get();

        //Api.imprimirPostLogAuditoria("users/", user.getId());
        return ResponseEntity.ok(Api.response("user", userModelData));
    }

    @RequestMapping(path = "/users", method = PUT)
    public ResponseEntity updateUser(
            @AuthenticationPrincipal UserData userData,
            @Valid @RequestBody UserModelData registerParam,
            BindingResult bindingResult
    ) {

        return userService.getUserRepository().findByUUID(registerParam.getUuid()).map(user -> {

            Boolean isCI = net.tecnopro.util.Strings.validateNationalIdentityDocument(registerParam.getCode());
            Boolean isRUC = net.tecnopro.util.Strings.validateTaxpayerDocument(registerParam.getCode());

            if (Objects.equals(isCI, Boolean.FALSE) && Objects.equals(isRUC, Boolean.FALSE)) {
                throw new NotFoundException("C.I/RUC no es válido.");
            }

            BeanUtils.copyProperties(registerParam, user, Strings.tokenizeToStringArray(this.ignoreProperties, ","));
            user.setRuc(isRUC ? user.getCode() : "");
            user.setCodeType(isCI ? CodeType.CEDULA : isRUC ? CodeType.RUC : CodeType.NONE);
            user.setDescription(registerParam.getDireccion());
            user.setUsuarioAPP(Boolean.TRUE);
            user.setInitials(user.getFullName() != null ? user.getFullName() : Constantes.NO_ORGANIZACION);
            //La contraseña viene encriptada, desencriptar y encriptar para Shiro autenticación
            String plainText = "";
            try {
                plainText = AESUtil.decrypt("dXNyX2FwcGF0cGE6cHJ1ZWI0c19BVFBBXzIwMjA", registerParam.getPassword());
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException | BadPaddingException ex) {
                Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
                throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
            }
            user.setPassword(svc.encryptPassword(plainText));
            userService.getUserRepository().save(user);

            //Cargar datos de retorno al frontend
            UserModelData userModelData = userService.findById(user.getId()).get();

            Api.imprimirUpdateLogAuditoria("users/password", user.getId(), registerParam);
            return ResponseEntity.ok(Api.response("user", userModelData));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @RequestMapping(path = "/users/password", method = PUT)
    public ResponseEntity updateUserPassword(
            @AuthenticationPrincipal UserData userData,
            @Valid @RequestBody UserModelData registerParam,
            BindingResult bindingResult
    ) {

        return userService.getUserRepository().findByUUID(registerParam.getUuid()).map(user -> {
            //La contraseña viene encriptada, desencriptar y encriptar para Shiro autenticación
            String plainText = "";
            try {
                plainText = AESUtil.decrypt("dXNyX2FwcGF0cGE6cHJ1ZWI0c19BVFBBXzIwMjA", registerParam.getPassword());
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException | BadPaddingException ex) {
                Logger.getLogger(SRIComprobantesController.class.getName()).log(Level.SEVERE, null, ex);
                throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
            }
            user.setPassword(svc.encryptPassword(plainText));
            userService.getUserRepository().save(user);

            //Cargar datos de retorno al frontend
            UserModelData userModelData = userService.findById(user.getId()).get();

            Api.imprimirUpdateLogAuditoria("users/password", user.getId(), registerParam);
            return ResponseEntity.ok(Api.response("user", userModelData));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @RequestMapping(path = "/users/{uuid}", method = DELETE)
    public ResponseEntity deleteUser(
            @AuthenticationPrincipal UserData userDataAuth,
            @PathVariable("uuid") String uuid
    ) {
        return userService.getUserRepository().findByUUID(uuid).map(user -> {
            if (!AuthorizationService.canWrite(userDataAuth, user)) {
                throw new NoAuthorizationException();
            }
            user.setDeleted(Boolean.TRUE);
            userService.getUserRepository().save(user);
            return ResponseEntity.ok(Api.response("user", userService.findById(user.getId()).get()));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    private void checkInput(@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        if (userService.getUserRepository().findByUsername(registerParam.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
        }
        if (userService.getUserRepository().findByEmail(registerParam.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
        }
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
    }

    private void checkInput(@Valid @RequestBody UserModelData userData, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        if (userService.getUserRepository().findByUsername(userData.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "DUPLICATED", "Nombre de usuario duplicado");
        }
        if (userService.getUserRepository().findByEmail(userData.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "DUPLICATED", "Correo electrónico duplicado");
        }
        if (userService.getUserRepository().findByCode(userData.getCode()).isPresent()) {
            bindingResult.rejectValue("codigo", "DUPLICATED", "Número de cédula duplicado");
        }
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
    }

}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class LoginParam {

//    @NotBlank(message = "can't be empty")
//    @Email(message = "should be an email")
//    private String email;
    @NotBlank(message = "can't be empty")
    private String username;
    @NotBlank(message = "can't be empty")
    private String password;
}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class RegisterParam {

    @NotBlank(message = "No puede ser vacio")
    @Email(message = "Debe ser un correo electrónico")
    private String email;
    @NotBlank(message = "No puede ser vacio")
    private String username;
    @NotBlank(message = "No puede ser vacio")
    private String password;
}
