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

// TODO: Auto-generated Javadoc

/**
 * The Class StringValidations.
 */
public final class StringValidations {

    /** The Constant EMAIL_REGEX. */
    public static final String EMAIL_REGEX = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e"
            + "-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]"
            + "*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4]"
            + "[0-9]|[01]?[0-9][0-9]?|[A-Za-z0-9-]*[A-Za-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b"
            + "\\x0c\\x0e-\\x7f])+)\\])";
    public static final String EMAIL_REGEX_2 = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    
    //https://www.freeformatter.com/java-regex-tester.html#ad-output
    public static final String EMAIL_REGEX_3 = "[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?";
    
    //dinardap
    public static final String EMAIL_REGEX_4 = "([A-Za-z0-9!#-'*+\\-/=?^_`{-~\\xA0-\\x{10FFFF}]+ (?:\\.[A-Za-z0-9!#-'*+\\-/=?^_`{-~\\xA0-\\x{10FFFF}])| \"(?:[ !#-\\[\\]-~\\xA0-\\x{10FFFF}]|\\\\[ -~])*\")@((?:[A-Za-z0-9](?:[A-Za-z0-9\\-]*[A-Za-z0-9])?| [\\x00-\\x{10FFFF}]*[\\x80-\\x{10FFFF}]+[\\x00-\\x{10FFFF}]*) (?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9\\-]* [A-Za-z0-9])?|[\\x00-\\x{10FFFF}]* [\\x80-\\x{10FFFF}]+[\\x00-\\x{10FFFF}]*))*)";

    private StringValidations() {
    }

    
    /**
     * Checks if is alphabetic.
     *
     * @param text the text
     * @return true, if is alphabetic
     */
    public static boolean isAlphabetic(final String text) {
        if (text != null) {
            return text.matches("^[a-zA-Z]*$");
        }

        return false;
    }

    /**
     * Checks if is alphanumeric.
     *
     * @param textWithDigits the text with digits
     * @return true, if is alphanumeric
     */
    public static boolean isAlphanumeric(final String textWithDigits) {
        if (textWithDigits != null) {
            return textWithDigits.matches("^[a-zA-Z\\d]*$");
        }

        return false;
    }

    /**
     * Checks if is alphanumeric dash.
     *
     * @param text the text
     * @return true, if is alphanumeric dash
     */
    public static boolean isAlphanumericDash(String text) {
        return text.matches("^[a-zA-Z\\d-]*$");
    }

    /**
     * Checks if is alphanumeric space underscore.
     *
     * @param textWithDigits the text with digits
     * @return true, if is alphanumeric space underscore
     */
    public static boolean isAlphanumericSpaceUnderscore(final String textWithDigits) {
        if (textWithDigits != null) {
            return textWithDigits.matches("^[a-zA-Z\\d_ ]*$");
        }

        return false;
    }

    /**
     * Checks if is empty string.
     *
     * @param text the text
     * @return true, if is empty string
     */
    public static boolean isEmptyString(final String text) {
        if (text != null) {
            return text.matches("");
        }

        return false;
    }

    /**
     * Checks if is punctuated text UTF 8.
     *
     * @param text the text
     * @return true, if is punctuated text UTF 8
     */
    public static boolean isPunctuatedTextUTF8(final String text) {
        if (text != null) {
            return text.matches("^[\\p{L}+a-zA-Z/\\d\\s._?!,;':\"~`$%&()+=\\[\\]-]*$");
        }

        return false;
    }

    /**
     * Checks if is punctuated text.
     *
     * @param text the text
     * @return true, if is punctuated text
     */
    public static boolean isPunctuatedText(final String text) {
        if (text != null) {
            return text.matches("^[a-zA-Z/\\d\\s._?!,;':\"~`$%&()+=\\[\\]-]*$");
        }

        return false;
    }

