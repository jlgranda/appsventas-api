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

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.dto.app.OrganizationData;

/**
 *
 * @author jlgranda
 */
@Getter
@Setter
@ToString
public class UserModelData extends BaseObjectData {

    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;
    private Long personId;
    protected Boolean temporal;
    private String urlVista;
    private Boolean contraseniaModificada = false;
    private String codigoNombre;

    //Data User
    private String firstname;
    private String surname;
    private String mobileNumber;

    //Datos de facturación
    protected String ruc;
    protected String initials;
    protected String direccion;

    //Estado para ejecutar facturación electrónica
    protected Boolean tieneCertificadoDigital;

    //Datos de organización
    OrganizationData organization;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.email);
        hash = 41 * hash + Objects.hashCode(this.username);
        hash = 41 * hash + Objects.hashCode(this.personId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserModelData other = (UserModelData) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return Objects.equals(this.personId, other.personId);
    }

}
