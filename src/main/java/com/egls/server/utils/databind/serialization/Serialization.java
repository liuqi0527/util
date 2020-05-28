package com.egls.server.utils.databind.serialization;

import java.util.ArrayList;
import java.util.function.Function;

import com.egls.server.utils.array.ArrayUtil;
import com.egls.server.utils.exception.IllegalFormatException;
import com.egls.server.utils.function.Caster;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 包内可见的反序列化的具体方法
 *
 * @author mayer - [Created on 2018-08-28 20:03]
 */
final class Serialization {

    static final String NULL = "NULL";
    static final char SPLIT_CHAR = ',';

    static final char ESCAPE_CHAR = '\\';

    static final char BRACKET_STRING_CHAR = '"';

    static final char BRACKET_LEFT_ARRAY_CHAR = '[';
    static final char BRACKET_RIGHT_ARRAY_CHAR = ']';

    static final char BRACKET_LEFT_SERIALIZED_OBJECT_CHAR = '{';
    static final char BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR = '}';

    static boolean isNull(final String string) {
        return string == null || StringUtils.equals(string, Serialization.NULL);
    }

    static void ensureArrayString(final String string) {
        if (!Serialization.isArrayString(string)) {
            throw new IllegalFormatException(String.format("not array string. %s", string));
        }
    }

