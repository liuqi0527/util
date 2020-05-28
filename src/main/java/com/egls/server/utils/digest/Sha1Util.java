package com.egls.server.utils.digest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author mayer - [Created on 2019-08-03 03:33]
 */
public class Sha1Util {

    /**
     * 获取一个String(UTF-8)的SHA-1大写
     */
    public static String getSha1(String text) {
        return DigestTool.SHA_1.getDigest(text);
    }

    /**
     * 获取SHA-1大写
     */
    public static String getSha1(final byte[] bytes) {
        return DigestTool.SHA_1.getDigest(bytes);
    }

    /**
     * 获取SHA-1大写
     */
    public static String getSha1(final File file) throws IOException {
        if (file == null) {
            return null;
        }
        return DigestUtils.sha1Hex(new FileInputStream(file)).toUpperCase();
    }

    public static boolean verifySha1(final String text, final String sha1) {
        return StringUtils.equals(sha1, getSha1(text));
    }

    public static boolean verifySha1(final byte[] bytes, final String sha1) {
        return StringUtils.equals(sha1, getSha1(bytes));
    }

    public static boolean verifySha1(final File file, final String sha1) throws IOException {
        return StringUtils.equals(sha1, getSha1(file));
    }

}
