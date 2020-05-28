package com.egls.server.utils.math;

import java.util.Collection;

import com.egls.server.utils.random.RandomUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 提供一些数学运算的工具方法
 *
 * @author mayer - [Created on 2018-08-09 14:57]
 */
public final class MathUtil {

    public static byte formatInRange(final byte number, final byte min, final byte max) {
        return number < min ? min : number > max ? max : number;
    }

    public static char formatInRange(final char number, final char min, final char max) {
        return number < min ? min : number > max ? max : number;
    }

    public static short formatInRange(final short number, final short min, final short max) {
        return number < min ? min : number > max ? max : number;
    }

    public static int formatInRange(final int number, final int min, final int max) {
        return number < min ? min : number > max ? max : number;
    }

    public static long formatInRange(final long number, final long min, final long max) {
        return number < min ? min : number > max ? max : number;
    }

    public static float formatInRange(final float number, final float min, final float max) {
        return number < min ? min : number > max ? max : number;
    }

    public static double formatInRange(final double number, final double min, final double max) {
        return number < min ? min : number > max ? max : number;
    }

    public static double sum(final Collection<? extends Number> values) {
        double result = 0;
        for (Number number : values) {
            result += number.doubleValue();
        }
        return result;
    }

    public static double sum(final Number... values) {
        double result = 0;
        for (Number number : values) {
            result += number.doubleValue();
        }
        return result;
    }

    /**
     * 64位整型数安全减法,不会产生越界溢出的问题.
     *
     * @param value       被减数
     * @param minusValues 减数
     * @return 差
     */
    public static long minusLong(final long value, final long... minusValues) {
        if (ArrayUtils.isEmpty(minusValues)) {
            return value;
        }
        double temp = value;
        for (long minusValue : minusValues) {
            temp -= minusValue;
        }
        return getLongValue(temp);
    }

    public static long minusLongWithMin(final long min, final long value, final long... minusValues) {
        long result = minusLong(value, minusValues);
        return result < min ? min : result;
    }

    public static long minusLongWithMax(final long max, final long value, final long... minusValues) {
        long result = minusLong(value, minusValues);
        return result > max ? max : result;
    }

    public static long minusLongWithRange(final long max, final long min, final long value, final long... minusValues) {
        long result = minusLong(value, minusValues);
        return result < min ? min : result > max ? max : result;
    }

    /**
     * 64位整型数安全加法,不会产生越界溢出变为负数的问题.
     *
     * @param value     被加数
     * @param addValues 加数
     * @return 和
     */
    public static long plusLong(final long value, final long... addValues) {
        if (ArrayUtils.isEmpty(addValues)) {
            return value;
        }
        double temp = value;
        for (long addValue : addValues) {
            temp += addValue;
        }
        return getLongValue(temp);
    }

    public static long plusLongWithMin(final long min, final long value, final long... addValues) {
        long result = plusLong(value, addValues);
        return result < min ? min : result;
    }

    public static long plusLongWithMax(final long max, final long value, final long... addValues) {
        long result = plusLong(value, addValues);
        return result > max ? max : result;
    }

    public static long plusLongWithRange(final long max, final long min, final long value, final long... addValues) {
        long result = plusLong(value, addValues);
        return result < min ? min : result > max ? max : result;
    }

    /**
     * 32位整型数安全减法,不会产生越界溢出的问题.
     *
     * @param value       被减数
     * @param minusValues 减数
     * @return 差
     */
    public static int minusInt(final int value, final int... minusValues) {
        if (ArrayUtils.isEmpty(minusValues)) {
            return value;
        }
        double temp = value;
        for (int minusValue : minusValues) {
            temp -= minusValue;
        }
        return getIntValue(temp);
    }

    public static int minusIntWithMin(final int min, final int value, final int... minusValues) {
        int result = minusInt(value, minusValues);
        return result < min ? min : result;
    }

    public static int minusIntWithMax(final int max, final int value, final int... minusValues) {
        int result = minusInt(value, minusValues);
        return result > max ? max : result;
    }

    public static int minusIntWithRange(final int max, final int min, final int value, final int... minusValues) {
        int result = minusInt(value, minusValues);
        return result < min ? min : result > max ? max : result;
    }

    /**
     * 32位整型数安全加法,不会产生越界溢出变为负数的问题.
     *
     * @param value     被加数
     * @param addValues 加数
     * @return 和
     */
    public static int plusInt(final int value, final int... addValues) {
        if (ArrayUtils.isEmpty(addValues)) {
            return value;
        }
        double temp = value;
        for (int addValue : addValues) {
            temp += addValue;
        }
        return getIntValue(temp);
    }

    public static int plusIntWithMin(final int min, final int value, final int... addValues) {
        int result = plusInt(value, addValues);
        return result < min ? min : result;
    }

    public static int plusIntWithMax(final int max, final int value, final int... addValues) {
        int result = plusInt(value, addValues);
        return result > max ? max : result;
    }

    public static int plusIntWithRange(final int max, final int min, final int value, final int... addValues) {
        int result = plusInt(value, addValues);
        return result < min ? min : result > max ? max : result;
    }

