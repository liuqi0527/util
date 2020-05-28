package com.egls.server.utils.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供一些操作函数对象的工具方法
 *
 * @author mayer - [Created on 2018-08-09 22:39]
 */
public final class MethodUtil {

    public static List<Method> getAllMethods(final Class<?> clazz) {
        List<Method> methodsIncludeStatic = getAllMethodsIncludeStatic(clazz);
        return methodsIncludeStatic.stream().filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
    }

    public static List<Method> getAllMethodsIncludeStatic(final Class<?> cls) {
        Class<?> clazz = cls;
        List<Method> methods = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (!Modifier.isAbstract(method.getModifiers())) {
                    method.setAccessible(true);
                    methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

}
