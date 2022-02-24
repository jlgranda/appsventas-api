package org.jlgranda.appsventas.services;

import org.jlgranda.appsventas.domain.BaseObject;
import org.jlgranda.appsventas.dto.BaseObjectData;
import org.jlgranda.appsventas.dto.UserData;

import org.springframework.security.core.GrantedAuthority;


public class AuthorizationService {
    
    /**
     * Verificación de permiso de lectura sobre objeto 
     * @param user el usuario a veririfacr
     * @param data el objeto de datos a verificar
     * @return 
     */
    public static boolean canRead(UserData user, BaseObjectData data) {
//        if (user == null) return true;
//        return user.getUsername().equals(data.getUsuarioCreacion());
        return true; //Todos pueden leer
    }
    
    public static boolean canRead(UserData user, BaseObject obj) {
//        if (user == null) return true;
//        return user.getUsername().equals(data.getUsuarioCreacion());
        return true; //Todos pueden leer
    }
    
          
    public static boolean canWrite(UserData user, BaseObject obj){
        return true;
//        if (user == null) return true;
//        return obj.getUsuarioCreacion().equalsIgnoreCase(user.getUsername());
    }
    
    public static boolean canWrite(UserData user,  BaseObjectData data){
        return true; /*hasRole(user, "ROL_DISENADOR_DE_FORMULARIO") 
                || hasRole(user, "ROL_ADMINISTRADOR_DE_DATOS")
                || hasRole(user, "ROL_ADMINISTRADOR_DE_DATOS") 
                || hasRole(user, "ROL_SUPERVISOR");*/
//        if (user == null) return true;
//        return data.getUsuarioCreacion().equalsIgnoreCase(user.getUsername());
    }
    
    public static boolean hasRole(UserData user, String rol) {
        GrantedAuthority grantedAuthority = user.getAuthorities().stream()
                .filter(authority -> rol.equals(authority.getAuthority()))
                .findAny()
                .orElse(null);
        return grantedAuthority != null;
    }
}
