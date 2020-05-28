package com.egls.server.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 14:54]
 */
public class TestNumberUtil {

    @Test
    public void test0() {
        Assert.assertFalse(NumberUtil.isDecimalNumeric("a"));
        Assert.assertFalse(NumberUtil.isDecimalNumeric("1a"));
        Assert.assertFalse(NumberUtil.isDecimalNumeric("a1"));
        Assert.assertFalse(NumberUtil.isDecimalNumeric("1.0.0"));
        Assert.assertFalse(NumberUtil.isDecimalNumeric("1..0"));
        Assert.assertFalse(NumberUtil.isDecimalNumeric("+1a0"));

        Assert.assertTrue(NumberUtil.isDecimalNumeric("1"));
        Assert.assertTrue(NumberUtil.isDecimalNumeric("+1"));
        Assert.assertTrue(NumberUtil.isDecimalNumeric("-1"));
        Assert.assertTrue(NumberUtil.isDecimalNumeric("1.0"));
        Assert.assertTrue(NumberUtil.isDecimalNumeric("+1.0"));
        Assert.assertTrue(NumberUtil.isDecimalNumeric("-1.0"));
    }

    @Test
    public void test1() {
        double d1 = 1D;
        double d2 = 1D + 1E-5D;
        double d3 = 1D + 1E-6D;
        double d4 = 1D + 1E-7D;

        Assert.assertFalse(NumberUtil.isApproximateZero(d1 - d2));
        Assert.assertTrue(NumberUtil.isApproximateZero(d1 - d3));
        Assert.assertTrue(NumberUtil.isApproximateZero(d1 - d4));

        Assert.assertFalse(NumberUtil.doubleIsEqual(d1, d2));
        Assert.assertTrue(NumberUtil.doubleIsEqual(d1, d3));
        Assert.assertTrue(NumberUtil.doubleIsEqual(d1, d4));
    }

    @Test
    public void test2() {
        float f1 = 1f;
        float f2 = 1f + 1E-5F;
        float f3 = 1f + 1E-6F;
        float f4 = 1f + 1E-7F;

        Assert.assertFalse(NumberUtil.isApproximateZero(f1 - f2));
        Assert.assertTrue(NumberUtil.isApproximateZero(f1 - f3));
        Assert.assertTrue(NumberUtil.isApproximateZero(f1 - f4));

        Assert.assertFalse(NumberUtil.doubleIsEqual(f1, f2));
        Assert.assertTrue(NumberUtil.doubleIsEqual(f1, f3));
        Assert.assertTrue(NumberUtil.doubleIsEqual(f1, f4));
    }

    @Test
    public void test3() {
        Assert.assertEquals(1, NumberUtil.convertPrecision(1.234, 0), 0.0);
        Assert.assertEquals(1.2, NumberUtil.convertPrecision(1.234, 1), 0.0);
        Assert.assertEquals(1.23, NumberUtil.convertPrecision(1.234, 2), 0.0);
        Assert.assertEquals(1.234, NumberUtil.convertPrecision(1.234, 3), 0.0);
        Assert.assertEquals(1.234, NumberUtil.convertPrecision(1.234, 4), 0.0);
    }


}
