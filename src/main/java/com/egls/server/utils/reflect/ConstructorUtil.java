package com.egls.server.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供一些操作构造器的工具方法
 *
 * @author mayer - [Created on 2018-08-09 22:36]
 */
public final class ConstructorUtil {

    public static List<Constructor<?>> getConstructors(final Class<?> clazz) {
        List<Constructor<?>> constructors = new ArrayList<>();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            constructor.setAccessible(true);
            constructors.add(constructor);
        }
        return constructors;
    }

    public static Constructor getNoneParamConstructor(final Class<?> clazz) {
        for (Constructor<?> constructor : getConstructors(clazz)) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }

    public static Constructor<?> getNoneParamConstructor(final String clsName) throws ClassNotFoundException {
        return getNoneParamConstructor(Class.forName(clsName));
    }

    @SuppressWarnings("unchecked")
    public static <T> T newObjectWithNoneParam(final Class<?> clazz)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clz = clazz;
        while (clz != null && clz != Object.class) {
            Constructor<?>[] constructors = clz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance();
                }
            }
            clz = clz.getSuperclass();
        }
        throw new IllegalAccessException(String.format("can't found none param constructor. (%s)", clazz.getName()));
    }

    public static boolean containsNoneParamConstructor(final Class<?> clazz) {
        return getNoneParamConstructor(clazz) != null;
    }

}
