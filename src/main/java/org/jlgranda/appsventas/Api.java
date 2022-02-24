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
package org.jlgranda.appsventas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlgranda.appsventas.dto.ResultData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author jlgranda
 */
public class Api {
    
    public static Map<String, Object> response(Object object) {
        return new HashMap<String, Object>() {
            private static final long serialVersionUID = 5636572627689425575L;

            {
                put(object.getClass().getSimpleName().toLowerCase(), object);
            }
        };
    }
    
    public static Map<String, Object> response(String root, Object object) {
        return new HashMap<String, Object>() {
            private static final long serialVersionUID = 5636572627689425575L;
        {
            put(root, object);
        }};
    }
    
    public static ResponseEntity responseError(String message, Object object) {
        ResultData resultData = new ResultData(false, message, object);
        resultData.setMessage(message);
        if (object instanceof Exception){
            resultData.setMessage(((Exception) object).getLocalizedMessage());
            resultData.setType("¡Algo salió mal!.");
        }
        resultData.setSuccess(false);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, Object>() {
            {
                put("errors", resultData);
            }
        });

        return responseEntity;
    }
    
    public static ResponseEntity responseWarning(String message, Object object) {
        ResultData resultData = new ResultData(false, message, object);
        resultData.setMessage(message);
        if (object instanceof Exception){
            resultData.setMessage(((Exception) object).getLocalizedMessage());
            resultData.setType("Warning");
        }
        resultData.setSuccess(false);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, Object>() {
            {
                put("errors", resultData);
            }
        });

        return responseEntity;
    }
    
    public static ResponseEntity responseError(String type, String message, Object object) {
        ResultData resultData = new ResultData(false, message, object);
        resultData.setMessage(message);
        resultData.setSuccess(false);
        resultData.setType(type);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, Object>() {
            {
                put("errors", resultData);
            }
        });

        return responseEntity;
    }
    
    public static void imprimirGetLogAuditoria(String endpoint, Long usuid){
        Api.imprimirLogAuditoria(endpoint, "get", "" + usuid, null);
    }
    public static void imprimirGetLogAuditoria(String endpoint, String usuid){
        Api.imprimirLogAuditoria(endpoint, "get", usuid, null);
    }
    public static void imprimirPostLogAuditoria(String endpoint, String usuid){
        Api.imprimirLogAuditoria(endpoint, "post", usuid, null);
    }
    public static void imprimirPostLogAuditoria(String endpoint, Long usuid){
        Api.imprimirLogAuditoria(endpoint, "post", "" + usuid, null);
    }
    public static void imprimirUpdateLogAuditoria(String endpoint, String usuid, Object data){
        Api.imprimirLogAuditoria(endpoint, "update", usuid, data);
    }
    
    public static void imprimirUpdateLogAuditoria(String endpoint, Long usuid, Object data){
        Api.imprimirLogAuditoria(endpoint, "update", "" + usuid, data);
    }
    public static void imprimirDeleteLogAuditoria(String endpoint, String usuid, Object data){
        Api.imprimirLogAuditoria(endpoint, "delete", usuid, data);
    }
    public static void imprimirDeleteLogAuditoria(String endpoint, Long usuid, Object data){
        Api.imprimirLogAuditoria(endpoint, "delete", "" + usuid, data);
    }
    
    public static void imprimirLogAuditoria(String endpoint, String metodo, String usuid, Object data){
        Class clazz = Api.class;
        if ("get".equalsIgnoreCase(metodo)){
            //LOGGER.info("prueba/create" + " usuario: " + util.filterUsuId(token));
            Logger.getLogger(clazz.getName()).log(Level.INFO, "{0} usuario: {1}", new Object[]{endpoint, usuid});
        } else if ("post".equalsIgnoreCase(metodo)){
            //LOGGER.info("prueba/create" + " usuario: " + util.filterUsuId(token));
            Logger.getLogger(clazz.getName()).log(Level.INFO, "{0} usuario: {1}", new Object[]{endpoint, usuid});
        } else if ("update".equalsIgnoreCase(metodo)){
            //LOGGER.info("prueba/update: entidad:" + prueba + " usuario: " + usuId);
            Logger.getLogger(clazz.getName()).log(Level.INFO, "{0} entidad: {1} usuario: {2}", new Object[]{endpoint, Api.toJSON(data), usuid});
        } else if ("delete".equalsIgnoreCase(metodo)){
            //LOGGER.info("prueba/delete: id:" + id + " usuario:" + usuId);
            Logger.getLogger(clazz.getName()).log(Level.INFO, "{0} id: {1} usuario: {1}", new Object[]{endpoint, data, usuid});
        }
    }
    /**
     * Utilitario para impresión de logs
     * @param clazz
     * @param mensaje
     * @param data 
     */
    public static void imprimirLog(Class clazz, String mensaje, Object data){
        String metodo = "";
        String endpoint = ""; 
        String usuid = ""; 
        Logger.getLogger(clazz.getName()).log(Level.INFO, "{0}. Datos: {1}", new Object[]{mensaje, Api.toJSON(data)});
        
    }
    
    private static String toJSON(Object data) {
        String jsonString = null;
        ObjectMapper mprObjecto = new ObjectMapper();
        mprObjecto.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            jsonString = mprObjecto.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonString;
    }
    
}
