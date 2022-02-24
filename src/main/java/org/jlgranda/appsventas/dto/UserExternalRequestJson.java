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


import java.io.Serializable;
import java.util.List;

/**
 *
 * @author wduck
 */
public class UserExternalRequestJson implements Serializable{
    
    private Long apliId;
    private String user_name;
    private Long exp;
    private String jti;
    private Long per_id;
    private Long usu_id;
    private String client_id;
    
    private List<String> scope;
    private List<RoleJson> roles;
    private List<ResourceJson> recursos;
    private List<String> authorities;
    private List<UserInfoJson> usuarios;

    public Long getApliId() {
        return apliId;
    }

    public void setApliId(Long apliId) {
        this.apliId = apliId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public Long getPer_id() {
        return per_id;
    }

    public void setPer_id(Long per_id) {
        this.per_id = per_id;
    }

    public Long getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(Long usu_id) {
        this.usu_id = usu_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public List<RoleJson> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleJson> roles) {
        this.roles = roles;
    }

    public List<ResourceJson> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<ResourceJson> recursos) {
        this.recursos = recursos;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<UserInfoJson> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UserInfoJson> usuarios) {
        this.usuarios = usuarios;
    }
    
}
