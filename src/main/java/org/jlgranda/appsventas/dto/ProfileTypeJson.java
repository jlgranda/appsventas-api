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

/**
 *
 * @author wduck
 */
public class ProfileTypeJson implements Serializable{    
    
    private String tpefNombre;      

    public String getTpefNombre() {
        return tpefNombre;
    }

    public void setTpefNombre(String tpefNombre) {
        this.tpefNombre = tpefNombre;
    }

    @Override
    public String toString() {
        return "ProfileTypeJson{" + "tpefNombre=" + tpefNombre + '}';
    }
    
}
