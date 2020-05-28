package com.egls.server.utils.cipher;

import com.egls.server.utils.cipher.rsa.RsaKeyUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * RSA算法加密解密工具
 *
 * @author mayer - [Created on 2018-08-13 22:16]
 */
@Deprecated
public final class RsaCipher {

    public static Pair<RSAPublicKey, RSAPrivateKey> generateKey() throws NoSuchAlgorithmException {
        return RsaKeyUtil.generateKey();
    }

    public static Pair<RSAPublicKey, RSAPrivateKey> generateKey(final int secretKeySize) throws NoSuchAlgorithmException {
        return RsaKeyUtil.generateKey(secretKeySize);
    }

    public static RSAPublicKey parsePublicKey(final String modulus, final String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return RsaKeyUtil.getPublicKey(modulus, publicExponent);
    }

    public static RSAPrivateKey parsePrivateKey(final String modulus, final String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return RsaKeyUtil.getPrivateKey(modulus, privateExponent);
    }

    /**
     * 公钥加密
     */
    public static byte[] encrypt(final RSAPublicKey publicKey, final byte[] data) throws Exception {
        return com.egls.server.utils.cipher.rsa.RsaCipher.encrypt(publicKey, data);
    }

    public static String encrypt(final RSAPublicKey publicKey, final String data) throws Exception {
        return com.egls.server.utils.cipher.rsa.RsaCipher.encrypt(publicKey, data);
    }

    /**
     * 私钥解密
     */
    public static byte[] decrypt(final RSAPrivateKey privateKey, final byte[] data) throws Exception {
        return com.egls.server.utils.cipher.rsa.RsaCipher.decrypt(privateKey, data);
    }

    public static String decrypt(final RSAPrivateKey privateKey, final String data) throws Exception {
        return com.egls.server.utils.cipher.rsa.RsaCipher.decrypt(privateKey, data);
    }

}