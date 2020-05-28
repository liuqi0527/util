package com.egls.server.utils.cipher;

import com.egls.server.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

/**
 * @author mayer - [Created on 2019-05-16 22:34]
 */
public class GeneralCipher {

    public enum Type {

        /**
         * DES加密
         */
        DES_NO_PADDING("DES", "DES/ECB/NoPadding"),

        /**
         * DES加密,要求密钥长度为64位
         */
        DES("DES", "DES/ECB/PKCS5Padding"),

        /**
         * 3重DES加密,要求密钥长度为192位
         */
        DESede("DESede", "DESede/ECB/PKCS5Padding"),

        /**
         * AES加密,要求密钥长度为128-256位
         */
        AES("AES", "AES/ECB/PKCS5Padding"),

        /**
         * BLOWFISH加密,要求密钥长度为8-448位
         */
        BLOWFISH("Blowfish", "Blowfish/ECB/PKCS5Padding");

        private String algorithm;

        private String transformation;

        Type(String algorithm, String transformation) {
            this.algorithm = algorithm;
            this.transformation = transformation;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public String getTransformation() {
            return transformation;
        }
    }

    private static Cipher getCipher(Type type, final int cipherMode, final byte[] key) throws GeneralSecurityException {
        SecretKeySpec secretKey = new SecretKeySpec(key, type.getAlgorithm());
        Cipher cipher = Cipher.getInstance(type.getTransformation());
        cipher.init(cipherMode, secretKey);
        return cipher;
    }

    public static byte[] encrypt(Type type, final byte[] key, final byte[] data) throws GeneralSecurityException {
        final Cipher cipher = getCipher(type, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static String encrypt(Type type, final byte[] key, final String data) throws GeneralSecurityException {
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        final byte[] bytes = encrypt(type, key, dataBytes);
        return StringUtil.binaryToHex(bytes);
    }

    public static String encrypt(Type type, final String key, final String data) throws GeneralSecurityException {
        final byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        final byte[] bytes = encrypt(type, keyBytes, dataBytes);
        return StringUtil.binaryToHex(bytes);
    }

    public static byte[] decrypt(Type type, final byte[] key, final byte[] data) throws GeneralSecurityException {
        final Cipher cipher = getCipher(type, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static String decrypt(Type type, final byte[] key, final String data) throws GeneralSecurityException {
        final byte[] dataBytes = StringUtil.hexToBinary(data);
        final byte[] bytes = decrypt(type, key, dataBytes);
        return StringUtils.trim(new String(bytes, Charset.defaultCharset()));
    }

    public static String decrypt(Type type, final String key, final String data) throws GeneralSecurityException {
        final byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        final byte[] dataBytes = StringUtil.hexToBinary(data);
        final byte[] bytes = decrypt(type, keyBytes, dataBytes);
        return StringUtils.trim(new String(bytes, Charset.defaultCharset()));
    }
}
