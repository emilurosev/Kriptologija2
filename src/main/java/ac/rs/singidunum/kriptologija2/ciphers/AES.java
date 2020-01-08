package ac.rs.singidunum.kriptologija2.ciphers;

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
    
    private static byte[] key = null;
    private static byte[] IV = null;

    public byte[] encrypt(String str) throws NoSuchAlgorithmException {
        try {
            SecureRandom rnd = new SecureRandom();
            IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));
            IV = iv.getIV();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            SecretKey k = generator.generateKey();
            //System.out.println(Arrays.toString(k.getEncoded()));
            key = k.getEncoded();
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, k, iv);
            return c.doFinal(str.getBytes("UTF-8"));
        } catch (NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public byte[] decrypt(byte[] enStr) {
        try {
            //SecureRandom rnd = new SecureRandom();
            //IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));
            IvParameterSpec iv = new IvParameterSpec(IV);

            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            SecretKey k = new SecretKeySpec(key, "AES");
            //System.out.println(Arrays.toString(k.getEncoded));
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, k, iv);
            return c.doFinal(enStr);
        } catch (NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*
    public static void main(String[] args) {
        try {
            System.out.println("password bytes " + Arrays.toString("password".getBytes()));
            System.out.println(Arrays.toString(encrypt("password")));
            System.out.println(Arrays.toString(decrypt(encrypt("password"))));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

}