    /**
     * Checks if is punctuated text without space.
     *
     * @param text the text
     * @return true, if is punctuated text without space
     */
    public static boolean isPunctuatedTextWithoutSpace(final String text) {
        if (text != null) {
            return text.matches("^[a-zA-Z/\\d._?!,;':\"~`$%&()+=\\[\\]-]*$");
        }

        return false;
    }

    /**
     * Checks if is decimal.
     *
     * @param number the number
     * @return true, if is decimal
     */
    public static boolean isDecimal(final String number) {
        if (number != null) {
            return number.matches("([0-9]+(\\.[0-9]+)?)|([0-9]*\\.[0-9]+)");
        }
        return false;
    }

    /**
     * Checks if is whole number.
     *
     * @param number the number
     * @return true, if is whole number
     */
    public static boolean isWholeNumber(final String number) {
        if (number != null) {
            return number.matches("[0-9]+");
        }
        return false;
    }

    /**
     * Checks if is digit.
     *
     * @param digit the digit
     * @return true, if is digit
     */
    public static boolean isDigit(final String digit) {
        if (digit != null) {
            return digit.matches("^\\d$");
        }

        return false;
    }

    /**
     * Checks if is email address.
     *
     * @param email the email
     * @return true, if is email address
     */
    public static boolean isEmailAddress(final String email) {
        if (email != null) {
            return email.matches("^" + EMAIL_REGEX + "$");
        }

        return false;
    }
    /**
     * Checks if is email address.
     *
     * @param email the email
     * @return true, if is email address
     */
    public static boolean isEmailAddress2(final String email) {
        if (email != null) {
            return email.matches("^" + EMAIL_REGEX_2 + "$");
        }

        return false;
    }
    /**
     * Checks if is email address.
     *
     * @param email the email
     * @return true, if is email address
     */
    public static boolean isEmailAddress3(final String email) {
        if (email != null) {
            return email.matches("^" + EMAIL_REGEX_3 + "$");
        }

        return false;
    }
    /**
     * Checks if is email address.
     *
     * @param email the email
     * @return true, if is email address
     */
    public static boolean isEmailAddress4(final String email) {
        if (email != null) {
            return email.matches("^" + EMAIL_REGEX_4 + "$");
        }

        return false;
    }

    /**
     * Checks if is state.
     *
     * @param state the state
     * @return true, if is state
     */
    public static boolean isState(final String state) {
        if (state != null) {
            return Strings.toUpperCase(state).matches(
                    "^(AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|FA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|"
                    + "MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|"
                    + "UT|VT|VI|VA|WA|WV|WI|WY)$");
        }

        return false;
    }

    /**
     * Checks if is zip code.
     *
     * @param zipCode the zip code
     * @return true, if is zip code
     */
    public static boolean isZipCode(final String zipCode) {
        if (zipCode != null) {
            return zipCode.matches("\\d{5}((-| +)\\d{4})?$");
        }

        return false;
    }
    /**
     * Checks if is uuid code.
     *
     * @param uuid the uuid code
     * @return true, if is uuid code
     */
    public static boolean isUUID(final String uuid) {
        if (uuid != null) {
            return uuid.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        }

        return false;
    }

    /**
     * Checks if is phone number.
     *
     * @param phoneNumber the phone number
     * @return true, if is phone number
     */
    public static boolean isPhoneNumber(final String phoneNumber) {
        if (phoneNumber != null) {
            return phoneNumber.matches("^(?:\\([2-9]\\d{2}\\)\\ ?|[2-9]\\d{2}(?:\\-?|\\ ?))[2-9]\\d{2}[- ]?\\d{4}$");
        }

        return false;
    }

    /**
     * Checks if is password.
     *
     * @param password the password
     * @return true, if is password
     */
    public static boolean isPassword(final String password) {
        return isPassword(password, 8);
    }
    
