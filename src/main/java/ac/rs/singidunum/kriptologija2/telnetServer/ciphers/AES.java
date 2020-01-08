package ac.rs.singidunum.kriptologija2.telnetServer.ciphers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static byte[] KEY = null;
    private static byte[] IV = null;

    public static void generateKey() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            IvParameterSpec iv = new IvParameterSpec(secureRandom.generateSeed(16));

            IV = iv.getIV();

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);

            SecretKey secretKey = keyGenerator.generateKey();

            KEY = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] encrypt(String str) {
        try {
            SecretKey secretKey = new SecretKeySpec(KEY, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            return cipher.doFinal(str.getBytes("UTF-8"));
        } catch (NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static byte[] decrypt(byte[] enStr) {
        try {
            SecretKey secretKey = new SecretKeySpec(KEY, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            return cipher.doFinal(enStr);
        } catch (NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static byte[] getIV() {
        return IV;
    }

    public static byte[] getKEY() {
        return KEY;
    }

    public static void setKEY(byte[] key) {
        KEY = key;
    }

    public static void setIV(byte[] iv) {
        IV = iv;
    }

    /*
    public static void main(String[] args) {
        generateKey();
        System.out.println("KEY " + Arrays.toString(KEY));
        System.out.println("PASSWORD BYTES " + Arrays.toString("password".getBytes()));
        System.out.println("ENCRYPTED BYTES " + Arrays.toString(encrypt("password")));
        System.out.println("DECRYPTED BYTES " + Arrays.toString(decrypt(encrypt("password"))));
    }
     */
}