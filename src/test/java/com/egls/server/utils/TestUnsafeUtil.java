package com.egls.server.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 14:57]
 */
public class TestUnsafeUtil {

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        Assert.assertEquals(16, UnsafeUtil.sizeOf(new ATest()));
        Assert.assertEquals(200, UnsafeUtil.fullSizeOf(new ATest()));
    }

    static class UTest {
        int a = 1;
        long b = 1;
        double[] c = new double[]{1, 2, 3, 4};
    }

    static class ATest {
        UTest[] testArray = new UTest[]{new UTest(), new UTest()};
    }

}
