package com.egls.server.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

import com.egls.server.utils.reflect.FieldUtil;

import sun.misc.Unsafe;

/**
 * 提供一些不推荐的工具方法
 * 仅限用于jdk8_64bits,其他版本jdk不保证正确性,有效性
 *
 * @author mayer - [Created on 2018-08-09 22:34]
 */
public final class UnsafeUtil {

    /**
     * <pre>
     * 64_bits系统jvm的对象头部的大小是8
     * 32_bits系统jvm的对象头部的大小是8
     * </pre>
     */
    private static final int OBJECT_HEADER_OFFSET = 8;
    /**
     * <pre>
     * 64_bits系统jvm的最小对象的大小是16
     * 32_bits系统jvm的最小对象的大小是8
     * 也就是说,对象没有任何属性的时候,占用最小内存.
     * </pre>
     */
    private static final int OBJECT_MINIMUM_OFFSET = 16;
    /**
     * <pre>
     * 不管是数组内或者是对象内,最小的一个对齐扩充单位,比如2个引用才会占8字节,但是只有一个引用也占8字节,因为这是最小对齐粒度了.
     * 64_bits系统jvm的最小粒度的大小是8
     * 32_bits系统jvm的最小粒度的大小是8
     * </pre>
     */
    private static final int ALIGNMENT = 8;

    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }

    /**
     * <pre>
     * 本方法仅仅提供一个对象的所[涉及]的全部内存的大小.
     * 无论是其中嵌套声明多少层的对象.或者数组,或者基础类型.
     * 单位byte
     * </pre>
     */
    public static long fullSizeOf(final Object object) throws NoSuchFieldException, IllegalAccessException {
        if (object == null) {
            return 0L;
        }
        Class<?> clazz = object.getClass();
        if (byte[].class.isAssignableFrom(clazz)
                || boolean[].class.isAssignableFrom(clazz)
                || char[].class.isAssignableFrom(clazz)
                || short[].class.isAssignableFrom(clazz)
                || int[].class.isAssignableFrom(clazz)
                || long[].class.isAssignableFrom(clazz)
                || float[].class.isAssignableFrom(clazz)
                || double[].class.isAssignableFrom(clazz)) {
            return sizeOf(object);
        } else if (clazz.isArray()) {
            long result = sizeOf(object);
            for (int i = 0; i < Array.getLength(object); i++) {
                result += fullSizeOf(Array.get(object, i));
            }
            return result;
        } else {
            List<Field> fields = FieldUtil.getAllFields(clazz);
            long result = sizeOf(object);
            for (Field field : fields) {
                switch (field.getType().getName()) {
                    case "byte":
                    case "boolean":
                    case "char":
                    case "short":
                    case "int":
                    case "long":
                    case "float":
                    case "double":
                        break;
                    default:
                        result += fullSizeOf(field.get(object));
                        break;
                }
            }
            return result;
        }
    }

    /**
     * <pre>
     * 本方法仅仅提供一个对象的基础内存占用的情况,比如当你定义了某种对象的数组,比如Integer[]数组
     * 仅仅会提供这个数组中的Integer索引内存.并不会计算对象内部的属性总体的内存.
     * 这跟{@code jmap -histo pid} 有些类似,统计的仅仅是单层对象的内存
     * 如果想知道全部涉及内存,使用{@link #fullSizeOf(Object)}
     * 单位byte
     * </pre>
     */
    public static long sizeOf(final Object object) throws NoSuchFieldException, IllegalAccessException {
        if (object == null) {
            return 0L;
        }
        return object.getClass().isArray() ? sizeOfArray(object) : sizeOfObject(object);
    }

    private static long sizeOfArray(final Object object) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Unsafe unsafe = getUnsafe();
        int arrayBaseOffset = unsafe.arrayBaseOffset(clazz);
        int arrayIndexScale = unsafe.arrayIndexScale(clazz);
        int result = arrayBaseOffset + (arrayIndexScale * Array.getLength(object));
        int mod = result % ALIGNMENT;
        return mod == 0 ? result : result + (ALIGNMENT - mod);
    }

    private static long sizeOfObject(final Object object) throws NoSuchFieldException, IllegalAccessException {
        Unsafe unsafe = getUnsafe();
        List<Field> fields = FieldUtil.getAllFields(object.getClass());

        long result = 0;
        for (Field field : fields) {
            long offset = unsafe.objectFieldOffset(field);
            if (offset > result) {
                result = offset;
            }
        }

        if (result == 0) {
            return OBJECT_MINIMUM_OFFSET;
        } else {
            long exceptHeader = result - OBJECT_HEADER_OFFSET;
            long mod = exceptHeader % ALIGNMENT;
            if (mod == 0) {
                exceptHeader += ALIGNMENT;
            } else {
                exceptHeader += (ALIGNMENT - mod);
            }
            return exceptHeader + OBJECT_HEADER_OFFSET;
        }
    }

    public static Thread[] getAllThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        while ((group = group.getParent()) != null) {
            topGroup = group;
        }
        Thread[] slackList = new Thread[topGroup.activeCount() << 2];
        int actualSize = topGroup.enumerate(slackList);
        Thread[] actualList = new Thread[actualSize];
        System.arraycopy(slackList, 0, actualList, 0, actualSize);
        return actualList;
    }

}
