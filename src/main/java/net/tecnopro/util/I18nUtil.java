/*
 * Copyright 2012  SIGCATASTROS TECNOPRO CIA. LTDA.
 *
 * Licensed under the TECNOPRO License, Version 1.0 (the "License");
 * you may not use this file or reproduce complete or any part without express autorization from TECNOPRO CIA LTDA.
 * You may obtain a copy of the License at
 *
 *     http://www.tecnopro.net
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tecnopro.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

// TODO: Auto-generated Javadoc
/**
 * The Class I18nUtil.
 *
 * @author jlgranda
 */
public final class I18nUtil {

    private I18nUtil() {
    }

    /**
     * Gets the messages.
     *
     * @param key the key
     * @return the messages
     */
    public static String getMessages(String key) {
//        FacesContext fc = FacesContext.getCurrentInstance();
        //Locale myLocale = fc.getExternalContext().getRequestLocale();
        Locale spanishLocale = new Locale("es");
        ResourceBundle myResources = ResourceBundle.getBundle("/recursos.mensajes", spanishLocale);

        return myResources.containsKey(key) ? myResources.getString(key) : key;
    }

    /**
     * Gets the format.
     *
     * @param key the key
     * @param args the args
     * @return the format
     */
    public static String getFormat(String key, String... args) {
        MessageFormat mf = new MessageFormat(I18nUtil.getMessages(key));
        return mf.format(args);
    }

    /**
     * Gets the messages.
     *
     * @param key the key
     * @param args the args
     * @return the messages
     */
    public static String getMessages(String key, String... args) {
        return I18nUtil.getFormat(key, args);
    }
}
