package com.egls.server.utils.digest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * MD5工具
 *
 * @author mayer - [Created on 2019-07-29 13:05]
 */
public final class Md5Util {

    /**
     * 获取一个String(UTF-8)的MD5,32位大写
     */
    public static String getMd5(String text) {
        return DigestTool.MD5.getDigest(text);
    }

    /**
     * 获取MD5,32位大写
     */
    public static String getMd5(final byte[] bytes) {
        return DigestTool.MD5.getDigest(bytes);
    }

    /**
     * 获取MD5,32位大写
     */
    public static String getMd5(final File file) throws IOException {
        if (file == null) {
            return null;
        }
        return DigestUtils.md5Hex(new FileInputStream(file)).toUpperCase();
    }

    public static boolean verifyMd5(final String text, final String md5) {
        return StringUtils.equals(md5, getMd5(text));
    }

    public static boolean verifyMd5(final byte[] bytes, final String md5) {
        return StringUtils.equals(md5, getMd5(bytes));
    }

    public static boolean verifyMd5(final File file, final String md5) throws IOException {
        return StringUtils.equals(md5, getMd5(file));
    }

}
