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
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

/**
 * @author wduck ResultRest
 */
public class ResultData implements Serializable {

    private static final long serialVersionUID = 1L;

    boolean success;
    String message;
    String type;
    Object payload;

    public ResultData() {
    }
    
    public <T> ResultData(boolean sucess, String message, T payload) {
        this.success = true;
        this.message = message;
        this.type = payload.getClass().getSimpleName();
        this.payload = payload;
    }
    
    public <T> ResultData(String message, T payload) {
        this.success = true;
        this.message = message;
        this.type = payload.getClass().getSimpleName();
        this.payload = payload;
    }

    public <T> ResultData(String message, T payload, Class<?> typeObjectList) {
        this.success = true;
        this.message = message;
        this.type = typeObjectList.getSimpleName();
        this.payload = payload;
    }

    public ResultData(String message, Class<? extends Throwable> type) {
        this.success = false;
        this.type = type.getSimpleName();
        this.message = message;
    }

    ResultData(Set<? extends ConstraintViolation<?>> violations) {
        this.success = false;
        this.type = ConstraintViolation.class.getSimpleName();
        this.message = violations.stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.joining(", "));
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}