    /**
     * Checks if is password.
     *
     * @param password the password
     * @param length the length
     * @return true, if is password
     */
    public static boolean isPassword(final String password, final int length) {
        if (password != null) {
            return password.matches("^[a-zA-Z0-9!@#$%^&*\\s\\(\\)_\\+=-]{" + length + ",}$");
        }

        return false;
    }

    /**
     * Checks if is strict password.
     *
     * @param password the password
     * @return true, if is strict password
     */
    public static boolean isStrictPassword(final String password) {
        if (password != null) {
            return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z\\d!@#$%^&*\\s\\(\\)_\\+=-]{8,}$");
        }

        return false;
    }

    /**
     * Checks for data.
     *
     * @param value the value
     * @return true, if successful
     */
    public static boolean hasData(final String value) {
        return value != null && value.length() > 0 && value.matches(".*\\S.*");
    }

    /**
     * Length.
     *
     * @param length the length
     * @param value the value
     * @return true, if successful
     */
    public static boolean length(final int length, final String value) {
        return value != null && value.length() == length;
    }

    /**
     * Min length.
     *
     * @param length the length
     * @param value the value
     * @return true, if successful
     */
    public static boolean minLength(final int length, final String value) {
        return value != null && value.length() >= length;
    }

    /**
     * Max length.
     *
     * @param length the length
     * @param value the value
     * @return true, if successful
     */
    public static boolean maxLength(final int length, final String value) {
        return value != null && value.length() <= length;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String args[]) {
//        String text = "José Luis";
//        if (!StringValidations.isPunctuatedTextUTF8(text)) {
//
//            System.out.print("Este texto " + text + " no anda bien, evite usar caracteres extraños...");
//
//        } else {
//                        System.out.print("Este texto " + text + " esta bién ;)...");
//        }
//        
//        String email = "a@b.123";
//        System.out.println(">>> validar : " + email);
//        System.out.println(">>> validador 1: " + StringValidations.isEmailAddress(email));
//        System.out.println(">>> validador 2: " + StringValidations.isEmailAddress2(email));
//        System.out.println(">>> validadro 3: " + StringValidations.isEmailAddress3(email));
//        System.out.println(">>> validadro dinardap: " + StringValidations.isEmailAddress4(email));
//        
//        email = "a@b.s";
//        System.out.println(">>> validar : " + email);
//        System.out.println(">>> validador 1: " + StringValidations.isEmailAddress(email));
//        System.out.println(">>> validador 2: " + StringValidations.isEmailAddress2(email));
//        System.out.println(">>> validadro 3: " + StringValidations.isEmailAddress3(email));
//        System.out.println(">>> validadro dinardap: " + StringValidations.isEmailAddress4(email));
//        
//        email = "a@ssssssssssssssssssssssssb.s";
//        System.out.println(">>> validar : " + email);
//        System.out.println(">>> validador 1: " + StringValidations.isEmailAddress(email));
//        System.out.println(">>> validador 2: " + StringValidations.isEmailAddress2(email));
//        System.out.println(">>> validadro 3: " + StringValidations.isEmailAddress3(email));
//        System.out.println(">>> validadro dinardap: " + StringValidations.isEmailAddress4(email));
//        
//        email = "jlgranda81@gmail.com";
//        System.out.println(">>> validar : " + email);
//        System.out.println(">>> validador 1: " + StringValidations.isEmailAddress(email));
//        System.out.println(">>> validador 2: " + StringValidations.isEmailAddress2(email));
//        System.out.println(">>> validadro 3: " + StringValidations.isEmailAddress3(email));
//        System.out.println(">>> validadro dinardap: " + StringValidations.isEmailAddress4(email));
//        
        System.out.println(">>> UUID : " + StringValidations.isUUID("6a151cea-2a82-4b32-98b2-1659b82e5301"));
        System.out.println(">>> UUID : " + StringValidations.isUUID("6a151cea-2a82-4b32-98b2-1659b82e5301/geojson"));
        System.out.println(">>> UUID : " + StringValidations.isUUID("PROY-001-PHOTO"));
        
        
    }
}
