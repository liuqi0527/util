package com.egls.server.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供一些操作属性的工具方法
 *
 * @author mayer - [Created on 2018-08-09 22:37]
 */
public final class FieldUtil {

    public static List<Field> getAllFields(final Class<?> clazz) {
        List<Field> fieldsIncludeStatic = getAllFieldsIncludeStatic(clazz);
        return fieldsIncludeStatic.stream().filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
    }

    public static List<Field> getAllFieldsIncludeStatic(final Class<?> cls) {
        Class<?> clazz = cls;
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
