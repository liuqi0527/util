package com.egls.server.utils.cipher.rsa;

import com.egls.server.utils.StringUtil;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author mayer - [Created on 2019-08-03 03:37]
 */
public class RsaSignatureUtil {

    /**
     * 对字符串使用私钥签名
     */
    public static String sign(final RSAPrivateKey rsaPrivateKey, final String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return sign(rsaPrivateKey, data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 对数据使用私钥签名
     */
    public static String sign(final RSAPrivateKey rsaPrivateKey, final byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(Rsa.SIGNATURE_ALGORITHM_NAME);
        signature.initSign(rsaPrivateKey);
        signature.update(data);
        return StringUtil.binaryToHex(signature.sign());
    }

    /**
     * 验证签名
     */
    public static boolean verify(final RSAPublicKey rsaPublicKey, final String signatureString, final String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return verify(rsaPublicKey, signatureString, data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证签名
     */
    public static boolean verify(final RSAPublicKey rsaPublicKey, final String signatureString, final byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(Rsa.SIGNATURE_ALGORITHM_NAME);
        signature.initVerify(rsaPublicKey);
        signature.update(data);
        return signature.verify(StringUtil.hexToBinary(signatureString));
    }

}
