package com.egls.server.utils.cipher;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author mayer - [Created on 2018-09-03 15:14]
 */
public class TestCipher {

    private static final String KEY8 = "1@3,da*&";

    private static final String text = "hello world,fslgai32r953nndska0210-5gggf0-000gg6814390*&*^*%^)(NJKVJLHIH&)660NGUG)!";

    @Test
    public void testDesNoPadding() throws Exception {
        final String encryptString = DesCipher.encrypt(KEY8, text);
        final String decryptString = DesCipher.decrypt(KEY8, encryptString);
        Assert.assertEquals(text, decryptString);
    }

    @Test
    public void testDes() throws Exception {
        byte[] key = RandomUtils.nextBytes(8);
        final String encryptString = GeneralCipher.encrypt(GeneralCipher.Type.DES, key, text);
        final String decryptString = GeneralCipher.decrypt(GeneralCipher.Type.DES, key, encryptString);
        Assert.assertEquals(text, decryptString);
    }

    @Test
    public void testDesede() throws Exception {
        byte[] key = RandomUtils.nextBytes(24);
        final String encryptString = GeneralCipher.encrypt(GeneralCipher.Type.DESede, key, text);
        final String decryptString = GeneralCipher.decrypt(GeneralCipher.Type.DESede, key, encryptString);
        Assert.assertEquals(text, decryptString);
    }

    @Test
    public void testAes() throws Exception {
        byte[] key = RandomUtils.nextBytes(32);
        final String encryptString = GeneralCipher.encrypt(GeneralCipher.Type.AES, key, text);
        final String decryptString = GeneralCipher.decrypt(GeneralCipher.Type.AES, key, encryptString);
        Assert.assertEquals(text, decryptString);
    }

    @Test
    public void testBlowfish() throws Exception {
        byte[] key = RandomUtils.nextBytes(56);
        final String encryptString = GeneralCipher.encrypt(GeneralCipher.Type.BLOWFISH, key, text);
        final String decryptString = GeneralCipher.decrypt(GeneralCipher.Type.BLOWFISH, key, encryptString);
        Assert.assertEquals(text, decryptString);
    }

    @Test
    public void testIDEA() {
        byte[] key = RandomUtils.nextBytes(16);
        final String encryptString = IdeaCipher.encrypt(key, text);
        final String decryptString = IdeaCipher.decrypt(key, encryptString);
        Assert.assertEquals(text, decryptString);
    }

    @Test
    public void testRSA() throws Exception {
        final Pair<RSAPublicKey, RSAPrivateKey> keyPair = RsaCipher.generateKey();
        final RSAPublicKey publicKey = keyPair.getLeft();
        final RSAPrivateKey privateKey = keyPair.getRight();

        final String publicKeyModulus = String.valueOf(publicKey.getModulus());
        final String publicKeyExponent = String.valueOf(publicKey.getPublicExponent());

        final String privateKeyModulus = String.valueOf(privateKey.getModulus());
        final String privateKeyExponent = String.valueOf(privateKey.getPrivateExponent());

        final String encryptString = RsaCipher.encrypt(RsaCipher.parsePublicKey(publicKeyModulus, publicKeyExponent), text);
        final String decryptString = RsaCipher.decrypt(RsaCipher.parsePrivateKey(privateKeyModulus, privateKeyExponent), encryptString);

        Assert.assertEquals(text, decryptString);
    }

    @Test
    public void testPerformance() throws GeneralSecurityException {
        byte[] key = RandomUtils.nextBytes(16);
        int time = 50000;
        System.out.println("encrypt content bytes length : " + text.getBytes().length);
        System.out.println("encrypt time : " + time);

        long millis = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            DesCipher.encrypt(KEY8, text);
        }
        System.out.println("des_64_bit : " + (System.currentTimeMillis() - millis));

        millis = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            IdeaCipher.encrypt(key, text);
        }
        System.out.println("idea_128_bit : " + (System.currentTimeMillis() - millis));
    }

}
