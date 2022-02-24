package org.jlgranda.appsventas.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class NaiveEncryptService implements EncryptService {

    @Override
    public String encrypt(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(password.getBytes(), 0, password.length());
            String hashedPass = new BigInteger(1, messageDigest.digest()).toString(16);
            if (hashedPass.length() < 32) {
                hashedPass = "0" + hashedPass;
            }
            return hashedPass;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NaiveEncryptService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "NO_PASSWORD";
    }

    @Override
    public boolean check(String checkPassword, String realPassword) {
        return checkPassword.equals(realPassword);
    }

    public static void main(String args[]) {
        NaiveEncryptService n = new NaiveEncryptService();
        System.out.println(">>> " + n.encrypt("jlgr4nd4M$G"));
    }

}
