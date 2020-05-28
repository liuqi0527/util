package com.egls.server.utils.databind.serialization;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

import com.egls.server.utils.CollectionUtil;
import com.egls.server.utils.StringUtil;
import com.egls.server.utils.exception.IllegalFormatException;
import com.egls.server.utils.exception.UnsupportedTypeException;
import com.egls.server.utils.reflect.ConstructorUtil;
import com.egls.server.utils.reflect.TypeUtil;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 *     一个序列化的工具对象, 通过本对象,将一些数据变成字符串.类似DataInputStream和DataOutputStream的作用.
 *     提供以下支持:
 *         支持全部基础类型和基础类型数组.
 *         支持List,Map,Set(但反序列化仅支持ArrayList,LinkedList,HashMap,LinkedHashMap,HashSet)
 *         支持枚举类型,支持枚举数组类型,支持集合枚举
 *
 *     Note: 本类的实现不是线程安全的.
 * </pre>
 *
 * @author mayer - [Created on 2018-08-29 14:27]
 */
@SuppressWarnings("unchecked")
public final class Serializer implements Serializable {

    public static <T extends Serializable> T deserialize(final String serializedString, final Class<T> clazz) {
        T result;
        try {
            result = ConstructorUtil.newObjectWithNoneParam(clazz);
            result.deserialize(serializedString);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return result;
    }

    public static <T extends Serializable> T clone(final T object) {
        return (T) deserialize(object.serialize(), object.getClass());
    }

    public static void ensureSerializedString(final String string) {
        if (!isSerializedString(string)) {
            throw new IllegalFormatException(String.format("not serialized string. %s", string));
        }
    }

    public static boolean isSerializedString(final String string) {
        return !StringUtils.isBlank(string)
                && string.charAt(0) == Serialization.BRACKET_LEFT_SERIALIZED_OBJECT_CHAR
                && string.charAt(string.length() - 1) == Serialization.BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR;
    }

    public static void ensureSupported(final Object object) {
        //null对象是允许的
        if (object != null) {
            ensureSupported(object.getClass());
        }
    }

    public static void ensureSupported(final Class<?> clazz) {
        if (!validateSupported(clazz)) {
            throw new UnsupportedTypeException(clazz);
        }
    }

    public static boolean validateSupported(final Class<?> clazz) {
        return Byte.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz)
                || Integer.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || Enum.class.isAssignableFrom(clazz)
                || String.class.isAssignableFrom(clazz)
                || Serializable.class.isAssignableFrom(clazz)

                || byte[].class.isAssignableFrom(clazz)
                || boolean[].class.isAssignableFrom(clazz)
                || char[].class.isAssignableFrom(clazz)
                || short[].class.isAssignableFrom(clazz)
                || int[].class.isAssignableFrom(clazz)
                || long[].class.isAssignableFrom(clazz)
                || float[].class.isAssignableFrom(clazz)
                || double[].class.isAssignableFrom(clazz)

                || Byte[].class.isAssignableFrom(clazz)
                || Boolean[].class.isAssignableFrom(clazz)
                || Character[].class.isAssignableFrom(clazz)
                || Short[].class.isAssignableFrom(clazz)
                || Integer[].class.isAssignableFrom(clazz)
                || Long[].class.isAssignableFrom(clazz)
                || Float[].class.isAssignableFrom(clazz)
                || Double[].class.isAssignableFrom(clazz)
                || Enum[].class.isAssignableFrom(clazz)
                || String[].class.isAssignableFrom(clazz)
                || Serializable[].class.isAssignableFrom(clazz)

                || Map.class.isAssignableFrom(clazz)
                || Collection.class.isAssignableFrom(clazz);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private int position = 0;

    private ArrayList<String> list = new ArrayList<>();

    /**
     * 本构造方法是提供给序列化使用的.
     */
    public Serializer() {
    }

    /**
     * 本构造方法是提供给反序列化使用的
     *
     * @param serializedString 序列化的字符串
     */
    public Serializer(final String serializedString) {
        ensureSerializedString(serializedString);
        String operationString = StringUtils.substring(serializedString, 1, serializedString.length() - 1);
        Serialization.CharacterReader characterReader = new Serialization.CharacterReader(operationString.toCharArray());
        Serialization.NextStringResult nextStringResult;
        while ((nextStringResult = Serialization.nextString(characterReader)) != null) {
            list.add(nextStringResult.result);
        }
    }

    private String nextOperationString() {
        return list.get(position++);
    }

    private <T> T get(final Class<T> clazz) {
        ensureSupported(clazz);
        final String operationString = nextOperationString();
        if (operationString == null) {
            //这里的条件是判断真的为null
            return null;
        } else if (TypeUtil.isByte(clazz)) {
            return (T) Byte.valueOf(operationString);
        } else if (TypeUtil.isBoolean(clazz)) {
            return (T) StringUtil.toBooleanObject(operationString);
        } else if (TypeUtil.isCharacter(clazz)) {
            return (T) CharUtils.toCharacterObject(operationString);
        } else if (TypeUtil.isShort(clazz)) {
            return (T) Short.valueOf(operationString);
        } else if (TypeUtil.isInteger(clazz)) {
            return (T) Integer.valueOf(operationString);
        } else if (TypeUtil.isLong(clazz)) {
            return (T) Long.valueOf(operationString);
        } else if (TypeUtil.isFloat(clazz)) {
            return (T) Float.valueOf(operationString);
        } else if (TypeUtil.isDouble(clazz)) {
            return (T) Double.valueOf(operationString);
        } else if (Enum.class.isAssignableFrom(clazz)) {
            return (T) Enum.valueOf((Class<? extends Enum>) clazz, operationString);
        } else if (String.class.isAssignableFrom(clazz)) {
            return (T) operationString;
        } else if (Serializable.class.isAssignableFrom(clazz)) {
            return (T) deserialize(operationString, (Class<? extends Serializable>) clazz);
        } else if (byte[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeBytePrimitiveArray(operationString);
        } else if (boolean[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeBooleanPrimitiveArray(operationString);
        } else if (char[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeCharPrimitiveArray(operationString);
        } else if (short[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeShortPrimitiveArray(operationString);
        } else if (int[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeIntPrimitiveArray(operationString);
        } else if (long[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeLongPrimitiveArray(operationString);
        } else if (float[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeFloatPrimitiveArray(operationString);
        } else if (double[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeDoublePrimitiveArray(operationString);
        } else if (Byte[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Byte[]::new, Byte::valueOf);
        } else if (Boolean[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Boolean[]::new, StringUtil::toBooleanObject);
        } else if (Character[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Character[]::new, CharUtils::toChar);
        } else if (Short[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Short[]::new, Short::valueOf);
        } else if (Integer[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Integer[]::new, Integer::valueOf);
        } else if (Long[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Long[]::new, Long::valueOf);
        } else if (Float[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Float[]::new, Float::valueOf);
        } else if (Double[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeObjectArray(operationString, Double[]::new, Double::valueOf);
        } else if (Enum[].class.isAssignableFrom(clazz)) {
            final Class<? extends Enum> enumClass = (Class<? extends Enum>) clazz.getComponentType();
            return (T) Serialization.deserializeObjectArray(operationString, length -> (Enum<?>[]) Array.newInstance(enumClass, length), s -> Enum.valueOf(enumClass, s));
        } else if (String[].class.isAssignableFrom(clazz)) {
            return (T) Serialization.deserializeEscapedObjectArray(operationString, String[]::new, s -> s);
        } else if (Serializable[].class.isAssignableFrom(clazz)) {
            final Class<? extends Serializable> elementClass = (Class<? extends Serializable>) clazz.getComponentType();
            return (T) Serialization.deserializeEscapedObjectArray(operationString, length -> (Serializable[]) Array.newInstance(elementClass, length), s -> deserialize(s, elementClass));
        } else {
            throw new UnsupportedTypeException(clazz);
        }
    }

    public final void add(final Object object) {
        ensureSupported(object);
        if (object == null) {
            list.add(Serialization.NULL);
        } else if (object instanceof Byte) {
            list.add(String.valueOf(object));
        } else if (object instanceof Boolean) {
            list.add(String.valueOf(object));
        } else if (object instanceof Character) {
            list.add(String.valueOf(object));
        } else if (object instanceof Short) {
            list.add(String.valueOf(object));
        } else if (object instanceof Integer) {
            list.add(String.valueOf(object));
        } else if (object instanceof Long) {
            list.add(String.valueOf(object));
        } else if (object instanceof Float) {
            list.add(String.valueOf(object));
        } else if (object instanceof Double) {
            list.add(String.valueOf(object));
        } else if (object instanceof Enum) {
            list.add(((Enum<?>) object).name());
        } else if (object instanceof String) {
            list.add(Serialization.escapeString((String) object));
        } else if (object instanceof Serializable) {
            list.add(((Serializable) object).serialize());
        } else if (object instanceof byte[]) {
            list.add(Serialization.serializeBytePrimitiveArray((byte[]) object));
        } else if (object instanceof boolean[]) {
            list.add(Serialization.serializeBooleanPrimitiveArray((boolean[]) object));
        } else if (object instanceof char[]) {
            list.add(Serialization.serializeCharPrimitiveArray((char[]) object));
        } else if (object instanceof short[]) {
            list.add(Serialization.serializeShortPrimitiveArray((short[]) object));
        } else if (object instanceof int[]) {
            list.add(Serialization.serializeIntPrimitiveArray((int[]) object));
        } else if (object instanceof long[]) {
            list.add(Serialization.serializeLongPrimitiveArray((long[]) object));
        } else if (object instanceof float[]) {
            list.add(Serialization.serializeFloatPrimitiveArray((float[]) object));
        } else if (object instanceof double[]) {
            list.add(Serialization.serializeDoublePrimitiveArray((double[]) object));
        } else if (object instanceof Byte[]) {
            list.add(Serialization.serializeObjectArray((Byte[]) object, String::valueOf));
        } else if (object instanceof Boolean[]) {
            list.add(Serialization.serializeObjectArray((Boolean[]) object, String::valueOf));
        } else if (object instanceof Character[]) {
            list.add(Serialization.serializeObjectArray((Character[]) object, CharUtils::toString));
        } else if (object instanceof Short[]) {
            list.add(Serialization.serializeObjectArray((Short[]) object, String::valueOf));
        } else if (object instanceof Integer[]) {
            list.add(Serialization.serializeObjectArray((Integer[]) object, String::valueOf));
        } else if (object instanceof Long[]) {
            list.add(Serialization.serializeObjectArray((Long[]) object, String::valueOf));
        } else if (object instanceof Float[]) {
            list.add(Serialization.serializeObjectArray((Float[]) object, String::valueOf));
        } else if (object instanceof Double[]) {
            list.add(Serialization.serializeObjectArray((Double[]) object, String::valueOf));
        } else if (Enum[].class.isAssignableFrom(object.getClass())) {
            list.add(Serialization.serializeObjectArray((Enum[]) object, Enum::name));
        } else if (object instanceof String[]) {
            list.add(Serialization.serializeObjectArray((String[]) object, Serialization::escapeString));
        } else if (object instanceof Serializable[]) {
            list.add(Serialization.serializeObjectArray((Serializable[]) object, Serializable::serialize));
        } else if (object instanceof Map) {
            final Serializer serializer = new Serializer();
            ((Map<?, ?>) object).forEach((key, value) -> {
                serializer.add(key);
                serializer.add(value);
            });
            add(serializer);
        } else if (object instanceof Collection) {
            final Serializer serializer = new Serializer();
            ((Collection<?>) object).forEach(serializer::add);
            add(serializer);
        } else {
            throw new UnsupportedTypeException(object.getClass());
        }
    }

    @Override
    public String serialize() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Serialization.BRACKET_LEFT_SERIALIZED_OBJECT_CHAR);
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if ((i + 1) < list.size()) {
                stringBuilder.append(Serialization.SPLIT_CHAR);
            }
        }
        stringBuilder.append(Serialization.BRACKET_RIGHT_SERIALIZED_OBJECT_CHAR);
        return stringBuilder.toString();
    }

    @Override
    public void deserialize(String serializedString) {
        Serializer serializer = new Serializer(serializedString);
        this.position = serializer.position;
        this.list = serializer.list;
    }

    public final boolean hasNext() {
        return position < list.size();
    }

    public final void reload() {
        position = 0;
    }

    public final void reset() {
        position = 0;
        list.clear();
    }

    public final int size() {
        return list.size() - position;
    }

    public final Byte getByte() {
        return get(Byte.class);
    }

    public final Boolean getBoolean() {
        return get(Boolean.class);
    }

    public final Character getChar() {
        return get(Character.class);
    }

    public final Short getShort() {
        return get(Short.class);
    }

    public final Integer getInt() {
        return get(Integer.class);
    }

    public final Long getLong() {
        return get(Long.class);
    }

    public final Float getFloat() {
        return get(Float.class);
    }

    public final Double getDouble() {
        return get(Double.class);
    }

    public final <T extends Enum<T>> T getEnum(Class<T> clazz) {
        return get(clazz);
    }

    public final String getString() {
        return get(String.class);
    }

    public final <T extends Serializable> T getSerializableObject(final Class<T> clazz) {
        return get(clazz);
    }

    public final byte[] getBytePrimitiveArray() {
        return get(byte[].class);
    }

    public final boolean[] getBooleanPrimitiveArray() {
        return get(boolean[].class);
    }

    public final char[] getCharPrimitiveArray() {
        return get(char[].class);
    }

    public final short[] getShortPrimitiveArray() {
        return get(short[].class);
    }

    public final int[] getIntPrimitiveArray() {
        return get(int[].class);
    }

    public final long[] getLongPrimitiveArray() {
        return get(long[].class);
    }

    public final float[] getFloatPrimitiveArray() {
        return get(float[].class);
    }

    public final double[] getDoublePrimitiveArray() {
        return get(double[].class);
    }

    public final Byte[] getByteObjectArray() {
        return get(Byte[].class);
    }

    public final Boolean[] getBooleanObjectArray() {
        return get(Boolean[].class);
    }

    public final Character[] getCharObjectArray() {
        return get(Character[].class);
    }

    public final Short[] getShortObjectArray() {
        return get(Short[].class);
    }

    public final Integer[] getIntObjectArray() {
        return get(Integer[].class);
    }

    public final Long[] getLongObjectArray() {
        return get(Long[].class);
    }

    public final Float[] getFloatObjectArray() {
        return get(Float[].class);
    }

    public final Double[] getDoubleObjectArray() {
        return get(Double[].class);
    }

    public final <T extends Enum<T>> T[] getEnumArray(Class<T> enumClass) {
        return Serialization.deserializeObjectArray(nextOperationString(), length -> (T[]) Array.newInstance(enumClass, length), s -> Enum.valueOf(enumClass, s));
    }

    public final String[] getStringArray() {
        return get(String[].class);
    }

    public final <T extends Serializable> T[] getSerializeObjectArray(final Class<T> elementClass) {
        return Serialization.deserializeEscapedObjectArray(nextOperationString(), length -> (T[]) Array.newInstance(elementClass, length), s -> deserialize(s, elementClass));
    }

    public final <K, V> HashMap<K, V> getHashMap(final Class<K> keyClass, final Class<V> valueClass) {
        return (HashMap<K, V>) getMap(keyClass, valueClass, (Supplier<Map<K, V>>) HashMap::new);
    }

    public final <K, V> LinkedHashMap<K, V> getLinkedHashMap(final Class<K> keyClass, final Class<V> valueClass) {
        return (LinkedHashMap<K, V>) getMap(keyClass, valueClass, (Supplier<Map<K, V>>) LinkedHashMap::new);
    }

    public final <K, V> ConcurrentHashMap<K, V> getConcurrentHashMap(final Class<K> keyClass, final Class<V> valueClass) {
        return (ConcurrentHashMap<K, V>) getMap(keyClass, valueClass, (Supplier<Map<K, V>>) ConcurrentHashMap::new);
    }

    public final <K, V, M extends Map<K, V>> M getMap(final Class<K> keyClass, final Class<V> valueClass, final Supplier<M> mapFactory) {
        final String operationString = nextOperationString();
        if (Serialization.isNull(operationString)) {
            return null;
        }
        final M map = mapFactory.get();
        final Serializer serializer = new Serializer(operationString);
        while (serializer.hasNext()) {
            map.put(serializer.get(keyClass), serializer.get(valueClass));
        }
        return map;
    }

    public final <E> ArrayList<E> getArrayList(final Class<E> elementClass) {
        return (ArrayList<E>) getCollection(elementClass, (Supplier<Collection<E>>) ArrayList::new);
    }

    public final <E> LinkedList<E> getLinkedList(final Class<E> elementClass) {
        return (LinkedList<E>) getCollection(elementClass, (Supplier<Collection<E>>) LinkedList::new);
    }

    public final <E> CopyOnWriteArrayList<E> getCopyOnWriteArrayList(final Class<E> elementClass) {
        return (CopyOnWriteArrayList<E>) getCollection(elementClass, (Supplier<Collection<E>>) CopyOnWriteArrayList::new);
    }

    public final <E> HashSet<E> getHashSet(final Class<E> elementClass) {
        return (HashSet<E>) getCollection(elementClass, (Supplier<Collection<E>>) HashSet::new);
    }

    public final <E> LinkedHashSet<E> getLinkedHashSet(final Class<E> elementClass) {
        return (LinkedHashSet<E>) getCollection(elementClass, (Supplier<Collection<E>>) LinkedHashSet::new);
    }

    public final <E> Set<E> getConcurrentHashSet(final Class<E> elementClass) {
        return (Set<E>) getCollection(elementClass, (Supplier<Collection<E>>) () -> Collections.newSetFromMap(CollectionUtil.newConcurrentHashMap()));
    }

    public final <E, C extends Collection<E>> C getCollection(final Class<E> elementClass, final Supplier<C> listFactory) {
        final String operationString = nextOperationString();
        if (Serialization.isNull(operationString)) {
            return null;
        }
        final C collection = listFactory.get();
        final Serializer serializer = new Serializer(operationString);
        while (serializer.hasNext()) {
            collection.add(serializer.get(elementClass));
        }
        return collection;
    }

}