    /**
     * 获取一个32位整型数
     *
     * @param value 浮点数
     * @return 32位整型数
     */
    public static int getIntValue(final double value) {
        if (value >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (value <= Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    /**
     * 获取一个64位整型数
     *
     * @param value 浮点数
     * @return 64位整型数
     */
    public static long getLongValue(final double value) {
        if (value >= Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        } else if (value <= Long.MIN_VALUE) {
            return Long.MIN_VALUE;
        }
        return (long) value;
    }

    /**
     * 多个32位整型数求平均数
     *
     * @param array 32位整型数
     * @return 平均数
     */
    public static int avg(final int... array) {
        double sum = 0;
        for (int value : array) {
            sum += value;
        }
        return getIntValue(sum / array.length);
    }

    /**
     * 多个64位整型数求平均数
     *
     * @param array 64位整型数
     * @return 平均数
     */
    public static long avg(final long... array) {
        double sum = 0;
        for (long value : array) {
            sum += value;
        }
        return getLongValue(sum / array.length);
    }

    public static float avg(final float... array) {
        float sum = 0;
        for (float value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    public static double avg(final double... array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    public static byte max(final byte... data) {
        return NumberUtils.max(data);
    }

    public static short max(final short... data) {
        return NumberUtils.max(data);
    }

    public static int max(final int... data) {
        return NumberUtils.max(data);
    }

    public static long max(final long... data) {
        return NumberUtils.max(data);
    }

    public static float max(final float... data) {
        return NumberUtils.max(data);
    }

    public static double max(final double... data) {
        return NumberUtils.max(data);
    }

    public static byte min(final byte... data) {
        return NumberUtils.min(data);
    }

    public static short min(final short... data) {
        return NumberUtils.min(data);
    }

    public static int min(final int... data) {
        return NumberUtils.min(data);
    }

    public static long min(final long... data) {
        return NumberUtils.min(data);
    }

    public static float min(final float... data) {
        return NumberUtils.min(data);
    }

    public static double min(final double... data) {
        return NumberUtils.min(data);
    }

    public static int fluctuate(final int value, final int base, final int fluctuate) {
        return getIntValue(fluctuate((double) value, (double) base, (double) fluctuate));
    }

    public static long fluctuate(final long value, final long base, final long fluctuate) {
        return getLongValue(fluctuate((double) value, (double) base, (double) fluctuate));
    }

    /**
     * 使一个值在一定范围内波动, 比如:波动正负10%(value,100,10)
     *
     * @param value     基础值
     * @param base      波动基数
     * @param fluctuate 波动比值
     * @return 随机波动结果
     */
    public static double fluctuate(final double value, final double base, final double fluctuate) {
        //fluctuate表示在base范围内，上下波动的范围
        double mul = 1000;
        double b = Math.abs(base) * mul;
        double f = Math.abs(fluctuate) * mul;

        double baseDivisor = b - f;
        double randomDivisor = RandomUtil.randomDouble(f * 2 + 1);
        return value * (baseDivisor + randomDivisor) / b;
    }

    public static int divideAndCeil(final int a, final int b) {
        return (a / b) + (a % b > 0 ? 1 : 0);
    }

    public static long divideAndCeil(final long a, final long b) {
        return (a / b) + (a % b > 0 ? 1 : 0);
    }

    public static int multiply(final int a, final int b) {
        return getIntValue((double) a * (double) b);
    }

    public static long multiply(final long a, final long b) {
        return getLongValue((double) a * (double) b);
    }

    /**
     * 获取一个数的千分值的多少.
     * 比如获取500的千分之3
     * 就应该是1.5
     */
    public static int getValueOfPermillage(final int value, final double permillage) {
        return getIntValue(value * (permillage / 1000D));
    }

    /**
     * 获取一个数的千分值的多少.
     * 比如获取500的千分之3
     * 就应该是1.5
     */
    public static long getValueOfPermillage(final long value, final double permillage) {
        return getLongValue(value * (permillage / 1000D));
    }

    /**
     * 获取一个数的千分值的多少.
     * 比如获取500的千分之3
     * 就应该是1.5
     */
    public static double getValueOfPermillage(final double value, final double permillage) {
        return value * (permillage / 1000D);
    }

    /**
     * 获取一个数的百分值的多少.
     * 比如获取500的百分之3
     * 就应该是15
     */
    public static int getValueOfPercentage(final int value, final double percentage) {
        return getIntValue(value * (percentage / 100D));
    }

    /**
     * 获取一个数的百分值的多少.
     * 比如获取500的百分之3
     * 就应该是15
     */
    public static long getValueOfPercentage(final long value, final double percentage) {
        return getLongValue(value * (percentage / 100D));
    }

    /**
     * 获取一个数的百分值的多少.
     * 比如获取500的百分之3
     * 就应该是15
     */
    public static double getValueOfPercentage(final double value, final double percentage) {
        return value * (percentage / 100D);
    }

    /**
     * 运算公式:result = primitive * (permillage / 1000) + value
     */
    public static double calculateValueOfPermillage(final double primitive, final double permillage, final double value) {
        return primitive * permillage / 1000D + value;
    }

    /**
     * 运算公式:result = primitive * (permillage / 100) + value
     */
    public static double calculateValueOfPercentage(final double primitive, final double percentage, final double value) {
        return primitive * percentage / 100D + value;
    }

}
