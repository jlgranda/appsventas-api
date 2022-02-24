package org.jlgranda.appsventas.security;


import org.jlgranda.appsventas.services.auth.UserService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tecnopro.util.Strings;
import org.jlgranda.appsventas.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.exception.NoAuthorizationException;
import org.jlgranda.appsventas.util.UserDataBuilder;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

//    @Autowired
//    private JwtService jwtService;

    private String header = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getTokenString(request.getHeader(header)).ifPresent(token -> {

            if (SecurityContextHolder.getContext().getAuthentication() == null
                    && !Strings.isNullOrEmpty(token)) {
                
                UserData userData = UserDataBuilder.append(token);

                if (userData.isExpired()) {
                    Logger.getLogger(JwtTokenFilter.class.getName()).log(Level.INFO, "Autorización caducada para el username {0} in token.", userData.getUsername());
                    throw new NoAuthorizationException();
                } else {

                    Logger.getLogger(JwtTokenFilter.class.getName()).log(Level.INFO, "Look for user by username {0} in token.", userData.getUsername());

                    if (!Strings.isNullOrEmpty(userData.getUsername())) {

                        Optional<Subject> userOpt = userService.getUserRepository().findByUsername(userData.getUsername());

                        Subject user = null; //El usuario local
                        //Obtener desde la base de datos local para completar la información
                        if (!userOpt.isPresent()) {
                            user = userService.crearInstancia();
                            user.setId(userData.getId());
                            user.setName(Strings.isNullOrEmpty(userData.getNombre()) ? "Sin nombre [" + userData.getId() + "]" : userData.getNombre());
                            user.setUsername(userData.getUsername());
                            user.setEmail(Strings.isNullOrEmpty(userData.getEmail()) ? userData.getUsername() + "@mag.gob.ec" : userData.getEmail());
                            //user.setUsuarioCreacion(userData.getPersonId());
                            user.setBio(userData.getBio());
                            //user.setCodigo("APPSVENTAS_" + userData.getPersonId()); //El id de persona en el sistema APPSVENTAS
                            //user.setPersonId(userData.getPersonId()); //El id de persona en el sistema APPSVENTAS
                            
                            userService.getUserRepository().save(user);

                            //recuperar el uuid 
                            user = userService.getUserRepository().findByUsername(userData.getUsername()).get();

                            userData.setUuid(user.getUuid());
                        }

                        user = userOpt.get();

//                        if ( !Constantes.DUMMY_PERSON_ID.equals(user.getPersonId()) ) { //Es un usuario sin correspondiente en MAG, actualizar
//                            user.setPersonId(userData.getPersonId());
//                            userService.getUserRepository().save(user); //Actualizar el personid en la base de datos local
//                        }

                        userData.setId(user.getId());
                        userData.setUuid(user.getUuid());
                        userData.setNombre(user.getName());
                        userData.setEmail(user.getEmail());
                        userData.setBio(user.getBio());

                        UsernamePasswordAuthenticationToken authenticationToken
                                = new UsernamePasswordAuthenticationToken(
                                        userData,
                                        null,
                                        userData.getAuthorities()
                                );
                        Logger.getLogger(JwtTokenFilter.class.getName()).log(Level.INFO, "establish user {0} for token, with roles {1}.", new Object[]{user, userData.getAuthorities()});
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        Logger.getLogger(JwtTokenFilter.class.getName()).log(Level.INFO, "User from id {0}, personId {1} and uuid {2}",  new Object[]{userData.getId(), userData.getId(), userData.getUuid()});

                    }
                }
            }
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenString(String header) {
        if (header == null) {
            return Optional.empty();
        } else {
            String[] split = header.split(" ");
            if (split.length < 2) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(split[1]);
            }
        }
    }

}
