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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jlgranda
 */
public final class Lists {

    private Lists() {
    }
    
    /**
     * Convierte una iterable en una cadena separada por comas, con los
     * elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param iter
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @return una cadena separada por comas, con los elementos de la lista.
     */
    public static String toString(Iterator<?> iter) {
       
        List list = new ArrayList();
        Object obj = null;
        while(iter.hasNext()) {
            obj = iter.next();
            list.add( obj instanceof Long ? "" + obj : obj);
        }
        return Lists.toString(list, ',', (char) Character.SPACE_SEPARATOR);
    }
    
    /**
     * Convierte una lista de objetos en una cadena separada por comas, con los
     * elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param iter
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @return una cadena separada por comas, con los elementos de la lista.
     */
    public static String toString(Iterable iter) {
        List list = new ArrayList();
        for (Object obj : iter){
            list.add(obj);
        }
        return Lists.toString(list, ',', (char) Character.SPACE_SEPARATOR);
    }
    
    /**
     * Convierte una lista de objetos en una cadena separada por comas, con los
     * elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param list
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @return una cadena separada por comas, con los elementos de la lista.
     */
    public static String toString(List list) {
        return Lists.toString(list, ',', (char) Character.SPACE_SEPARATOR);
    }

    /**
     * Convierte una lista de objetos en una cadena separada por comas, con los
     * elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param list
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @return una cadena separada por comas, con los elementos de la lista.
     */
    public static String toString(String[] list) {
        if (list == null) {
            return "-";
        }
        StringBuilder buffer = new StringBuilder();
        int length = list.length;
        for (int i = 0; i < length; i++) {
            buffer.append(list[i]);
            if (i > 0) {
                buffer.append(", ");
            }
        }
        return buffer.toString();
    }

    /**
     * Convierte una lista de objetos en una cadena separada por separador, con
     * los elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param list
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @param separador
     * @return una cadena separada por separador, con los elementos de la lista.
     */
    public static String toString(List list, char separador) {
        return Lists.toString(list, separador, '\0');
    }
   
    /**
     * Convierte una lista de objetos en una cadena separada por separador, con
     * los elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param list
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @param separador
     * @param delimitador
     * @return una cadena separada por separador, con los elementos de la lista.
     */
    public static String toString(List<String> list, char separador, char delimitador) {
        if (list.isEmpty()) {
            return "-";
        }
        StringBuilder buffer = new StringBuilder();
        int index = 0;
        String str;
        for (String value : list ) {
            str = value == null ? "" : value;
            str = str.replaceAll("\'", "\\\'");
            buffer.append(delimitador)
            .append(str)
            .append(delimitador);
           
            if (index < (list.size() - 1)) {
                buffer.append(separador);
                buffer.append(' ');
            }
            index++;
        }
        return buffer.toString();
    }

    /**
     * Convierte una lista de objetos en una cadena separada por separador, con
     * los elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param list
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @param separador
     * @return una cadena separada por separador, con los elementos de la lista.
     */
    public static String toString(List<String> list, String separador) {
        if (list.isEmpty()) {
            return "-";
        }
        StringBuilder buffer = new StringBuilder();
        int index = 1;
        int ultimo = list.size() ;
        for (String value : list ) {
            buffer.append(value);
            if (index > 0 && index < ultimo) {
                buffer.append(separador);
            }
            index++;
        }
        return buffer.toString();
    }
    
    /**
     * Convierte una lista de objetos en una cadena separada por separador, con
     * los elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param list
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @param separador
     * @return una cadena separada por separador, con los elementos de la lista.
     */
    public static String toStringFromObjectList(List<Object> list, String separador) {
        List<String> strs = new ArrayList<>();
        list.forEach((obj) -> {
            strs.add(obj != null ? obj.toString() : "null");
        });
        return Lists.toString(strs, separador);
    }
    /**
     * Convierte una lista de objetos en una cadena separada por separador, con
     * los elementos de la lista. Los objetos de la lista deben implementar el
     * mÃ©todo toString
     *
     * @param list
     *            la lista sobre la cual se construye la cadena de elementos
     *            seperadas por comas
     * @param separador
     * @return una cadena separada por separador, con los elementos de la lista.
     */
    public static String toStringFromLongList(List<Long> list, String separador) {
        List<String> strs = new ArrayList<>();
        list.forEach((obj) -> {
            strs.add(obj != null ? obj.toString() : "");
        });
        return Lists.toString(strs, separador);
    }

    public static List<String> toList(String s) {
        if (s == null) {
            return new ArrayList<>();
        }
        if (s.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(s.split(","));
    }

    public static String findDefaultValue(String values) {
        return findDefaultValue(values, "*");
    }

    public static String findDefaultValue(String values, String mnemotecnico) {
        List<String> options = Lists.toList(values);
        String defaultValue = null;
        for (String s : options) {
            if (s.contains(mnemotecnico)) {
                defaultValue = s.substring(0, s.length() - 1);
            }
        }
        return defaultValue;
    }
    
     public static <T> T random(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for(T t: coll){
            num = num - 1;
            if (num < 0) {
                return t;
            }
        }
        throw new AssertionError();
    }
     
    public static void main(String args[]){
        //String scape = "ORD-CHICAGO-O'HARE INTERNATIONAL AIRPORT".replace("\'", "\\\'");
        String scape = "&quot;test&quot;";
        List<String> tests = new ArrayList<>();
        tests.add(scape);
//        tests.add(scape);
//        tests.add(scape);
//        tests.add(scape);
//        tests.add(scape);
//        tests.add(scape);
       
       
       // System.out.print(Lists.toString(tests, ',', '\''));
        System.out.print("concat(" + Lists.toString(tests, ", ' ',") + ")");
    }
}