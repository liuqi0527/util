package com.egls.server.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author mayer - [Created on 2018-08-09 22:30]
 * @see com.egls.server.utils.digest.Md5Util
 */
@Deprecated
public final class Md5Util {

    public static boolean verify(final String text, final String md5) {
        return com.egls.server.utils.digest.Md5Util.verifyMd5(text, md5);
    }

    public static String digest(final String text) {
        return com.egls.server.utils.digest.Md5Util.getMd5(text);
    }

    public static String digest(final byte[] bytes) {
        return com.egls.server.utils.digest.Md5Util.getMd5(bytes);
    }

    public static String digest(final File file) throws IOException {
        return com.egls.server.utils.digest.Md5Util.getMd5(file);
    }

}
