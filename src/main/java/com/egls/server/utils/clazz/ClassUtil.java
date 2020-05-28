package com.egls.server.utils.clazz;

import com.egls.server.utils.file.FileType;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 提供一些工具方法
 *
 * @author mayer - [Created on 2018-08-27 21:41]
 * @see org.apache.commons.lang3.ClassUtils
 * @see com.egls.server.utils.reflect.TypeUtil
 * @see com.fasterxml.jackson.databind.util.ClassUtil
 */
public final class ClassUtil {

    /**
     * Helper method that checks if given class is a concrete one;
     * that is, not an interface or abstract class.
     */
    public static boolean isConcrete(Class<?> type) {
        int mod = type.getModifiers();
        return !Modifier.isInterface(mod) && !Modifier.isAbstract(mod);
    }

    public static boolean isConcrete(Member member) {
        int mod = member.getModifiers();
        return !Modifier.isInterface(mod) && !Modifier.isAbstract(mod);
    }

    public static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static boolean isArray(Class<?> type) {
        return type.isArray();
    }

    public static boolean isCollectionMapOrArray(Class<?> type) {
        return isCollection(type) || isMap(type) || isArray(type);
    }

    public static boolean isPublicClass(Class<?> type) {
        int mod = type.getModifiers();
        return Modifier.isPublic(mod);
    }

    public static boolean isBogusClass(Class<?> cls) {
        return (cls == Void.class || cls == Void.TYPE);
    }

    public static boolean isInnerClass(final Class<?> cls) {
        return cls != null && cls.getEnclosingClass() != null;
    }

    public static boolean isNonStaticInnerClass(Class<?> cls) {
        return !Modifier.isStatic(cls.getModifiers()) && (getEnclosingClass(cls) != null);
    }

    public static boolean isObjectOrPrimitive(Class<?> cls) {
        return (cls == Object.class) || cls.isPrimitive();
    }

    public static Class<?> getEnclosingClass(Class<?> cls) {
        // Caching does not seem worthwhile, as per profiling
        return isObjectOrPrimitive(cls) ? null : cls.getEnclosingClass();
    }

    /**
     * 获取一个包下的所有类,包括内部类
     *
     * @param domain 包名
     * @return 找到的类
     * @throws ClassNotFoundException 异常
     */
    public static Collection<Class<?>> getClasses(final String domain) throws ClassNotFoundException {
        ConfigurationBuilder configuration = new ConfigurationBuilder();
        configuration.setExpandSuperTypes(false);
        configuration.setUrls(ClasspathHelper.forPackage(domain));
        configuration.setInputsFilter(new FilterBuilder.Include(FileType.CLASS.getPatternString()));
        configuration.setScanners(new TypeElementsScanner().publicOnly(false).includeFields(false).includeAnnotations(false).includeMethods(false));

        final Reflections reflections = new Reflections(configuration);
        Set<Class<?>> set = new HashSet<>();
        for (String className : reflections.getStore().get(TypeElementsScanner.class.getSimpleName()).keySet()) {
            //for (String className : reflections.getStore().keys(TypeElementsScanner.class.getSimpleName())) { // 0.9.12
            if (StringUtils.startsWith(className, domain)) {
                set.add(ClassUtils.getClass(className));
            }
        }
        return set;
    }

    /**
     * 获取一个包下的所有枚举
     *
     * @param domain 包名
     * @return 找到的类
     * @throws ClassNotFoundException 异常
     */
    public static Collection<Class<?>> getEnums(final String domain) throws ClassNotFoundException {
        return getClasses(domain).stream().filter(Class::isEnum).collect(Collectors.toList());
    }
}
