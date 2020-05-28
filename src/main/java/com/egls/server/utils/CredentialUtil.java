package com.egls.server.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mayer - [Created on 2018-10-30 13:28]
 */
public final class CredentialUtil {

    /**
     * 用所有网络硬件计算一个64bit的标记
     *
     * @return 标记
     */
    public static long getComputerCredential() throws SocketException {
        long credential = 0;
        final Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = netInterfaces.nextElement();
            final byte[] hardwareAddress = networkInterface.getHardwareAddress();
            if (networkInterface.isVirtual() || ArrayUtils.isEmpty(hardwareAddress)) {
                continue;
            }
            for (int i = 0; i < hardwareAddress.length; i++) {
                credential = credential + ((hardwareAddress[i] & 0xFFL) << (8 * i));
            }
        }
        return credential;
    }

    public static String getComputerCredentialString() throws SocketException {
        return Long.toHexString(getComputerCredential());
    }

    public static boolean verifyComputerCredential(final long credential) throws SocketException {
        long computerCredential = getComputerCredential();
        return (computerCredential & credential) == credential;
    }

    public static boolean verifyComputerCredentialString(final String credential) throws SocketException {
        return verifyComputerCredential(Long.parseLong(credential, 16));
    }

    /**
     * 身份证号长度
     */
    private static final int RESIDENT_IDENTITY_CARD_NUMBER_LENGTH = 18;

    /**
     * 身份验证十七位数字本体码权重
     */
    private static final int[] RESIDENT_IDENTITY_CARD_NUMBER_WEIGHTS = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 身份验证校验码字符值
     */
    private static final String[] RESIDENT_IDENTITY_CARD_NUMBER_MOD = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    public static boolean verifyResidentIdentityCardNumber(final String credential) {
        if (StringUtils.isBlank(credential)) {
            return false;
        }
        char[] chars = credential.toCharArray();
        if (chars.length != RESIDENT_IDENTITY_CARD_NUMBER_LENGTH) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < chars.length - 1; i++) {
            if (!CharUtils.isAsciiNumeric(chars[i])) {
                return false;
            }
            sum += CharUtils.toIntValue(chars[i]) * RESIDENT_IDENTITY_CARD_NUMBER_WEIGHTS[i];
        }
        //前17位求和,结果对11取模,获得对应校验位的索引
        return StringUtils.equalsIgnoreCase(CharUtils.toString(chars[chars.length - 1]), RESIDENT_IDENTITY_CARD_NUMBER_MOD[sum % 11]);
    }

    private CredentialUtil() {
    }

}
