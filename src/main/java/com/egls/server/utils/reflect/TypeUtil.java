package com.egls.server.utils.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 提供一些类型的工具方法
 *
 * @author mayer - [Created on 2018-08-09 22:40]
 * @see com.egls.server.utils.clazz.ClassUtil
 */
public final class TypeUtil {

    public static boolean isBytePrimitive(final Class<?> clazz) {
        return clazz == Byte.TYPE;
    }

    public static boolean isBooleanPrimitive(final Class<?> clazz) {
        return clazz == Boolean.TYPE;
    }

    public static boolean isCharPrimitive(final Class<?> clazz) {
        return clazz == Character.TYPE;
    }

    public static boolean isShortPrimitive(final Class<?> clazz) {
        return clazz == Short.TYPE;
    }

    public static boolean isIntPrimitive(final Class<?> clazz) {
        return clazz == Integer.TYPE;
    }

    public static boolean isLongPrimitive(final Class<?> clazz) {
        return clazz == Long.TYPE;
    }

    public static boolean isFloatPrimitive(final Class<?> clazz) {
        return clazz == Float.TYPE;
    }

    public static boolean isDoublePrimitive(final Class<?> clazz) {
        return clazz == Double.TYPE;
    }

    public static boolean isVoidPrimitive(final Class<?> clazz) {
        return clazz == Void.TYPE;
    }

    public static boolean isByteObject(final Class<?> clazz) {
        return Byte.class.isAssignableFrom(clazz);
    }

    public static boolean isBooleanObject(final Class<?> clazz) {
        return Boolean.class.isAssignableFrom(clazz);
    }

    public static boolean isCharObject(final Class<?> clazz) {
        return Character.class.isAssignableFrom(clazz);
    }

    public static boolean isShortObject(final Class<?> clazz) {
        return Short.class.isAssignableFrom(clazz);
    }

    public static boolean isIntObject(final Class<?> clazz) {
        return Integer.class.isAssignableFrom(clazz);
    }

    public static boolean isLongObject(final Class<?> clazz) {
        return Long.class.isAssignableFrom(clazz);
    }

    public static boolean isFloatObject(final Class<?> clazz) {
        return Float.class.isAssignableFrom(clazz);
    }

    public static boolean isDoubleObject(final Class<?> clazz) {
        return Double.class.isAssignableFrom(clazz);
    }

    public static boolean isByte(final Class<?> clazz) {
        return isBytePrimitive(clazz) || isByteObject(clazz);
    }

    public static boolean isBoolean(final Class<?> clazz) {
        return isBooleanPrimitive(clazz) || isBooleanObject(clazz);
    }

    public static boolean isCharacter(final Class<?> clazz) {
        return isCharPrimitive(clazz) || isCharObject(clazz);
    }

    public static boolean isShort(final Class<?> clazz) {
        return isShortPrimitive(clazz) || isShortObject(clazz);
    }

    public static boolean isInteger(final Class<?> clazz) {
        return isIntPrimitive(clazz) || isIntObject(clazz);
    }

    public static boolean isLong(final Class<?> clazz) {
        return isLongPrimitive(clazz) || isLongObject(clazz);
    }

    public static boolean isFloat(final Class<?> clazz) {
        return isFloatPrimitive(clazz) || isFloatObject(clazz);
    }

    public static boolean isDouble(final Class<?> clazz) {
        return isDoublePrimitive(clazz) || isDoubleObject(clazz);
    }

