package com.egls.server.utils.cipher;

import com.egls.server.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *     IDEA = International Data Encryption Algorithm
 *
 *     本类提供IDEA算法加密解密方法,IDEA是一个128 bit密钥的加密算法
 *
 *     IDEA算法的key只能是16 bytes, 如果key超过16 bytes,则取前16 bytes
 *
 *     IDEA算法是将数据按照8 bytes划分为一个unit的加密算法.但是数据不一定是8字节的整数倍,所以要有Padding方式.
 *
 *     Padding的方式是,在末尾进行添加 (0-8] 个bytes,最后一个字节用来标记padding了几个bytes.
 *
 * </pre>
 *
 * @author mayer - [Created on 2018-08-13 19:51]
 */
public final class IdeaCipher {

    private static final int FLAG = 0x10001;

    /**
     * 分组字节长度
     */
    private static final int UNIT_BYTES_LENGTH = 8;

    /**
     * 8位的密钥需要16个. 16 bytes
     */
    private static final int BYTES_SECRET_KEY_SIZE = 16;

    /**
     * 16位的密钥需要8个. 8 shorts
     */
    private static final int SHORTS_SECRET_KEY_SIZE = 8;

    /**
     * 8个加密轮次
     */
    private static final int ENCRYPTION_CYCLE = 8;

    /**
     * 52个子密钥,8轮中的每一轮需要6个，其他4个用于输出变换
     */
    private static final int SECRET_SUB_KEY_COUNT = 52;

    /**
     * 将某个位置的两个bytes转成无符号的short.但是java的只能用int来表示无符号的short
     */
    private static int bytesToUnsignedShort(final byte[] bytes, final int position) {
        return ((bytes[position] << 8) & 0xFF00) + (bytes[position + 1] & 0xFF);
    }

    /**
     * 将某个无符号的short转为两个bytes, java只能使用int来标示无符号的short.
     */
    private static void unsignedShortToBytes(final int val, final byte[] bytes, final int position) {
        bytes[position] = (byte) (val >>> 8);
        bytes[position + 1] = (byte) val;
    }

    private static int invMul(int a) {
        if (a <= 1) {
            return a;
        }
        int b = 1;
        int c = FLAG / a;
        for (int i = FLAG % a; i != 1; ) {
            int d = a / i;
            a %= i;
            b = (b + (c * d)) & 0xFFFF;
            if (a == 1) {
                return b;
            }
            d = i / a;
            i %= a;
            c = (c + (b * d)) & 0xFFFF;
        }
        return (1 - c) & 0xFFFF;
    }

    private static int mul(int a, int b) {
        if (a == 0) {
            a = FLAG - b;
        } else if (b == 0) {
            a = FLAG - a;
        } else {
            int tmp = a * b;
            b = tmp & 0xFFFF;
            a = tmp >>> 16;
            a = (b - a) + (b < a ? 1 : 0);
        }
        return a & 0xFFFF;
    }

    private static void cipher(final int[] subKeys, final byte[] dataBytes, final byte[] outBytes) {
        int keyIndex = 0;
        int a = bytesToUnsignedShort(dataBytes, 0);
        int b = bytesToUnsignedShort(dataBytes, 2);
        int c = bytesToUnsignedShort(dataBytes, 4);
        int d = bytesToUnsignedShort(dataBytes, 6);
        for (int i = 0; i < ENCRYPTION_CYCLE; i++) {
            a = mul(a, subKeys[keyIndex++]);
            b = (b + subKeys[keyIndex++]) & 0xFFFF;
            c = (c + subKeys[keyIndex++]) & 0xFFFF;
            d = mul(d, subKeys[keyIndex++]);
            int tmp1 = b;
            int tmp2 = c;
            c = c ^ a;
            b = b ^ d;
            c = mul(c, subKeys[keyIndex++]);
            b = (b + c) & 0xFFFF;
            b = mul(b, subKeys[keyIndex++]);
            c = (c + b) & 0xFFFF;
            a = a ^ b;
            d = d ^ c;
            b = b ^ tmp2;
            c = c ^ tmp1;
        }
        unsignedShortToBytes(mul(a, subKeys[keyIndex++]), outBytes, 0);
        unsignedShortToBytes((c + subKeys[keyIndex++]) & 0xFFFF, outBytes, 2);
        unsignedShortToBytes((b + subKeys[keyIndex++]) & 0xFFFF, outBytes, 4);
        unsignedShortToBytes(mul(d, subKeys[keyIndex]), outBytes, 6);
    }

    private static int[] getEncryptSubKeys(final byte[] keyBytes) {
        int[] encryptSubKeys = new int[SECRET_SUB_KEY_COUNT];
        for (int i = 0; i < SHORTS_SECRET_KEY_SIZE; i++) {
            encryptSubKeys[i] = bytesToUnsignedShort(keyBytes, i * 2);
        }
        for (int i = 8; i < encryptSubKeys.length; i++) {
            if ((i & 0x7) < 6) {
                encryptSubKeys[i] = (((encryptSubKeys[i - 7] & 0x7f) << 9) | (encryptSubKeys[i - 6] >> 7)) & 0xFFFF;
            } else if ((i & 0x7) == 6) {
                encryptSubKeys[i] = (((encryptSubKeys[i - 7] & 0x7f) << 9) | (encryptSubKeys[i - 14] >> 7)) & 0xFFFF;
            } else {
                encryptSubKeys[i] = (((encryptSubKeys[i - 15] & 0x7f) << 9) | (encryptSubKeys[i - 14] >> 7)) & 0xFFFF;
            }
        }
        return encryptSubKeys;
    }

