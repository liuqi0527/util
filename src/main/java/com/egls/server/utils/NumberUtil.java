package com.egls.server.utils;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import com.egls.server.utils.units.NumberUnitsConst;

import org.apache.commons.lang3.StringUtils;

/**
 * 提供一些数字的工具方法
 *
 * @author mayer - [Created on 2018-08-09 20:39]
 */
public final class NumberUtil {

    /**
     * 整数部分的分隔符
     */
    private static final char NUMBER_INTEGER_SEPARATOR = ',';

    /**
     * 整数和小数部分的分隔符
     */
    private static final char NUMBER_SEPARATOR = '.';

    /**
     * 十进制数字的正则表达式
     */
    private static final Pattern DECIMAL_NUMERIC_REGULAR = Pattern.compile("^[-+]?\\d+\\.?\\d*$");

    /**
     * 是否是十进制的数字
     */
    public static boolean isDecimalNumeric(final String text) {
        return DECIMAL_NUMERIC_REGULAR.matcher(text).matches();
    }

    public static boolean doubleIsDifferent(final double d1, final double d2) {
        return !doubleIsEqual(d1, d2);
    }

    public static boolean floatIsDifferent(final float f1, final float f2) {
        return !floatIsEqual(f1, f2);
    }

    public static boolean doubleIsEqual(final double d1, final double d2) {
        return isApproximateZero(d1 - d2);
    }

    public static boolean floatIsEqual(final float f1, final float f2) {
        return isApproximateZero(f1 - f2);
    }

    public static boolean isApproximateZero(final double d) {
        return Math.abs(d) <= NumberUnitsConst.DOUBLE_APPROXIMATE_ZERO;
    }

    public static boolean isApproximateZero(final float f) {
        return Math.abs(f) <= NumberUnitsConst.FLOAT_APPROXIMATE_ZERO;
    }

    /**
     * <pre>
     *     转换浮点数的精度,取小数点后的几位.
     *     convertPrecision(1.234, 0) = 1
     *     convertPrecision(1.234, 1) = 1.2
     *     convertPrecision(1.234, 2) = 1.23
     *     convertPrecision(1.234, 3) = 1.234
     *     convertPrecision(1.234, 4) = 1.2340
     * </pre>
     *
     * @param value  小数
     * @param digits 位数
     * @return 转换后的值
     */
    public static double convertPrecision(final double value, final int digits) {
        if (digits < 0) {
            throw new IllegalArgumentException("digits must be positive");
        }
        String string = formatFraction(value, digits);
        string = StringUtils.remove(string, NUMBER_INTEGER_SEPARATOR);
        if (digits == 0) {
            return Double.parseDouble(string);
        } else {
            String[] parts = StringUtils.split(string, NUMBER_SEPARATOR);
            return Double.parseDouble(parts[0] + NUMBER_SEPARATOR + StringUtils.substring(parts[1], 0, digits));
        }
    }

    /**
     * 格式化输出数字,指定小数部分的位数
     * <p>
     * formatFraction(10000, 1) = 10,000.0
     * formatFraction(10000, 2) = 10,000.00
     * formatFraction(10000.12, 1) = 10,000.1
     * formatFraction(10000.12, 2) = 10,000.12
     */
    public static String formatFraction(final Object number, final int digits) {
        return formatFraction(number, digits, digits, null);
    }

    public static String formatFraction(final Object number, final int digits, final RoundingMode roundingMode) {
        return formatFraction(number, digits, digits, roundingMode);
    }

    public static String formatFraction(final Object number, final int minDigits, final int maxDigits) {
        return formatFraction(number, minDigits, maxDigits, null);
    }

    public static String formatFraction(final Object number, final int minDigits, final int maxDigits, final RoundingMode roundingMode) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(minDigits);
        numberFormat.setMaximumFractionDigits(maxDigits);
        if (roundingMode != null) {
            numberFormat.setRoundingMode(roundingMode);
        }
        return numberFormat.format(number);
    }

    /**
     * 格式化输出数字,指定整数部分的位数.如果没有小数部分,则无小数部分,如果有小数部分,小数部分最长取3位
     * <p>
     * formatInteger(10000, 1) = 0
     * formatInteger(10000, 2) = 00
     * formatInteger(10000.12, 1) = 0.12
     * formatInteger(10000.12345, 2) = 00.123
     */
    public static String formatInteger(final Object number, final int digits) {
        return formatInteger(number, digits, digits, null);
    }

    public static String formatInteger(final Object number, final int digits, final RoundingMode roundingMode) {
        return formatInteger(number, digits, digits, roundingMode);
    }

    public static String formatInteger(final Object number, final int minDigits, final int maxDigits) {
        return formatInteger(number, minDigits, maxDigits, null);
    }

    public static String formatInteger(final Object number, final int minDigits, final int maxDigits, final RoundingMode roundingMode) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(minDigits);
        numberFormat.setMaximumIntegerDigits(maxDigits);
        if (roundingMode != null) {
            numberFormat.setRoundingMode(roundingMode);
        }
        return numberFormat.format(number);
    }


    /**
     * 格式化输出数字,指定整数部分的位数和小数部分的位数.整数部分或者小数部分长度不足最小位数,会填充'0'
     * <p>
     * formatIntegerFraction(10000, 1, 1) = 0.0
     * formatIntegerFraction(10000, 2, 2) = 00.00
     * formatIntegerFraction(10000.12, 1, 1) = 0.1
     * formatIntegerFraction(10000.12345, 3, 6) = 000.123450
     */
    public static String formatIntegerFraction(final Object number, final int intDigits, final int fraDigits) {
        return formatIntegerFraction(number, intDigits, intDigits, fraDigits, fraDigits, null);
    }

    public static String formatIntegerFraction(final Object number, final int intDigits, final int fraDigits, final RoundingMode roundingMode) {
        return formatIntegerFraction(number, intDigits, intDigits, fraDigits, fraDigits, roundingMode);
    }

    public static String formatIntegerFraction(final Object number,
                                               final int minIntDigits, final int maxIntDigits,
                                               final int minFraDigits, final int maxFraDigits) {
        return formatIntegerFraction(number, minIntDigits, maxIntDigits, minFraDigits, maxFraDigits, null);
    }

    public static String formatIntegerFraction(final Object number,
                                               final int minIntDigits, final int maxIntDigits,
                                               final int minFraDigits, final int maxFraDigits,
                                               final RoundingMode roundingMode) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(minIntDigits);
        numberFormat.setMaximumIntegerDigits(maxIntDigits);
        numberFormat.setMinimumFractionDigits(minFraDigits);
        numberFormat.setMaximumFractionDigits(maxFraDigits);
        if (roundingMode != null) {
            numberFormat.setRoundingMode(roundingMode);
        }
        return numberFormat.format(number);
    }

}
