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
package org.jlgranda.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tecnopro.mailing.Message;
import net.tecnopro.mailing.Notification;
import net.tecnopro.util.Dates;
import net.tecnopro.util.Lists;
import net.tecnopro.util.VelocityHelper;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.services.mailing.MessageService;
import org.jlgranda.appsventas.services.mailing.NotificationService;

/**
 *
 * @author jlgranda
 */


public class EmailUtil {
    
    private static EmailUtil instance;

    private EmailUtil() {
    }

    public static synchronized EmailUtil getInstance() {
        if (instance == null) {
            instance = new EmailUtil();
        }
        return instance;
    }
    
    /**
     * Envia un mensaje de correo para el <tt>Subject</tt> destinatario desde la 
     * cuenta <tt>from</tt>, usando los textos plantilla y 
     * los valores <tt>values</tt> para rellenar la plantilla.
     *
     * @param remitente quién envia
     * @param destinatario sujeto que recibe el mensaje
     * @param titulo titulo plantilla
     * @param cuerpoMensaje texto plantilla
     * @param values valores para rellenar texto
     * @param notificationService
     * @param messageService
     * @param claveAcceso
     * @param xml
     * @param pdf
     * @return true si el mensaje se envio sin problemas, false en caso
     * contrario
     */
    public boolean enviarCorreo(UserData remitente, UserData destinatario, 
            String titulo, String cuerpoMensaje, Map<String, Object> values, 
            NotificationService notificationService, MessageService messageService, String claveAcceso,
                        byte[] pdf, byte[] xml) {

        boolean valorRetorno = false;
        String title;
        String body;
        String txt;
        try {
            title = VelocityHelper.getRendererMessage(titulo, values);
            body = VelocityHelper.getRendererMessage(cuerpoMensaje, values);
            txt = VelocityHelper.getRendererMessage(cuerpoMensaje, values);
            Notification notification = notificationService.crearInstancia();
            Message message = messageService.crearInstancia();
            List<String> nombres = new ArrayList<>();
            nombres.add(Constantes.MENSAJE);
            nombres.add(title);
            nombres.add("" + destinatario.getId());
            nombres.add("" + Dates.now().getTime());
            message.setName(Lists.toString(nombres, Constantes.SEPARADOR).toUpperCase());
            message.setSubject(title);
            message.setHtml(body);
            message.setText(txt);
            messageService.guardar(message);

            nombres = new ArrayList<>();
            nombres.add(Constantes.NOTIFICACION);
            nombres.add(title);
            nombres.add("" + destinatario.getId());
            nombres.add("" + Dates.now().getTime());
            notification.setName(Lists.toString(nombres, Constantes.SEPARADOR).toUpperCase());
            notification.setFfrom(remitente.getEmail());
            notification.setTto(destinatario.getEmail());
            notification.setMessageId(message.getId());
            notification.setSriNotificacion(Boolean.TRUE);
            notification.setSriClaveAcceso(claveAcceso);
            notification.setSriPDF(pdf);
            notification.setSriXML(xml);

            notificationService.guardar(notification);
            valorRetorno = true; //todo ok
        } catch (Exception ex) {
            Logger.getLogger(EmailUtil.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return valorRetorno;
    }

}