    private static int[] getDecryptSubKeys(final int[] encryptSubKeys) {
        int head = 0, tail = encryptSubKeys.length;
        int[] decryptSubKeys = new int[encryptSubKeys.length];
        int invA = invMul(encryptSubKeys[head++]);
        int invB = (0 - encryptSubKeys[head++]) & 0xFFFF;
        int invC = (0 - encryptSubKeys[head++]) & 0xFFFF;
        int invD = invMul(encryptSubKeys[head++]);
        decryptSubKeys[--tail] = invD;
        decryptSubKeys[--tail] = invC;
        decryptSubKeys[--tail] = invB;
        decryptSubKeys[--tail] = invA;
        for (int i = 1; i < ENCRYPTION_CYCLE; i++) {
            invA = encryptSubKeys[head++];
            invB = encryptSubKeys[head++];
            decryptSubKeys[--tail] = invB;
            decryptSubKeys[--tail] = invA;
            invA = invMul(encryptSubKeys[head++]);
            invB = (0 - encryptSubKeys[head++]) & 0xFFFF;
            invC = (0 - encryptSubKeys[head++]) & 0xFFFF;

            invD = invMul(encryptSubKeys[head++]);
            decryptSubKeys[--tail] = invD;
            decryptSubKeys[--tail] = invB;
            decryptSubKeys[--tail] = invC;
            decryptSubKeys[--tail] = invA;
        }
        invA = encryptSubKeys[head++];
        invB = encryptSubKeys[head++];
        decryptSubKeys[--tail] = invB;
        decryptSubKeys[--tail] = invA;
        invA = invMul(encryptSubKeys[head++]);
        invB = (0 - encryptSubKeys[head++]) & 0xFFFF;
        invC = (0 - encryptSubKeys[head++]) & 0xFFFF;
        invD = invMul(encryptSubKeys[head]);
        decryptSubKeys[--tail] = invD;
        decryptSubKeys[--tail] = invC;
        decryptSubKeys[--tail] = invB;
        decryptSubKeys[--tail] = invA;
        return decryptSubKeys;
    }

    /**
     * @param type true 加密. false 解密
     */
    private static int[] getSubKeys(final boolean type, final byte[] keyBytes) {
        return type ? getEncryptSubKeys(keyBytes) : getDecryptSubKeys(getEncryptSubKeys(keyBytes));
    }

    private static byte[] cipher(final byte[] keyBytes, final byte[] dataBytes, final boolean type) {
        final byte[] outBytes = new byte[UNIT_BYTES_LENGTH];
        cipher(getSubKeys(type, keyBytes), dataBytes, outBytes);
        return outBytes;
    }

    public static byte[] encrypt(final byte[] key, final byte[] data) {
        checkKey(key);

        final int padding = UNIT_BYTES_LENGTH - (data.length % UNIT_BYTES_LENGTH);
        final byte[] outBytes = new byte[data.length + padding];
        System.arraycopy(data, 0, outBytes, 0, data.length);
        outBytes[outBytes.length - 1] = (byte) padding;
        final byte[] block = new byte[UNIT_BYTES_LENGTH];
        for (int time = 0; time < outBytes.length / UNIT_BYTES_LENGTH; time++) {
            int position = time * UNIT_BYTES_LENGTH;
            System.arraycopy(outBytes, position, block, 0, UNIT_BYTES_LENGTH);
            System.arraycopy(cipher(key, block, true), 0, outBytes, position, UNIT_BYTES_LENGTH);
        }
        return outBytes;
    }

    public static String encrypt(final byte[] key, final String data) {
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        final byte[] bytes = encrypt(key, dataBytes);
        return StringUtil.binaryToHex(bytes);
    }

    public static String encrypt(final String key, final String data) {
        final byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        final byte[] bytes = encrypt(keyBytes, dataBytes);
        return StringUtil.binaryToHex(bytes);
    }

    public static byte[] decrypt(final byte[] key, final byte[] data) {
        checkKey(key);
        checkData(data);
        final byte[] block = new byte[UNIT_BYTES_LENGTH];
        for (int time = 0; time < data.length / UNIT_BYTES_LENGTH; time++) {
            int position = time * UNIT_BYTES_LENGTH;
            System.arraycopy(data, position, block, 0, UNIT_BYTES_LENGTH);
            System.arraycopy(cipher(key, block, false), 0, data, position, UNIT_BYTES_LENGTH);
        }
        final byte padding = data[data.length - 1];
        return ArrayUtils.subarray(data, 0, data.length - padding);
    }

    public static String decrypt(final byte[] key, final String data) {
        final byte[] dataBytes = StringUtil.hexToBinary(data);
        final byte[] bytes = IdeaCipher.decrypt(key, dataBytes);
        return new String(bytes, Charset.defaultCharset());
    }

    public static String decrypt(final String key, final String data) {
        final byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        final byte[] dataBytes = StringUtil.hexToBinary(data);
        final byte[] bytes = IdeaCipher.decrypt(keyBytes, dataBytes);
        return new String(bytes, Charset.defaultCharset());
    }

    private static void checkKey(final byte[] key) {
        if (ArrayUtils.isEmpty(key)) {
            throw new IllegalArgumentException("empty key for IDEA cipher");
        }
        if (key.length != BYTES_SECRET_KEY_SIZE) {
            throw new IllegalArgumentException("Wrong key size");
        }
    }

    private static void checkData(final byte[] data) {
        if (ArrayUtils.isEmpty(data)) {
            throw new IllegalArgumentException("empty data for IDEA cipher");
        }
        if (data.length % UNIT_BYTES_LENGTH != 0) {
            throw new IllegalArgumentException("Wrong data size");
        }
    }

}
