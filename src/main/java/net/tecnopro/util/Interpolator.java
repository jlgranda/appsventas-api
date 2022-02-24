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

/**
 *
 * @author jlgranda
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc

/**
 * The Class Interpolator.
 */
public final class Interpolator { // NOPMD

    
    /** The Constant templateRegex. */
    private static final String templateRegex = "\\{(\\d+)\\}";
    
    /** The Constant templatePattern. */
    private static final Pattern templatePattern = Pattern.compile(templateRegex);

    
    private Interpolator() {
    }
    
    /**
     * Populate a template with the corresponding parameters.
     *
     * @param template the template
     * @param params the params
     * @return the string
     */
    public static String interpolate(final String template, final Object... params) {
        StringBuffer result = new StringBuffer();
        if (template != null && params != null) {
            Matcher matcher = templatePattern.matcher(template);
            while (matcher.find()) {
                int index = Integer.valueOf(matcher.group(1));
                Object value = matcher.group();

                if (params.length > index) {
                    if (params[index] != null) {
                        value = params[index];
                    }
                }
                matcher.appendReplacement(result, value.toString());
            }
            matcher.appendTail(result);
        } else if (template != null) {
            result = new StringBuffer(template);
        }
        return result.toString();
    }
}
