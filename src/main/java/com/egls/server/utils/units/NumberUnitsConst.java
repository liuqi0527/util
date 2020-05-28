package com.egls.server.utils.units;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 提供一些数字单位
 *
 * @author mayer - [Created on 2018-08-13 19:40]
 */
public final class NumberUnitsConst {

    //@formatter:off

    public static final int INT_TEN                             = 10;
    public static final int INT_HUNDRED                         = 100;
    public static final int INT_THOUSAND                        = 1000;
    public static final int INT_TEN_THOUSAND                    = 10000;
    public static final int INT_ONE_HUNDRED_THOUSAND            = 100000;
    public static final int INT_MILLION                         = 1000000;

    public static final long LONG_TEN                           = 10L;
    public static final long LONG_HUNDRED                       = 100L;
    public static final long LONG_THOUSAND                      = 1000L;
    public static final long LONG_TEN_THOUSAND                  = 10000L;
    public static final long LONG_ONE_HUNDRED_THOUSAND          = 100000L;
    public static final long LONG_MILLION                       = 1000000L;

    public static final float FLOAT_TEN                         = 10F;
    public static final float FLOAT_HUNDRED                     = 100F;
    public static final float FLOAT_THOUSAND                    = 1000F;
    public static final float FLOAT_TEN_THOUSAND                = 10000F;
    public static final float FLOAT_ONE_HUNDRED_THOUSAND        = 100000F;
    public static final float FLOAT_MILLION                     = 1000000F;

    public static final double DOUBLE_TEN                       = 10D;
    public static final double DOUBLE_HUNDRED                   = 100D;
    public static final double DOUBLE_THOUSAND                  = 1000D;
    public static final double DOUBLE_TEN_THOUSAND              = 10000D;
    public static final double DOUBLE_ONE_HUNDRED_THOUSAND      = 100000D;
    public static final double DOUBLE_MILLION                   = 1000000D;

    public static final int THE_3_POWER_OF_2                    = 8;
    public static final int THE_4_POWER_OF_2                    = 16;
    public static final int THE_5_POWER_OF_2                    = 32;
    public static final int THE_6_POWER_OF_2                    = 64;
    public static final int THE_7_POWER_OF_2                    = 128;
    public static final int THE_8_POWER_OF_2                    = 256;
    public static final int THE_9_POWER_OF_2                    = 512;
    public static final int THE_10_POWER_OF_2                   = 1024;
    public static final int THE_11_POWER_OF_2                   = 2048;
    public static final int THE_12_POWER_OF_2                   = 4096;
    public static final int THE_13_POWER_OF_2                   = 8192;
    public static final int THE_14_POWER_OF_2                   = 16384;
    public static final int THE_15_POWER_OF_2                   = 32768;
    public static final int THE_16_POWER_OF_2                   = 65536;
    public static final int THE_17_POWER_OF_2                   = 131072;
    public static final int THE_18_POWER_OF_2                   = 262144;
    public static final int THE_19_POWER_OF_2                   = 524288;
    public static final int THE_20_POWER_OF_2                   = 1048576;
    public static final int THE_21_POWER_OF_2                   = 2097152;
    public static final int THE_22_POWER_OF_2                   = 4194304;

    //@formatter:on

    /**
     * 对于一个数字的小数部分的精确位数,此位数之后的都舍弃掉
     *
     * @see #FLOAT_APPROXIMATE_ZERO
     * @see #DOUBLE_APPROXIMATE_ZERO
     */
    public static final int APPROXIMATE_ZERO_DIGITS = 6;

    /**
     * 无论正数负数,绝对值小于等于此值,则认为0
     */
    public static final float FLOAT_APPROXIMATE_ZERO = ObjectUtils.CONST(1E-6F);

    /**
     * 无论正数负数,绝对值小于等于此值,则认为0
     */
    public static final double DOUBLE_APPROXIMATE_ZERO = ObjectUtils.CONST(1E-6D);

    public static final float FLOAT_MIN_VALUE_OF_INT = ObjectUtils.CONST(Integer.MIN_VALUE);
    public static final float FLOAT_MAX_VALUE_OF_INT = ObjectUtils.CONST(Integer.MAX_VALUE);

    public static final float DOUBLE_MIN_VALUE_OF_INT = ObjectUtils.CONST(Integer.MIN_VALUE);
    public static final float DOUBLE_MAX_VALUE_OF_INT = ObjectUtils.CONST(Integer.MAX_VALUE);

    public static final float FLOAT_MIN_VALUE_OF_LONG = ObjectUtils.CONST(Long.MIN_VALUE);
    public static final float FLOAT_MAX_VALUE_OF_LONG = ObjectUtils.CONST(Long.MAX_VALUE);

    public static final float DOUBLE_MIN_VALUE_OF_LONG = ObjectUtils.CONST(Long.MIN_VALUE);
    public static final float DOUBLE_MAX_VALUE_OF_LONG = ObjectUtils.CONST(Long.MAX_VALUE);

}
