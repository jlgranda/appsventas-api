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
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.UserWithToken;
import org.jlgranda.appsventas.services.auth.UserService;
import org.jlgranda.appsventas.util.UserDataBuilder;

@RestController
@RequestMapping(path = "/user")
public class CurrentUserController {

    @Autowired
    private UserService userService;
   
    @Autowired
    public CurrentUserController(UserService userService){
        this.userService = userService;
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
            userData.setNombre(user.getName());
            userData.setEmail(user.getEmail());
            userData.setBio(user.getBio());
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
