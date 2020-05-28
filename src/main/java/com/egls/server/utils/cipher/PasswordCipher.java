package com.egls.server.utils.cipher;

import com.egls.server.utils.StringUtil;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * 密码专用加密,不可解密.
 * <p>
 * 1.密码加密不可逆运算得到用户的密码
 * 2.密码加密使内部人员无法得知用户的密码
 * 3.密码加密保证能够看到数据的人员无法通过设置相同的密码得知用户的密码
 * 4.即使被拖库,破解所有用户密码的成本很高,攻击者也无法从中破解出用户的密码
 * <p>
 * 对于保存用户密码所用的散列函数,密码学的应用安全,是建立在破解所要付出的成本远超出能得到的利益上的。
 * 必须要求算法本身不能通过密文C能得到明文P,这个散列算法本身就可以做到.
 * 密码哈希一定要加盐,并且每个密码的盐都不一样,避免使用同一套生成的查询库即可破解所有密码.将生成密码库的沉没成本变成边际成本
 * <p>
 * 密码改变的同时,盐值也要改变,生成新的盐值,使用新的盐值进行重新计算密码哈希散列值.
 *
 * @author mayer - [Created on 2019-12-06 19:40]
 */
public class PasswordCipher {

    /**
     * SecureRandom 内置两种随机数算法, NativePRNG 和 SHA1PRNG
     * SHA1PRNG 性能好,NativePRNG 安全性高。
     */
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

    /**
     * 摘要算法
     */
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";

    /**
     * 盐的字节数
     */
    private static final int SALT_LENGTH = 64;

    /**
     * 生成密文的字节数
     */
    private static final int DERIVED_KEY_LENGTH = 64;

    /**
     * 迭代次数,调整这个值来改变运行时间,提升破解成本
     */
    private static final int PBKDF2_ITERATIONS = 12345;

    /**
     * 对密码进行验证,用相同的盐值对用户输入的密码进行加密,把加密后的密文和原密文进行比较,相同则验证成功,否则失败
     *
     * @param password       输入的密码
     * @param salt           盐值
     * @param hashedPassword 保存的加密过的密码
     * @return true or false
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return hashedPassword.equals(getPasswordHash(password, salt));
    }

    /**
     * 根据password和salt生成密文,这里的password并不是一定是明文密码,可以是已经进行过哈希映射的密码
     *
     * @param password 输入的密码
     * @param salt     盐值
     * @return 加密过的密码
     */
    public static String getPasswordHash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final byte[] saltBytes = StringUtil.hexToBinary(salt);
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        final KeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), saltBytes, PBKDF2_ITERATIONS, DERIVED_KEY_LENGTH * 8);
        byte[] hashBytes = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        return StringUtil.binaryToHex(hashBytes);
    }

    /**
     * 生成随机盐值
     *
     * @return 随机盐值
     */
    public static String getPasswordSalt() throws NoSuchAlgorithmException {
        // UUID其实就是一个随机盐值. return UUID.randomUUID().toString().replaceAll("-", "") ;
        final SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        final byte[] randomBytes = new byte[SALT_LENGTH];
        random.nextBytes(randomBytes);
        return StringUtil.binaryToHex(randomBytes);
    }

}
