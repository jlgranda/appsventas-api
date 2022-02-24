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
package org.jlgranda.appsventas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jlgranda
 */
public class CommandToolsUtil implements Serializable {

    private static CommandToolsUtil instance;

    private CommandToolsUtil() {
    }

    public static synchronized CommandToolsUtil getInstance() {
        if (instance == null) {
            instance = new CommandToolsUtil();
        }
        return instance;
    }
    
    
    
    /**
     * Ejecuta el comando <tt>phantomjs</tt> con los parámetros del proyecto.
     * @param binFolder
     * @param url
     * @param output
     * @return true si la ejecución no tuvó problemas, false en caso contrario.
     */
    public boolean ejecutarPhantomJS(String binFolder, String url, String output) {
        //Ejecutar comando python mag_offline_converter.py PROYECTO-001-20190607 PROYECTO-001 

        StringBuilder run = new StringBuilder(binFolder + "/run_phantomjs.sh");
        run.append(" ")
                .append("-u ")
                .append(url)
                .append(" ")
                .append("-o ")
                .append(output);

        try {
            String s = "";
            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            System.out.println("call: " + run.toString() + "\n");

            Process p = Runtime.getRuntime().exec("/bin/bash " + run.toString());

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, "Here is the standard output of the command: \n");
            while ((s = stdInput.readLine()) != null) {
                Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, s);
            }

            // read any errors from the attempted command
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.SEVERE, "Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.SEVERE, "exception happened - here''s what I know: {0}", e);
            return false;
        }

        return true;
    }
    
    
    /**
     * Ejecuta el comando <tt>command</tt> y recibir salida en <tt>output</tt>
     * @param command
     * @param output
     * @return true si la ejecución no tuvó problemas, false en caso contrario.
     */
    public boolean ejecutar(String command, String output) {
        //Ejecutar comando python mag_offline_converter.py PROYECTO-001-20190607 PROYECTO-001 

        try {
            String s = "";
            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, "call: {0}\n", command);

            Process p = Runtime.getRuntime().exec("" + command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            //Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, "Here is the standard output of the command: \n")
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, "Here is the standard output of the command: \n");
            while ((s = stdInput.readLine()) != null) {
                Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, s);
            }

            // read any errors from the attempted command
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, "Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.SEVERE, s);
            }
        } catch (IOException e) {
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.SEVERE, "exception happened - here''s what I know: {0}", e);
            return false;
        }

        return true;
    }
   
    public boolean ejecutar(String command, String output, int timeout) throws InterruptedException{
        try {
            String s = "";
            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            System.out.println("call: " + command + "\n");

            Process p = Runtime.getRuntime().exec("" + command);
            
            p.waitFor(timeout, TimeUnit.SECONDS);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, "Here is the standard output of the command: \n");
            while ((s = stdInput.readLine()) != null) {
                Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.INFO, s);
            }

            // read any errors from the attempted command
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.SEVERE, "Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            Logger.getLogger(CommandToolsUtil.class.getName()).log(Level.SEVERE, "exception happened - here''s what I know: {0}", e);
            return false;
        }

        return true;
    }
    
    
}
