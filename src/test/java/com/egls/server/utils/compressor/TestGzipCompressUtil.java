package com.egls.server.utils.compressor;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2019-06-18 15:29]
 */
public class TestGzipCompressUtil {

    private static final String string = "asdf1234%……*）*()U_HTG&*G)^F79h9bn&GP:F*P";

    @Test
    public void test() throws Exception {
        String hexString = GzipCompressUtil.compressToHexString(string);
        Assert.assertEquals(string, GzipCompressUtil.decompressHexString(hexString));
    }

    @Test
    public void test1() throws Exception {
        String hexString = GzipCompressUtil.compressToBase64String(string);
        Assert.assertEquals(string, GzipCompressUtil.decompressBase64String(hexString));
    }

}
