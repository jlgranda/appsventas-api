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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author jlgranda
 */
public class ReadFileUtil {
    
    public static final String TEST_PATH = "/home/opt/mag/test";
    
    private static ReadFileUtil instance;

    private ReadFileUtil() {
    }

    public static synchronized ReadFileUtil getInstance() {
        if (instance == null) {
            instance = new ReadFileUtil();
        }
        return instance;
    }

//    public String read(String nombreArchivo) {
//        StringBuilder output = new StringBuilder();
//        String path = ReadFileUtil.TEST_PATH + File.separator + nombreArchivo;
//        //using class of nio file package 
//        Path filePath = Paths.get(path);
//
//        //converting to UTF 8
//        Charset charset = StandardCharsets.UTF_8;
//
//        //try with resource
//        try (BufferedReader bufferedReader = Files.newBufferedReader(filePath, charset)) {
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                output.append(line).append((char) Character.LINE_SEPARATOR);
//            }
//        } catch (IOException ex) {
//            System.out.format("I/O exception: ", ex);
//        }
//        
//        return output.toString();
//    }
    public String read(String nombreArchivo) {
        StringBuilder output = new StringBuilder();
        String path = ReadFileUtil.TEST_PATH + File.separator + nombreArchivo;
        //using class of nio file package 
        try (FileReader fileReader = new FileReader(path)){
            int i;
            while ((i = fileReader.read()) != -1)
                output.append((char) i);
        } catch (IOException ex) {
            System.out.format("I/O exception, no se encontró el archivo (Establesca la variable TEST_PATH al valor correcto ): ", ex);
        }
        return output.toString();
    }
}
