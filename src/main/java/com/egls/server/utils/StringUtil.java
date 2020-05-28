package com.egls.server.utils;

import com.egls.server.utils.array.ArraySearchUtil;
import com.egls.server.utils.array.ArrayUtil;
import com.egls.server.utils.exception.IllegalFormatException;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 提供一些字符串的工具方法
 *
 * @author mayer - [Created on 2018-08-09 20:36]
 */
public final class StringUtil {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final String UNIX_LINE_SEPARATOR = "\n";

    private static final String WINDOWS_LINE_SEPARATOR = "\r\n";

    public static String binaryToHex(final byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    public static byte[] hexToBinary(final String hexString) {
        return DatatypeConverter.parseHexBinary(hexString);
    }

    public static boolean toBoolean(final String string) {
        return toBooleanObject(string);
    }

    public static Boolean toBooleanObject(final String string) {
        Boolean b = BooleanUtils.toBooleanObject(string);
        if (b == null) {
            throw new IllegalFormatException(String.format("only support [true:false] [on:off] [yes:no]. not support -> (%s)", string));
        } else {
            return b;
        }
    }

    /**
     * @see #allUpperCase(String)
     */
    private static boolean allLowerCase(final String string) {
        for (char character : string.toCharArray()) {
            if (Character.isUpperCase(character)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether or not the each letter in the specified String is upper case (i.e. digits etc are ignored).
     *
     * @param string The string.
     * @return {@code true} if no letters in the specified String are lower case, otherwise {@code false}.
     */
    private static boolean allUpperCase(final String string) {
        for (char character : string.toCharArray()) {
            if (Character.isLowerCase(character)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method apply to convey a brief content of long string.
     * If the length of str is longer than length, there will be "..." after return string.
     * Otherwise, the suffix will not be added.
     *
     * @param str    Source string.
     * @param length Excepted max length of return string.
     * @return Son of str meeting the length require.
     */
    public static String cutLongString(final String str, final int length) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length) + "...";
    }

    /**
     * This method will insert parameters into str refer to "$index" one by one.
     * The number of "$" must equals to params' length.
     *
     * @param string Source string reserved some space signed by "$index" for parameters.
     * @param params Parameters waiting to be inserted to str.
     * @return A new string mix str and parameters.
     */
    public static String fillVars(final String string, final Object... params) {
        String result = string;
        if (!StringUtils.isBlank(result)) {
            for (int i = (params.length - 1); i >= 0; i--) {
                result = StringUtils.replace(result, "$" + i, params[i].toString());
            }
        }
        return result;
    }

    /**
     * 是否包含增补的Unicode的字符.就是一个char无法表示的字符.
     *
     * @param str string.
     * @return Return true if str doesn't contain supplementary character.
     */
    public static boolean containsSupplementaryCharacter(final String str) {
        return str != null && str.length() != str.codePointCount(0, str.length());
    }

    public static String getLineSeparator() {
        return LINE_SEPARATOR;
    }

    public static String getUnixLineSeparator() {
        return UNIX_LINE_SEPARATOR;
    }

    public static String getWindowsLineSeparator() {
        return WINDOWS_LINE_SEPARATOR;
    }

    /**
     * 截取字符串的某两个符号中间的字符串
     * 注意要点:
     * <pre>
     *     1,当多个区间符号递归包含时,仅去除最外层.
     *     for example:
     *     input  : String string = "{{a}{b}{c}}{1}{2}{3}";
     *     output : [{a}{b}{c},1,2,3]
     * </pre>
     *
     * @param operationString 操作字符串
     * @param leftBracket     左
     * @param rightBracket    右
     * @return 返回数组
     */
    public static String[] subStringsBetween(final String operationString, final String leftBracket, final String rightBracket) {
        if (Objects.isNull(operationString) || Objects.isNull(leftBracket) || Objects.isNull(rightBracket)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList<String> tmpArray = new ArrayList<>();
        char[] array = operationString.toCharArray();
        char[] leftArray = leftBracket.toCharArray();
        char[] rightArray = rightBracket.toCharArray();
        int sum = 0, position = 0, index = 0;
        while (index < array.length) {
            if (ArraySearchUtil.equals(array, index, leftArray, 0, leftArray.length) && sum++ == 0) {
                position = index;
            }
            if (ArraySearchUtil.equals(array, index, rightArray, 0, rightArray.length) && --sum == 0) {
                tmpArray.add(new String(ArrayUtil.copyOfRange(array, position + leftArray.length, index)));
            }
            index++;
        }
        return tmpArray.toArray(new String[]{});
    }

    /**
     * 截取字符串的某两个符号中间的字符串
     * 注意要点:
     * <pre>
     *     1,当多个区间符号递归包含时,仅去除最外层.
     *     for example:
     *     input  : String string = "{{a}{b}{c}}{1}{2}{3}";
     *     output : [{a}{b}{c},1,2,3]
     * </pre>
     *
     * @param operationString 操作字符串
     * @param leftBracket     左
     * @param rightBracket    右
     * @return 返回数组
     */
    public static String[] subStringsBetween(final String operationString, final char leftBracket, final char rightBracket) {
        if (Objects.isNull(operationString)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList<String> tmpArray = new ArrayList<>();
        char[] array = operationString.toCharArray();
        int sum = 0, position = 0, index = 0;
        while (index < array.length) {
            if (array[index] == leftBracket && sum++ == 0) {
                position = index;
            }
            if (array[index] == rightBracket && --sum == 0) {
                tmpArray.add(new String(ArrayUtil.copyOfRange(array, position + 1, index)));
            }
            index++;
        }
        return tmpArray.toArray(new String[]{});
    }

    public static List<String> splitToList(String string, char separator) {
        return Splitter.on(separator).trimResults().omitEmptyStrings().splitToList(string);
    }

    public static List<String> splitToList(String string, String separator) {
        if (StringUtils.isBlank(string)) {
            return Collections.emptyList();
        }
        return Splitter.on(separator).trimResults().omitEmptyStrings().splitToList(string);
    }

}
