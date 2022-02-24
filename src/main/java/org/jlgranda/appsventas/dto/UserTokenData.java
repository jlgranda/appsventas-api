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

import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.tecnopro.util.Dates;

/**
 *
 * @author jlgranda
 */
public class UserTokenData {
    
    String sub;
    List<String> scopes;
    String iss;
    Long iat;
    Long exp;
    String jti;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
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
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.sub);
        hash = 89 * hash + Objects.hashCode(this.scopes);
        hash = 89 * hash + Objects.hashCode(this.iss);
        hash = 89 * hash + Objects.hashCode(this.iat);
        hash = 89 * hash + Objects.hashCode(this.exp);
        hash = 89 * hash + Objects.hashCode(this.jti);
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
        final UserTokenData other = (UserTokenData) obj;
        if (!Objects.equals(this.sub, other.sub)) {
            return false;
        }
        if (!Objects.equals(this.iss, other.iss)) {
            return false;
        }
        if (!Objects.equals(this.iat, other.iat)) {
            return false;
        }
        if (!Objects.equals(this.scopes, other.scopes)) {
            return false;
        }
        if (!Objects.equals(this.exp, other.exp)) {
            return false;
        }
        if (!Objects.equals(this.jti, other.jti)) {
            return false;
        }
        return true;
    }
    
    
    public boolean isExpired(){
        Date expire = Dates.now();
        expire.setTime(this.getExp());
        return  this.getExp() == null ? false : Dates.isDateInPast( expire );
    }
}
