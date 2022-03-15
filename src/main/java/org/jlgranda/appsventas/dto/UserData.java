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
package org.jlgranda.appsventas.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.Data;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.dto.app.OrganizationData;
import org.springframework.security.core.GrantedAuthority;

@Data
public class UserData {

    protected Long id;
    protected String uuid;
    protected String email;
    protected String username;
    protected String nombre;
    protected String bio;
    protected String image;
    protected Date expire;
    protected String token; // Algunos servicios internos precisan el token
    protected Boolean temporal;

    //Datos de facturación
    protected String ruc;
    protected String initials;
    protected String direccion;
    
    //Estado para ejecutar facturación electrónica
    protected Boolean tieneCertificadoDigital; 
    
    //Datos de organización
    OrganizationData organization;

    protected Collection<? extends GrantedAuthority> authorities;

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        this.getAuthorities().forEach(a -> {
            roles.add(a.getAuthority());
        });

        return roles;
    }

    public boolean isExpired() {
        return this.getExpire() == null ? false : Dates.isDateInPast(this.getExpire());
    }

}
