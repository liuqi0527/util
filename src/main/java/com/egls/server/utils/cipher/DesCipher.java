package com.egls.server.utils.cipher;

import com.egls.server.utils.CharsetUtil;
import com.egls.server.utils.StringUtil;
import com.egls.server.utils.math.MathUtil;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * <pre>
 *
 *     本类提供DES算法加密解密方法,DES是一个64 bit密钥的加密算法
 *
 *     DES算法的key只能是8 bytes,如果key超过8 bytes,则取前8 bytes
 *
 *     DES算法是将数据按照8 bytes划分为一个unit的加密算法.但是数据不一定是8字节的整数倍,所以要有Padding方式.
 *
 *     Padding的方式是,加密数据不满足8 bytes的整数倍的时候,在数据末端添0 byte,直到数据长度等于原始长度最接近的8的倍数
 *
 * </pre>
 *
 * @author mayer - [Created on 2018-08-09 14:42]
 */
@Deprecated
public final class DesCipher {

    private static final int UNIT_BYTES_LENGTH = 8;

    public static byte[] encrypt(final byte[] key, final byte[] data) throws GeneralSecurityException {
        byte[] dataBytes = data;
        if (dataBytes.length % UNIT_BYTES_LENGTH != 0) {
            //兼容艾格拉斯的客户端DES算法.加密数据不满足8bytes的整数倍的时候.末端填充0.这种填充方法会导致数据本身末端的n个byte是0的时候,数据被破坏.(n>1 && n<8)
            final byte[] newDataBytes = new byte[MathUtil.divideAndCeil(dataBytes.length, UNIT_BYTES_LENGTH) * UNIT_BYTES_LENGTH];
            System.arraycopy(dataBytes, 0, newDataBytes, 0, dataBytes.length);
            dataBytes = newDataBytes;
        }
        return GeneralCipher.encrypt(GeneralCipher.Type.DES_NO_PADDING, key, dataBytes);
    }

    /**
     * 加密
     */
    public static String encrypt(final String key, final String data) throws GeneralSecurityException {
        final byte[] keyBytes = key.getBytes(CharsetUtil.defaultCharset());
        final byte[] dataBytes = data.getBytes(CharsetUtil.defaultCharset());
        final byte[] bytes = encrypt(keyBytes, dataBytes);
        return StringUtil.binaryToHex(bytes);
    }

    public static byte[] decrypt(final byte[] key, final byte[] data) throws GeneralSecurityException {
        final byte[] dataBytes = GeneralCipher.decrypt(GeneralCipher.Type.DES_NO_PADDING, key, data);
        //数据字节长度不够8的整数倍的时候的填充方式导致解密时的数组末端带有填充的0数据.无法确切知道填充几位只好将等于0的byte去掉.
        int count = 0, index = dataBytes.length - 1;
        while (dataBytes[index--] == 0) {
            count++;
        }
        return Arrays.copyOf(dataBytes, dataBytes.length - count);
    }

    /**
     * 解密
     */
    public static String decrypt(final String key, final String data) throws GeneralSecurityException {
        final byte[] keyBytes = key.getBytes(CharsetUtil.defaultCharset());
        final byte[] dataBytes = StringUtil.hexToBinary(data);
        final byte[] bytes = decrypt(keyBytes, dataBytes);
        //兼容艾格拉斯的客户端DES算法，当Padding方式出错的时候,会导致字符串出现空白.例如平台登陆验证的url.所以这里进行了trim,但是这样的处理是错误的.
        return StringUtils.trim(new String(bytes, Charset.defaultCharset()));
    }

}
