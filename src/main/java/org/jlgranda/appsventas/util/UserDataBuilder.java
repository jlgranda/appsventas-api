/*
 * Licensed under the TECNOPRO License, Version 1.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.tecnopro.net/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jlgranda.appsventas.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.dto.UserExternalMapperUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlgranda.appsventas.dto.UserTokenData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Constructor de instancias <tt>UserData</tt> desde el Token de Auth
 * @author jlgranda
 */
public class UserDataBuilder {

    public static UserData append(String token) {
        
        try {
            int p1 = token.indexOf(".");
            int p2 = token.lastIndexOf(".");
            String dataEnToken = token.substring(p1 + 1, p2);

//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
//            System.out.println("" + new String(java.util.Base64.getDecoder().decode(dataEnToken)));
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
            UserTokenData magjson = UserExternalMapperUtil.mapper(new String(java.util.Base64.getDecoder().decode(dataEnToken)));
            UserData userData = new UserData();
            userData.setId(magjson.getIat());

            //userData.setPersonId(magjson.getUsuarios().get(0).getUsuId()); //Establecer a usu_id
            userData.setUsername(magjson.getSub());
            userData.setBio(magjson.getSub() + "[" + magjson.getIat() + "]");
            userData.setToken(token);

            userData.setExpire(new Date(magjson.getExp() * Constantes.MIL));
            
            //List<String> permisosAsignados = new ArrayList<String>();
            List<GrantedAuthority> authorities = new ArrayList<>();
            magjson.getScopes().forEach(rol -> {
                authorities.add(new SimpleGrantedAuthority(rol));
            });
            userData.setAuthorities(authorities);
            return userData;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(UserDataBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
