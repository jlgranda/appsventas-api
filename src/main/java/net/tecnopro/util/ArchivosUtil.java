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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @based https://stackoverflow.com/questions/24890675/file-upload-using-rest-api-in-java
 * @author jlgranda
 */
public final class ArchivosUtil {

    private ArchivosUtil() {
    }
    
    
    /**
     * Escribe el flujo de carga de archivo en el disco
     * @param uploadedInputStream
     * @param uploadedFileLocation 
     * @return true si se graba el archivo sin problemas, falso en caso contrario
     */
    public static boolean escribirEnArchivo(InputStream uploadedInputStream,
            String uploadedFileLocation) {
        boolean uploadedAndWrited = false;
        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            uploadedAndWrited = true;
        } catch (IOException e) {
            uploadedAndWrited = false;
        }
        
        return uploadedAndWrited = true;
    }
    
    // Utility method
    public static void escribirEnArchivo(byte[] content, String filename) throws IOException {
        File file = new File(filename);
 
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
    }
    
}
