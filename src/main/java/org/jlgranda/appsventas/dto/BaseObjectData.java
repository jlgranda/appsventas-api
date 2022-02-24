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
import java.util.Date;
import lombok.Data;
import org.jlgranda.appsventas.domain.CodeType;

/**
 *
 * @author jlgranda
 */
@Data
public class BaseObjectData implements Serializable {

    protected String uuid;
    protected String code;
    protected CodeType codeType;
    protected String name;
    protected String description;
    protected int version = 0;
    protected Date lastUpdate;
    protected Date createdOn;
    protected Date activationTime;
    protected Date expirationTime;
    protected Integer priority;
    protected Boolean active;
    protected String status;

    protected Short orden;
    
}
