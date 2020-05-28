package com.egls.server.utils.cipher.rsa;

import com.egls.server.utils.StringUtil;
import com.egls.server.utils.math.MathUtil;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

/**
 * @author mayer - [Created on 2019-08-02 22:04]
 */
public class RsaCipher {

    public static byte[] encrypt(Rsa.KeyBits keyBits, final RSAPublicKey publicKey, final byte[] data) throws Exception {
        final Cipher cipher = Cipher.getInstance(Rsa.CIPHER_ALGORITHM_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        final int blockCount = MathUtil.divideAndCeil(data.length, keyBits.encryptBlockLength);
        final byte[] outBytes = new byte[blockCount * keyBits.decryptBlockLength];

        for (int blockIndex = 0; blockIndex < blockCount; blockIndex++) {
            byte[] inBlockBytes = Arrays.copyOfRange(data, blockIndex * keyBits.encryptBlockLength, Math.min(data.length, (blockIndex + 1) * keyBits.encryptBlockLength));
            byte[] outBlockBytes = cipher.doFinal(inBlockBytes);
            System.arraycopy(outBlockBytes, 0, outBytes, blockIndex * keyBits.decryptBlockLength, outBlockBytes.length);
        }

        return outBytes;
    }

    public static byte[] encrypt(final RSAPublicKey publicKey, final byte[] data) throws Exception {
        return encrypt(Rsa.RSA_KEY_MODE, publicKey, data);
    }

    public static String encrypt(final RSAPublicKey publicKey, final String data) throws Exception {
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        final byte[] encryptBytes = encrypt(publicKey, dataBytes);
        return StringUtil.binaryToHex(encryptBytes);
    }

    public static byte[] decrypt(Rsa.KeyBits keyBits, final RSAPrivateKey privateKey, final byte[] data) throws Exception {
        if (data.length % keyBits.decryptBlockLength != 0) {
            throw new IllegalArgumentException("Wrong data size");
        }

        final Cipher cipher = Cipher.getInstance(Rsa.CIPHER_ALGORITHM_NAME);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        final int blockCount = data.length / keyBits.decryptBlockLength;
        final byte[] outBytes = new byte[blockCount * keyBits.encryptBlockLength];

        int bytesLength = 0;
        for (int blockIndex = 0; blockIndex < blockCount; blockIndex++) {
            byte[] inBlockBytes = Arrays.copyOfRange(data, blockIndex * keyBits.decryptBlockLength, Math.min(data.length, (blockIndex + 1) * keyBits.decryptBlockLength));
            byte[] outBlockBytes = cipher.doFinal(inBlockBytes);
            bytesLength = bytesLength + outBlockBytes.length;
            System.arraycopy(outBlockBytes, 0, outBytes, blockIndex * keyBits.encryptBlockLength, outBlockBytes.length);
        }

        return Arrays.copyOfRange(outBytes, 0, Math.min(outBytes.length, bytesLength));
    }

    public static byte[] decrypt(final RSAPrivateKey privateKey, final byte[] data) throws Exception {
        return decrypt(Rsa.RSA_KEY_MODE, privateKey, data);
    }

    public static String decrypt(final RSAPrivateKey privateKey, final String data) throws Exception {
        final byte[] dataBytes = StringUtil.hexToBinary(data);
        final byte[] decryptBytes = decrypt(privateKey, dataBytes);
        return new String(decryptBytes, Charset.defaultCharset());
    }

}
