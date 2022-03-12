package com.moderngas.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class AESUtil {

    private static String secretKey;

    private static String saltValue;

    @Autowired
    public AESUtil(@Value("${aes.secret.key}") String key,
                   @Value("${aes.salt.value}") String salt) {
        this.secretKey = key;
        this.saltValue = salt;
    }

    @PostConstruct
    public void init() {
        log.info("AESUtil >>> Started");
    }

    /* Generate IV */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /* Generate Key */
    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    /* Encryption Method */
    public static String encrypt(String strToEncrypt) {
        try {
            /* Declare a byte array. */
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec parameterSpec = new IvParameterSpec(iv);
            /* Create factory for secret keys. */
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            /* PBEKeySpec class implements KeySpec interface. */
            KeySpec pbeKeySpec = new PBEKeySpec(secretKey.toCharArray(), saltValue.getBytes(), 65536, 256);
            //KeySpec pbeKeySpec = new PBEKeySpec("chaLLenge@Reek2022Encrypt".toCharArray(), "tokenModern".getBytes(), 65536, 256);
            SecretKey secret = factory.generateSecret(pbeKeySpec);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
            /* Return encrypted value. */
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeySpecException e) {
            log.error("Error occurred during encryption: {}", e);
        }
        return null;
    }

    /* Decryption Method */
    public static String decrypt(String strToDecrypt) {
        try {
            /* Declare a byte array. */
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec parameterSpec = new IvParameterSpec(iv);
            /* Create factory for secret keys. */
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            /* PBEKeySpec class implements KeySpec interface. */
            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), saltValue.getBytes(), 65536, 256);
            //KeySpec keySpec = new PBEKeySpec("chaLLenge@Reek2022Encrypt".toCharArray(), "tokenModern".getBytes(), 65536, 256);
            SecretKey secret = factory.generateSecret(keySpec);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            /* Return decrypted value. */
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            log.info("Error occured during decryption: {}" + e);
        }
        return null;
    }


    public static String createJWTToken(String username) {
        return encrypt(JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes())));
    }


}
