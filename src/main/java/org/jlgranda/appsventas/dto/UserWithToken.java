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
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserWithToken extends UserData {

    private List<String> roles = new ArrayList<>();

    public UserWithToken() {
        super();
    }

    public UserWithToken(UserData userData, String token, List<String> roles) {
        this.id = userData.getId();
        this.uuid = userData.getUuid();
        this.email = userData.getEmail();
        this.username = userData.getUsername();
        this.nombre = userData.getNombre();
        this.bio = userData.getBio();
        this.image = userData.getImage();
        this.token = token;
        this.expire = userData.getExpire();

        this.code = userData.getCode();
        this.mobileNumber = userData.getMobileNumber();
        this.firstname = userData.getFirstname();
        this.surname = userData.getSurname();

        this.ruc = userData.getRuc();
        this.initials = userData.getInitials();
        this.direccion = userData.getDireccion();

        this.tieneCertificadoDigital = userData.getTieneCertificadoDigital();

        this.organization = userData.getOrganization();

        this.roles = roles;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
