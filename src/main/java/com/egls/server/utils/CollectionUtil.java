package com.egls.server.utils;

import com.egls.server.utils.clazz.ClassUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 提供一些操作集合或者Map的工具方法.
 *
 * @author mayer - [Created on 2018-08-09 16:07]
 */
public final class CollectionUtil {

    /**
     * The default map initial capacity 16 - MUST be a power of two.
     */
    private static final int DEFAULT_MAP_INITIAL_CAPACITY = ObjectUtils.CONST(1 << 4);

    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <C extends Collection<Integer>> C toCollection(final int[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (int value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <C extends Collection<Long>> C toCollection(final long[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (long value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <C extends Collection<Short>> C toCollection(final short[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (short value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <C extends Collection<Byte>> C toCollection(final byte[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (byte value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <C extends Collection<Boolean>> C toCollection(final boolean[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (boolean value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <C extends Collection<Character>> C toCollection(final char[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (char value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <C extends Collection<Float>> C toCollection(final float[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (float value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <C extends Collection<Double>> C toCollection(final double[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        for (double value : array) {
            collection.add(value);
        }
        return collection;
    }

    public static <E, C extends Collection<E>> C toCollection(final E[] array, final Supplier<C> collectionSupplier) {
        C collection = collectionSupplier.get();
        Collections.addAll(collection, array);
        return collection;
    }

    @SafeVarargs
    public static <E, C extends Collection<E>> C addAll(final C acceptor, final C... providers) {
        for (C provider : providers) {
            acceptor.addAll(provider);
        }
        return acceptor;
    }

    @SafeVarargs
    public static <K, V, M extends Map<K, V>> M putAll(final M acceptor, final M... providers) {
        for (M provider : providers) {
            acceptor.putAll(provider);
        }
        return acceptor;
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>(DEFAULT_MAP_INITIAL_CAPACITY);
    }

    public static <K, V> HashMap<K, V> newHashMap(final K key, final V value) {
        final HashMap<K, V> map = newHashMap();
        map.put(key, value);
        return map;
    }

    public static <K, V> HashMap<K, V> newHashMap(final K key1, final V value1, final K key2, final V value2) {
        final HashMap<K, V> map = newHashMap();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>(DEFAULT_MAP_INITIAL_CAPACITY);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(final K key, final V value) {
        final LinkedHashMap<K, V> map = newLinkedHashMap();
        map.put(key, value);
        return map;
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(final K key1, final V value1, final K key2, final V value2) {
        final LinkedHashMap<K, V> map = newLinkedHashMap();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>(DEFAULT_MAP_INITIAL_CAPACITY);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(final K key, final V value) {
        final ConcurrentHashMap<K, V> map = newConcurrentHashMap();
        map.put(key, value);
        return map;
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(final K key1, final V value1, final K key2, final V value2) {
        final ConcurrentHashMap<K, V> map = newConcurrentHashMap();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    public static <E> void addIfAbsent(final List<E> list, final E e) {
        if (!list.contains(e)) {
            list.add(e);
        }
    }

    public static <E> void addIfAbsent(final List<E> list, final E[] es) {
        for (E e : es) {
            addIfAbsent(list, e);
        }
    }

    public static <T> T first(List<T> list) {
        return isEmpty(list) ? null : list.get(0);
    }

    public static <T> T last(List<T> list) {
        return isEmpty(list) ? null : list.get(list.size() - 1);
    }

    /**
     * 删除重复元素、排除空元素、保持原有顺序
     */
    public static <T> List<T> unique(List<T> list) {
        if (isEmpty(list)) {
            return list;
        }
        final List<T> result = new ArrayList<>();
        for (final T item : list) {
            if (item != null && !result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 获取某集合的泛型类型,仅获取单层
     */
    public static Class getCollectionGenericType(final Field field) {
        if (!ClassUtil.isCollection(field.getType())) {
            throw new IllegalArgumentException("Not Collection");
        }
        final Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            final Type[] genericActualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            return (Class) genericActualTypes[0];
        }
        throw new IllegalArgumentException("None Generic Type Of Collection");
    }

    /**
     * 获取Map的泛型,仅获取单层
     */
    public static Pair<Class, Class> getMapGenericType(final Field field) {
        if (!ClassUtil.isMap(field.getType())) {
            throw new IllegalArgumentException("Not Map");
        }
        final Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            final Type[] genericActualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            return Pair.of((Class) genericActualTypes[0], (Class) genericActualTypes[1]);
        }
        throw new IllegalArgumentException("None Generic Type Of Map");
    }

}
