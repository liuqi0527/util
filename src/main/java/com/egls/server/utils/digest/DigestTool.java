package com.egls.server.utils.digest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * @author mayer - [Created on 2019-08-04 20:53]
 */
public enum DigestTool {

    /**
     * MD5摘要
     */
    MD5(DigestUtils::md5Hex, DigestUtils::md5Hex),

    /**
     * SHA-1摘要
     */
    SHA_1(DigestUtils::sha1Hex, DigestUtils::sha1Hex),

    /**
     * SHA-1摘要
     */
    SHA_256(DigestUtils::sha256Hex, DigestUtils::sha256Hex),

    /**
     * SHA-1摘要
     */
    SHA_512(DigestUtils::sha512Hex, DigestUtils::sha512Hex);

    private final Function<String, String> textFunction;

    private final Function<byte[], String> bytesFunction;

    DigestTool(Function<String, String> textFunction, Function<byte[], String> bytesFunction) {
        this.textFunction = textFunction;
        this.bytesFunction = bytesFunction;
    }

    public String getDigest(String text) {
        if (text == null) {
            return null;
        }
        return textFunction.apply(text).toUpperCase();
    }

    public String getDigest(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return bytesFunction.apply(bytes).toUpperCase();
    }

    public boolean verifyDigest(final String text, final String digest) {
        return StringUtils.equals(digest, getDigest(text));
    }

    public boolean verifyDigest(final byte[] bytes, final String digest) {
        return StringUtils.equals(digest, getDigest(bytes));
    }

}