    public static boolean isString(final Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public static boolean isEnum(final Class<?> clazz) {
        return clazz.isEnum();
    }

    /**
     * 是否是原生8种类型
     */
    public static boolean isPrimitiveType(final Class<?> clazz) {
        return isBytePrimitive(clazz)
                || isBooleanPrimitive(clazz)
                || isCharPrimitive(clazz)
                || isShortPrimitive(clazz)
                || isIntPrimitive(clazz)
                || isLongPrimitive(clazz)
                || isFloatPrimitive(clazz)
                || isDoublePrimitive(clazz)
                || isVoidPrimitive(clazz);
    }

    /**
     * 是否是原生8种类型的包装类型
     */
    public static boolean isPrimitiveTypeWrapper(final Class<?> clazz) {
        return isByteObject(clazz)
                || isBooleanObject(clazz)
                || isCharObject(clazz)
                || isShortObject(clazz)
                || isIntObject(clazz)
                || isLongObject(clazz)
                || isFloatObject(clazz)
                || isDoubleObject(clazz);
    }

    /**
     * 是否是基础类型,包括8种原生类型,8种原生类型的包装类型,字符串类型,枚举类型
     */
    public static boolean isBasicType(final Class<?> clazz) {
        return isPrimitiveType(clazz) || isPrimitiveTypeWrapper(clazz) || isString(clazz) || isEnum(clazz);
    }

    public static void setByte(final Object object, final Field field, final byte value) throws IllegalAccessException {
        if (isBytePrimitive(field.getType())) {
            field.setByte(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setBoolean(final Object object, final Field field, final boolean value) throws IllegalAccessException {
        if (isBooleanPrimitive(field.getType())) {
            field.setBoolean(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setCharacter(final Object object, final Field field, final char value) throws IllegalAccessException {
        if (isCharPrimitive(field.getType())) {
            field.setChar(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setShort(final Object object, final Field field, final short value) throws IllegalAccessException {
        if (isShortPrimitive(field.getType())) {
            field.setShort(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setInteger(final Object object, final Field field, final int value) throws IllegalAccessException {
        if (isIntPrimitive(field.getType())) {
            field.setInt(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setLong(final Object object, final Field field, final long value) throws IllegalAccessException {
        if (isLongPrimitive(field.getType())) {
            field.setLong(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setFloat(final Object object, final Field field, final float value) throws IllegalAccessException {
        if (isFloatPrimitive(field.getType())) {
            field.setFloat(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setDouble(final Object object, final Field field, final double value) throws IllegalAccessException {
        if (isDoublePrimitive(field.getType())) {
            field.setDouble(object, value);
        } else {
            field.set(object, value);
        }
    }

    public static void setString(final Object object, final Field field, final String value) throws IllegalAccessException {
        setObject(object, field, value);
    }

    @SuppressWarnings("unchecked")
    public static void setEnum(Object object, Field field, String value) throws IllegalAccessException {
        setObject(object, field, StringUtils.isBlank(value) ? null : Enum.valueOf((Class) field.getType(), value));
    }

    public static void setObject(final Object object, final Field field, final Object value) throws IllegalAccessException {
        field.set(object, value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean isArray(final Class<?> clazz) {
        return clazz.isArray();
    }

    public static boolean isBytePrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isBytePrimitive(clazz.getComponentType());
    }

    public static boolean isBooleanPrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isBooleanPrimitive(clazz.getComponentType());
    }

    public static boolean isCharPrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isCharPrimitive(clazz.getComponentType());
    }

    public static boolean isShortPrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isShortPrimitive(clazz.getComponentType());
    }

    public static boolean isIntPrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isIntPrimitive(clazz.getComponentType());
    }

    public static boolean isLongPrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isLongPrimitive(clazz.getComponentType());
    }

    public static boolean isFloatPrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isFloatPrimitive(clazz.getComponentType());
    }

    public static boolean isDoublePrimitiveArray(final Class<?> clazz) {
        return isArray(clazz) && isDoublePrimitive(clazz.getComponentType());
    }

    public static boolean isByteObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isByteObject(clazz.getComponentType());
    }

    public static boolean isBooleanObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isBooleanObject(clazz.getComponentType());
    }

    public static boolean isCharObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isCharObject(clazz.getComponentType());
    }

    public static boolean isShortObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isShortObject(clazz.getComponentType());
    }

    public static boolean isIntObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isIntObject(clazz.getComponentType());
    }

    public static boolean isLongObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isLongObject(clazz.getComponentType());
    }

    public static boolean isFloatObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isFloatObject(clazz.getComponentType());
    }

    public static boolean isDoubleObjectArray(final Class<?> clazz) {
        return isArray(clazz) && isDoubleObject(clazz.getComponentType());
    }

    public static boolean isByteArray(final Class<?> clazz) {
        return isBytePrimitiveArray(clazz) || isByteObjectArray(clazz);
    }

    public static boolean isBooleanArray(final Class<?> clazz) {
        return isBooleanPrimitiveArray(clazz) || isBooleanObjectArray(clazz);
    }

    public static boolean isCharacterArray(final Class<?> clazz) {
        return isCharPrimitiveArray(clazz) || isCharObjectArray(clazz);
    }

    public static boolean isShortArray(final Class<?> clazz) {
        return isShortPrimitiveArray(clazz) || isShortObjectArray(clazz);
    }

    public static boolean isIntegerArray(final Class<?> clazz) {
        return isIntPrimitiveArray(clazz) || isIntObjectArray(clazz);
    }

    public static boolean isLongArray(final Class<?> clazz) {
        return isLongPrimitiveArray(clazz) || isLongObjectArray(clazz);
    }

    public static boolean isFloatArray(final Class<?> clazz) {
        return isFloatPrimitiveArray(clazz) || isFloatObjectArray(clazz);
    }

    public static boolean isDoubleArray(final Class<?> clazz) {
        return isDoublePrimitiveArray(clazz) || isDoubleObjectArray(clazz);
    }

    public static boolean isStringArray(final Class<?> clazz) {
        return isArray(clazz) && String.class.isAssignableFrom(clazz.getComponentType());
    }

    public static boolean isEnumArray(final Class<?> clazz) {
        return clazz.isArray() && clazz.getComponentType().isEnum();
    }

    /**
     * 是否是原生8种类型的数组
     */
    public static boolean isPrimitiveTypeArray(final Class<?> clazz) {
        return isBytePrimitiveArray(clazz)
                || isBooleanPrimitiveArray(clazz)
                || isCharPrimitiveArray(clazz)
                || isShortPrimitiveArray(clazz)
                || isIntPrimitiveArray(clazz)
                || isLongPrimitiveArray(clazz)
                || isFloatPrimitiveArray(clazz)
                || isDoublePrimitiveArray(clazz);
    }

    /**
     * 是否是原生8种类型的包装类型的数组
     */
    public static boolean isPrimitiveTypeWrapperArray(final Class<?> clazz) {
        return isByteObjectArray(clazz)
                || isBooleanObjectArray(clazz)
                || isCharObjectArray(clazz)
                || isShortObjectArray(clazz)
                || isIntObjectArray(clazz)
                || isLongObjectArray(clazz)
                || isFloatObjectArray(clazz)
                || isDoubleObjectArray(clazz);
    }

    /**
     * 是否是基础类型的数组,包括8种原生类型,8种原生类型的包装类型,字符串类型,枚举类型
     */
    public static boolean isBasicTypeArray(final Class<?> clazz) {
        return isPrimitiveTypeArray(clazz)
                || isPrimitiveTypeWrapperArray(clazz)
                || isStringArray(clazz)
                || isEnumArray(clazz);
    }

    public static void setByteArray(final Object object, final Field field, final byte[] values) throws IllegalAccessException {
        if (isBytePrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setBooleanArray(final Object object, final Field field, final boolean[] values) throws IllegalAccessException {
        if (isBooleanPrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setCharacterArray(final Object object, final Field field, final char[] values) throws IllegalAccessException {
        if (isCharPrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setShortArray(final Object object, final Field field, final short[] values) throws IllegalAccessException {
        if (isShortPrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setIntegerArray(final Object object, final Field field, final int[] values) throws IllegalAccessException {
        if (isIntPrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setLongArray(final Object object, final Field field, final long[] values) throws IllegalAccessException {
        if (isLongPrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setFloatArray(final Object object, final Field field, final float[] values) throws IllegalAccessException {
        if (isFloatPrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setDoubleArray(final Object object, final Field field, final double[] values) throws IllegalAccessException {
        if (isDoublePrimitiveArray(field.getType())) {
            field.set(object, values);
        } else {
            field.set(object, ArrayUtils.toObject(values));
        }
    }

    public static void setStringArray(final Object object, final Field field, final String[] values) throws IllegalAccessException {
        setObjectArray(object, field, values);
    }

    @SuppressWarnings("unchecked")
    public static void setEnumArray(final Object object, final Field field, final String[] values) throws IllegalAccessException {
        final Object array = Array.newInstance(field.getType().getComponentType(), values.length);
        for (int i = 0; i < values.length; i++) {
            if (StringUtils.isNotBlank(values[i])) {
                Array.set(array, i, Enum.valueOf((Class) field.getType().getComponentType(), values[i]));
            }
        }
        setObject(object, field, array);
    }

    public static <T> void setObjectArray(final Object object, final Field field, final T[] values) throws IllegalAccessException {
        setObject(object, field, values);
    }

}
