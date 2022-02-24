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
 * @author jlgranda
 */
public class ErrorData implements Serializable {
    
    ResultData errors;

    public ErrorData() {
    }

    public ErrorData(ResultData errors) {
        this.errors = errors;
    }

    public ResultData getErrors() {
        return errors;
    }

    public void setErrors(ResultData errors) {
        this.errors = errors;
    }
}