    static boolean isArrayString(final String string) {
        return !StringUtils.isBlank(string)
                && string.charAt(0) == BRACKET_LEFT_ARRAY_CHAR
                && string.charAt(string.length() - 1) == BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeBytePrimitiveArray(final byte[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeBooleanPrimitiveArray(final boolean[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeCharPrimitiveArray(final char[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeShortPrimitiveArray(final short[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeIntPrimitiveArray(final int[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeLongPrimitiveArray(final long[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeFloatPrimitiveArray(final float[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static String serializeDoublePrimitiveArray(final double[] array) {
        return array == null ? NULL : BRACKET_LEFT_ARRAY_CHAR + StringUtils.join(ArrayUtils.toObject(array), SPLIT_CHAR) + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static <T> String serializeObjectArray(final T[] array, final Caster<T, String> caster) {
        if (array == null) {
            return NULL;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i] == null ? NULL : caster.cast(array[i]));
            if (i < array.length - 1) {
                stringBuilder.append(SPLIT_CHAR);
            }
        }
        return BRACKET_LEFT_ARRAY_CHAR + stringBuilder.toString() + BRACKET_RIGHT_ARRAY_CHAR;
    }

    static byte[] deserializeBytePrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toBytePrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    static boolean[] deserializeBooleanPrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toBooleanPrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    static char[] deserializeCharPrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toCharPrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    static short[] deserializeShortPrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toShortPrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    static int[] deserializeIntPrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toIntPrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    static long[] deserializeLongPrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toLongPrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    static float[] deserializeFloatPrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toFloatPrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    static double[] deserializeDoublePrimitiveArray(final String string) {
        if (isNull(string)) {
            return null;
        }
        ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        return ArrayUtil.toDoublePrimitiveArray(StringUtils.split(operationString, SPLIT_CHAR));
    }

    /**
     * 反序列化不需要转义的数组
     */
    static <T> T[] deserializeObjectArray(final String string, final Function<Integer, T[]> arraySupplier, final Caster<String, T> caster) {
        if (isNull(string)) {
            return null;
        }
        Serialization.ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        final String[] strings = StringUtils.split(operationString, SPLIT_CHAR);
        final T[] array = arraySupplier.apply(strings.length);
        for (int i = 0; i < array.length; i++) {
            array[i] = Serialization.isNull(strings[i]) ? null : caster.cast(strings[i]);
        }
        return array;
    }

    /**
     * 反序列化需要转义的数组
     */
    static <T> T[] deserializeEscapedObjectArray(final String string, final Function<Integer, T[]> arraySupplier, final Caster<String, T> caster) {
        if (isNull(string)) {
            return null;
        }
        Serialization.ensureArrayString(string);
        final String operationString = StringUtils.substring(string, 1, string.length() - 1);
        final ArrayList<String> stringList = new ArrayList<>();
        final Serialization.CharacterReader characterReader = new Serialization.CharacterReader(operationString.toCharArray());
        NextStringResult nextStringResult;
        while ((nextStringResult = Serialization.nextString(characterReader)) != null) {
            stringList.add(nextStringResult.result);
        }
        final T[] array = arraySupplier.apply(stringList.size());
        for (int i = 0; i < array.length; i++) {
            array[i] = Serialization.isNull(stringList.get(i)) ? null : caster.cast(stringList.get(i));
        }
        return array;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    static final class CharacterReader {

        private int index = 0;

        private final char[] chars;

        CharacterReader(final char[] chars) {
            this.chars = chars;
        }

        private Character nextCharacter() {
            return hasNext() ? chars[index++] : null;
        }

        private boolean hasNext() {
            return index < chars.length;
        }

    }

    /**
     * 主要是封装result为null的情况.
     */
    static class NextStringResult {

        final String result;

        private NextStringResult(String result) {
            this.result = result;
        }

    }

    static String escapeString(final String string) {
        char[] chars = string.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BRACKET_STRING_CHAR);
        for (char aChar : chars) {
            if (isEscapeChar(aChar)) {
                stringBuilder.append(ESCAPE_CHAR).append(escapeChar(aChar));
            } else {
                stringBuilder.append(aChar);
            }
        }
        stringBuilder.append(BRACKET_STRING_CHAR);
        return stringBuilder.toString();
    }

    static NextStringResult nextString(final CharacterReader characterReader) {
        Character firstCharacter = characterReader.nextCharacter();
        if (firstCharacter == null) {
            return null;
        }
        if (firstCharacter == BRACKET_LEFT_ARRAY_CHAR) {
            //数组
            return new NextStringResult(nextBracketString(BRACKET_LEFT_ARRAY_CHAR, BRACKET_RIGHT_ARRAY_CHAR, firstCharacter, characterReader));
        } else if (firstCharacter == BRACKET_LEFT_SERIALIZED_OBJECT_CHAR) {
            //序列化对象
            return new NextStringResult(nextBracketString(BRACKET_LEFT_SERIALIZED_OBJECT_CHAR, BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR, firstCharacter, characterReader));
        } else if (firstCharacter == BRACKET_STRING_CHAR) {
            //字符串
            StringBuilder stringBuilder = new StringBuilder();
            Character curCharacter;
            while ((curCharacter = characterReader.nextCharacter()) != null) {
                if (curCharacter == ESCAPE_CHAR) {
                    //处理转义
                    Character nextCharacter = characterReader.nextCharacter();
                    if (nextCharacter != null) {
                        stringBuilder.append(unescapeChar(nextCharacter));
                    }
                } else {
                    if (curCharacter == BRACKET_STRING_CHAR) {
                        //读出SPLIT_CHAR
                        characterReader.nextCharacter();
                        break;
                    } else {
                        stringBuilder.append(curCharacter);
                    }
                }
            }
            return new NextStringResult(stringBuilder.toString());
        } else {
            //8种基本类型和NULL
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(firstCharacter);
            Character curCharacter;
            while ((curCharacter = characterReader.nextCharacter()) != null) {
                stringBuilder.append(curCharacter);
                if (curCharacter == SPLIT_CHAR) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    break;
                }
            }
            final String tempResult = stringBuilder.toString();
            return new NextStringResult(StringUtils.equals(NULL, tempResult) ? null : tempResult);
        }
    }

    private static String nextBracketString(final char leftBracketChar, final char rightBracketChar, final Character firstCharacter, final CharacterReader characterReader) {
        //这里不处理转义,完全按照括号匹配.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstCharacter);
        int count = 1;
        Character lastCharacter = firstCharacter;
        Character curCharacter;
        while ((curCharacter = characterReader.nextCharacter()) != null) {
            stringBuilder.append(curCharacter);
            if (lastCharacter != ESCAPE_CHAR) {
                if (curCharacter == leftBracketChar) {
                    count++;
                }
                if (curCharacter == rightBracketChar) {
                    count--;
                }
            }
            lastCharacter = curCharacter;
            if (count == 0) {
                //读出SPLIT_CHAR
                characterReader.nextCharacter();
                break;
            }
        }
        return stringBuilder.toString();
    }

    private static boolean isEscapeChar(final char aChar) {
        return aChar == '\b'
                || aChar == '\t'
                || aChar == '\f'
                || aChar == '\r'
                || aChar == '\n'
                || aChar == ESCAPE_CHAR
                || aChar == BRACKET_STRING_CHAR
                || aChar == BRACKET_LEFT_ARRAY_CHAR
                || aChar == BRACKET_RIGHT_ARRAY_CHAR
                || aChar == BRACKET_LEFT_SERIALIZED_OBJECT_CHAR
                || aChar == BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR;

    }

    private static char escapeChar(final char aChar) {
        switch (aChar) {
            case '\b':
                return 'b';
            case '\t':
                return 't';
            case '\f':
                return 'f';
            case '\r':
                return 'r';
            case '\n':
                return 'n';
            case ESCAPE_CHAR:
                return ESCAPE_CHAR;
            case BRACKET_STRING_CHAR:
                return BRACKET_STRING_CHAR;
            case BRACKET_LEFT_ARRAY_CHAR:
                return BRACKET_LEFT_ARRAY_CHAR;
            case BRACKET_RIGHT_ARRAY_CHAR:
                return BRACKET_RIGHT_ARRAY_CHAR;
            case BRACKET_LEFT_SERIALIZED_OBJECT_CHAR:
                return BRACKET_LEFT_SERIALIZED_OBJECT_CHAR;
            case BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR:
                return BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR;
            default:
                return aChar;
        }
    }

    private static char unescapeChar(final char aChar) {
        switch (aChar) {
            case 'b':
                return '\b';
            case 't':
                return '\t';
            case 'f':
                return '\f';
            case 'r':
                return '\r';
            case 'n':
                return '\n';
            case ESCAPE_CHAR:
                return ESCAPE_CHAR;
            case BRACKET_STRING_CHAR:
                return BRACKET_STRING_CHAR;
            case BRACKET_LEFT_ARRAY_CHAR:
                return BRACKET_LEFT_ARRAY_CHAR;
            case BRACKET_RIGHT_ARRAY_CHAR:
                return BRACKET_RIGHT_ARRAY_CHAR;
            case BRACKET_LEFT_SERIALIZED_OBJECT_CHAR:
                return BRACKET_LEFT_SERIALIZED_OBJECT_CHAR;
            case BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR:
                return BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR;
            default:
                return aChar;
        }
    }

}
