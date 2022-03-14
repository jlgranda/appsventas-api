/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlgranda.util;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author jlgranda
 */
public class AESUtil implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8814862516698789539L;

    private static AESUtil instance;

    private AESUtil() {
    }

    public static synchronized AESUtil getInstance() {
        if (instance == null) {
            instance = new AESUtil();
        }
        return instance;
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }
    public static SecretKey getKeyFromPassword(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray());
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    
    public static IvParameterSpec generateIvFrom(String from) {
        byte[] iv = from.getBytes();
        //new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String algorithm, String input, SecretKey key,
            IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key,
            IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }
    public static String decrypt(String algorithm, String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }
    
    public static String decrypt(String secret, String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException {

        byte[] cipherData = Base64.getDecoder().decode(cipherText);
        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
        SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptedData = aesCBC.doFinal(encrypted);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
    
    /**
     * Generates a key and an initialization vector (IV) with the given salt and
     * password.
     * <p>
     * This method is equivalent to OpenSSL's EVP_BytesToKey function (see
     * https://github.com/openssl/openssl/blob/master/crypto/evp/evp_key.c). By
     * default, OpenSSL uses a single iteration, MD5 as the algorithm and UTF-8
     * encoded password data.
     * </p>
     *
     * @param keyLength the length of the generated key (in bytes)
     * @param ivLength the length of the generated IV (in bytes)
     * @param iterations the number of digestion rounds
     * @param salt the salt data (8 bytes of data or <code>null</code>)
     * @param password the password data (optional)
     * @param md the message digest algorithm to use
     * @return an two-element array with the generated key and IV
     */
    public static byte[][] GenerateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;

        try {
            md.reset();

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0) {
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                }
                md.update(password);
                if (salt != null) {
                    md.update(salt, 0, 8);
                }
                md.digest(generatedData, generatedLength, digestLength);

                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }

                generatedLength += digestLength;
            }

            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0) {
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);
            }

            return result;

        } catch (DigestException e) {
            throw new RuntimeException(e);

        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte) 0);
        }
    }
}
