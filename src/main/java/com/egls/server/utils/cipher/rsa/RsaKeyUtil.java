package com.egls.server.utils.cipher.rsa;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.Enumeration;

/**
 * @author mayer - [Created on 2019-08-02 23:32]
 */
public class RsaKeyUtil {

    public static Pair<RSAPublicKey, RSAPrivateKey> generateKey() throws NoSuchAlgorithmException {
        return generateKey(Rsa.RSA_KEY_MODE);
    }

    public static Pair<RSAPublicKey, RSAPrivateKey> generateKey(Rsa.KeyBits keyBits) throws NoSuchAlgorithmException {
        return generateKey(keyBits.keyBitsLength);
    }

    public static Pair<RSAPublicKey, RSAPrivateKey> generateKey(final int secretKeySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Rsa.KEY_ALGORITHM_NAME);
        keyPairGenerator.initialize(secretKeySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return Pair.of((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
    }

    public static RSAPublicKey getPublicKey(final String modulus, final String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(Rsa.KEY_ALGORITHM_NAME);
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        return (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
    }

    public static RSAPrivateKey getPrivateKey(final String modulus, final String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(Rsa.KEY_ALGORITHM_NAME);
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        return (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivateKeySpec);
    }

    /**
     * 从公钥证书文件(pub.cer)获取获取证书对象
     */
    public static Certificate getCertificateFromFile(final String certFilePath) throws IOException, CertificateException {
        return getCertificateFromStream(new FileInputStream(certFilePath));
    }

    public static Certificate getCertificateFromStream(final InputStream certFileInputStream) throws IOException, CertificateException {
        byte[] certFileBytes = IOUtils.toByteArray(certFileInputStream);
        return getCertificate(new String(certFileBytes));
    }

    public static Certificate getCertificate(final String certFileString) throws IOException, CertificateException {
        String certFileContent = getKeyContent(certFileString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(certFileContent));
        return CertificateFactory.getInstance("X509").generateCertificate(byteArrayInputStream);
    }

    /**
     * 从公钥证书文件(pub.cer)获取公钥
     *
     * @param publicKeyFilePath 公钥文件路径
     */
    public static RSAPublicKey getPublicKeyFromFile(final String publicKeyFilePath) throws IOException, CertificateException {
        return (RSAPublicKey) getCertificateFromFile(publicKeyFilePath).getPublicKey();
    }

    /**
     * 从输入流获取公钥数据
     */
    public static RSAPublicKey getPublicKeyFromStream(final InputStream publicKeyFileInputStream) throws IOException, CertificateException {
        return (RSAPublicKey) getCertificateFromStream(publicKeyFileInputStream).getPublicKey();
    }

    /**
     * 根据公钥文本串读取公钥
     */
    public static RSAPublicKey getPublicKey(final String publicKeyString) throws CertificateException, IOException {
        return (RSAPublicKey) getCertificate(publicKeyString).getPublicKey();
    }

    /**
     * 从不含密码保护的私钥文件
     *
     * @param privateKeyFilePath 公钥文件路径
     */
    public static RSAPrivateKey getPrivateKeyFromFile(final String privateKeyFilePath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return getPrivateKeyFromStream(new FileInputStream(privateKeyFilePath));
    }

    /**
     * 从输入流获取私钥数据
     */
    public static RSAPrivateKey getPrivateKeyFromStream(final InputStream privateKeyFileInputStream) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] privateKeyBytes = IOUtils.toByteArray(privateKeyFileInputStream);
        String privateKeyString = new String(privateKeyBytes);
        return getPrivateKey(privateKeyString);
    }

    /**
     * 根据公钥文本串读取公钥
     */
    public static RSAPrivateKey getPrivateKey(final String privateKeyString) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyContent = getKeyContent(privateKeyString);
        EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        return (RSAPrivateKey) KeyFactory.getInstance(Rsa.KEY_ALGORITHM_NAME).generatePrivate(encodedKeySpec);
    }

    /**
     * 拼接密钥
     */
    private static String getKeyContent(final String keyString) throws IOException {
        StringBuilder keyContentBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(keyString));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (StringUtils.isNotBlank(line) && !line.contains("-")) {
                keyContentBuilder.append(StringUtils.trim(line));
            }
        }
        return keyContentBuilder.toString();
    }

    /**
     * 从key store文件中获取私钥,pfx文件
     */
    public static RSAPrivateKey getPrivateKeyFromKeyStoreFile(final String pfxFilePath, final String keyStorePassword) throws Exception {
        return getPrivateKeyFromKeyStoreFileStream(new FileInputStream(pfxFilePath), keyStorePassword);
    }

    /**
     * 从key store文件中获取私钥,pfx文件
     */
    public static RSAPrivateKey getPrivateKeyFromKeyStoreFileStream(final InputStream pfxFileInputStream, final String keyStorePassword) throws Exception {
        return getPrivateKeyFromKeyStoreFileBytes(IOUtils.toByteArray(pfxFileInputStream), keyStorePassword);
    }

    /**
     * 从key store文件中获取私钥,pfx文件
     */
    public static RSAPrivateKey getPrivateKeyFromKeyStoreFileBytes(byte[] pfxFileBytes, String keyStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new ByteArrayInputStream(pfxFileBytes), keyStorePassword.toCharArray());
        Enumeration<String> aliasEnum = keyStore.aliases();
        String keyAlias = null;
        if (aliasEnum.hasMoreElements()) {
            keyAlias = aliasEnum.nextElement();
        }
        return (RSAPrivateKey) keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
    }

}
