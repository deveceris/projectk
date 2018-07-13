package kr.co.eceris.projectk;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StaticTest {

    @Test
    public void asdf() {
        String test1 = new BCryptPasswordEncoder().encode("test1");
        System.out.println(test1);
    }

    @Test
    public void aaa() {
        Date date = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(720));
        System.out.println(date);
    }
    //        String cipherText = "U2FsdGVkX1+w5mJIRfmgiZavdZmYtTx/qEEQx/q3bTQ=";
    private static final String key = "test";


    @Test
    public void asdffff() {
        String encode = decode("U2FsdGVkX1+w5mJIRfmgiZavdZmYtTx/qEEQx/q3bTQ=");
        System.out.println(encode);
    }
    private static SecretKeySpec getSecretKeySpec() {
        SecretKeySpec keySpec = null;
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            keySpec = new SecretKeySpec(keyBytes, "AES");
        } catch (UnsupportedEncodingException e) {
            log.error("Set SecretKeySpec failed.", e);
        }
        return keySpec;
    }

    public static String encode(String rawStr) {
        String iv = key.substring(0, 16);
        SecretKeySpec keySpec = getSecretKeySpec();
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
            encrypted = cipher.doFinal(rawStr.getBytes("UTF-8"));
        } catch (Exception e){
            log.error("Password encode failed.", e);
        }
        return new String(Base64.encodeBase64(encrypted));

    }

    public static String decode(String encStr) {
        String iv = key;
        SecretKeySpec keySpec = getSecretKeySpec();
        byte[] decrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
            decrypted = cipher.doFinal(Base64.decodeBase64(encStr.getBytes()));
        } catch (Exception e) {
            log.error("Password decode failed.",e);
        }
        return new String(decrypted);
    }

}
