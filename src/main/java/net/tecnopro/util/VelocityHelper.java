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
package net.tecnopro.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public final class VelocityHelper {

    private VelocityHelper() {
    }

    public static String getRendererMessage(String message, Map values) throws Exception {

        Properties p = new Properties();
//        p.setProperty("file.resource.loader.path", "/tmp");
        p.setProperty("resource.loader.file.path", "/tmp");

        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init(p);
        /*  next, get the Template  */
        String templateFileName = createTemporaryFileTemplate(message);
        Template t = ve.getTemplate(templateFileName);
        /*  create a context and add data */
        VelocityContext context = new VelocityContext(values);
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        String rendererMessage = writer.toString();
        /* show the World */
        //System.out.println("VelocityHelper: Se renderizó el mensaje: " + rendererMessage );
        destroyTemporaryFileTemplate(templateFileName);
        return rendererMessage;

    }

    public static String getRendererMessage(String message, Map values, String templateFileName) throws Exception {

        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "/tmp");

        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();

        ve.init(p);
        /*  next, get the Template  */
        Template t = ve.getTemplate(templateFileName);
        /*  create a context and add data */
        VelocityContext context = new VelocityContext(values);
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        
        return writer.toString();

//        String rendererMessage = writer.toString();
//        /* show the World */
//        //System.out.println("VelocityHelper: Se renderizó el mensaje: " + rendererMessage );
//        return rendererMessage;

    }

    public static String createTemporaryFileTemplate(String message) throws IOException {
        File f = new File("/tmp");
        File tmp = File.createTempFile("email_", ".vm", f);
        if (tmp.canWrite()) {
            try (FileOutputStream stream = new FileOutputStream(tmp)) {
                stream.write(message.getBytes());
            }
        } else {
            return "failure";
        }
        return tmp.getName();
    }

    public static boolean destroyTemporaryFileTemplate(String filename) throws IOException {

        File tmp = new File(System.getProperty("java.io.tmpdir") + File.separator + filename);
        if (tmp.canWrite()) {
            return tmp.delete();
        }
        return false;
    }
    
    public static void main(String[] args) throws Exception {
        
        //Todo leer el archivo plantilla desde un archivo
        String xmlPlantilla = "<step><name>$tablaOrigen</name>....<fecha>$now</fecha>"; 
        Map<String, Object> values = new HashMap<>();
        values.put("now", Dates.toString(Dates.now(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")); //La fecha actual en el formato indicado en la especificación
        values.put("tablaOrigen", "Tabla 1"); 
        values.put("tablaDestino", "Tabla 2"); 
        String xmlSalida = VelocityHelper.getRendererMessage(xmlPlantilla, values);
        System.out.println("XML: " + xmlSalida);
    }

}
