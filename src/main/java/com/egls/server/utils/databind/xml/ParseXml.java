package com.egls.server.utils.databind.xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.egls.server.utils.CollectionUtil;
import com.egls.server.utils.StringUtil;
import com.egls.server.utils.array.ArrayUtil;
import com.egls.server.utils.clazz.ClassUtil;
import com.egls.server.utils.databind.xml.annotation.*;
import com.egls.server.utils.exception.ExceptionUtil;
import com.egls.server.utils.exception.MissingResourceException;
import com.egls.server.utils.exception.UnsupportedTypeException;
import com.egls.server.utils.reflect.ConstructorUtil;
import com.egls.server.utils.reflect.TypeUtil;
import com.egls.server.utils.text.XmlUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

/**
 * 包内可见的解析Xml的具体方法
 *
 * @author mayer - [Created on 2018-08-27 11:54]
 */
@SuppressWarnings({"Duplicates", "unchecked"})
final class ParseXml {

    private static void ensureIgnorable(final Field field) {
        if (!field.isAnnotationPresent(XmlIgnorable.class)) {
            throw new MissingResourceException(field.toString());
        }
    }

    /**
     * @see XmlAttribute
     */
    static void parseXmlAttribute(final XmlObject xmlObject, final Field field, final XmlAttribute annotation, final Element element) {
        try {
            if (element == null) {
                ensureIgnorable(field);
                return;
            }
            final String text = XmlUtil.getAttribute(element, annotation.value());
            if (StringUtils.isEmpty(text)) {
                //没有配置attribute
                ensureIgnorable(field);
                return;
            }
            final Class fieldType = field.getType();
            final XmlElementParser specifiedParser = getSpecifiedXmlElementParser(annotation.parser());
            if (specifiedParser != null) {
                if (TypeUtil.isPrimitiveType(fieldType)) {
                    //不支持8种基础类型的自定义,如果需要对基础类型进行自定义,可以使用包装类进行哦
                    throw new UnsupportedTypeException(fieldType);
                }
                TypeUtil.setObject(xmlObject, field, specifiedParser.parseXmlElementToObject(element));
            } else {
                if (TypeUtil.isByte(fieldType)) {
                    TypeUtil.setByte(xmlObject, field, Byte.valueOf(text));
                } else if (TypeUtil.isBoolean(fieldType)) {
                    TypeUtil.setBoolean(xmlObject, field, StringUtil.toBoolean(text));
                } else if (TypeUtil.isCharacter(fieldType)) {
                    TypeUtil.setCharacter(xmlObject, field, CharUtils.toChar(text));
                } else if (TypeUtil.isShort(fieldType)) {
                    TypeUtil.setShort(xmlObject, field, Short.valueOf(text));
                } else if (TypeUtil.isInteger(fieldType)) {
                    TypeUtil.setInteger(xmlObject, field, Integer.valueOf(text));
                } else if (TypeUtil.isLong(fieldType)) {
                    TypeUtil.setLong(xmlObject, field, Long.valueOf(text));
                } else if (TypeUtil.isFloat(fieldType)) {
                    TypeUtil.setFloat(xmlObject, field, Float.valueOf(text));
                } else if (TypeUtil.isDouble(fieldType)) {
                    TypeUtil.setDouble(xmlObject, field, Double.valueOf(text));
                } else if (TypeUtil.isString(fieldType)) {
                    TypeUtil.setString(xmlObject, field, text);
                } else if (TypeUtil.isEnum(fieldType)) {
                    TypeUtil.setEnum(xmlObject, field, text);
                } else {
                    throw new UnsupportedTypeException(fieldType);
                }
            }
        } catch (Exception exception) {
            final Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[attribute:%s] -> %s", annotation.value(), exception.getMessage()), throwable);
        }
    }

    /**
     * @see XmlAttributeArray
     */
    static void parseXmlAttributeArray(final XmlObject xmlObject, final Field field, final XmlAttributeArray annotation, final Element element) {
        try {
            if (element == null) {
                ensureIgnorable(field);
                return;
            }
            final String[] texts = XmlUtil.getChildrenAttribute(element, annotation.element(), annotation.attribute());
            if (ArrayUtils.isEmpty(texts) || Arrays.stream(texts).anyMatch(StringUtils::isEmpty)) {
                ensureIgnorable(field);
                return;
            }
            final Class fieldType = field.getType();
            final XmlElementParser specifiedParser = getSpecifiedXmlElementParser(annotation.parser());
            if (specifiedParser != null) {
                if (TypeUtil.isPrimitiveTypeArray(fieldType)) {
                    //不支持8种基础类型的自定义,如果需要对基础类型进行自定义,可以使用包装类进行哦
                    throw new UnsupportedTypeException(fieldType);
                }
                final Element[] children = XmlUtil.getChildren(element, annotation.element());
                final Object array = Array.newInstance(fieldType.getComponentType(), children.length);
                for (int index = 0; index < children.length; index++) {
                    try {
                        Array.set(array, index, specifiedParser.parseXmlElementToObject(children[index]));
                    } catch (Exception exception) {
                        throw new RuntimeException(String.format("[index:%d] -> %s", index, exception.getMessage()), exception);
                    }
                }
                TypeUtil.setObject(xmlObject, field, array);
            } else {
                if (TypeUtil.isByteArray(fieldType)) {
                    TypeUtil.setByteArray(xmlObject, field, ArrayUtil.toBytePrimitiveArray(texts));
                } else if (TypeUtil.isBooleanArray(fieldType)) {
                    TypeUtil.setBooleanArray(xmlObject, field, ArrayUtil.toBooleanPrimitiveArray(texts));
                } else if (TypeUtil.isCharacterArray(fieldType)) {
                    TypeUtil.setCharacterArray(xmlObject, field, ArrayUtil.toCharPrimitiveArray(texts));
                } else if (TypeUtil.isShortArray(fieldType)) {
                    TypeUtil.setShortArray(xmlObject, field, ArrayUtil.toShortPrimitiveArray(texts));
                } else if (TypeUtil.isIntegerArray(fieldType)) {
                    TypeUtil.setIntegerArray(xmlObject, field, ArrayUtil.toIntPrimitiveArray(texts));
                } else if (TypeUtil.isLongArray(fieldType)) {
                    TypeUtil.setLongArray(xmlObject, field, ArrayUtil.toLongPrimitiveArray(texts));
                } else if (TypeUtil.isFloatArray(fieldType)) {
                    TypeUtil.setFloatArray(xmlObject, field, ArrayUtil.toFloatPrimitiveArray(texts));
                } else if (TypeUtil.isDoubleArray(fieldType)) {
                    TypeUtil.setDoubleArray(xmlObject, field, ArrayUtil.toDoublePrimitiveArray(texts));
                } else if (TypeUtil.isStringArray(fieldType)) {
                    TypeUtil.setStringArray(xmlObject, field, texts);
                } else if (TypeUtil.isEnumArray(fieldType)) {
                    TypeUtil.setObject(xmlObject, field, ArrayUtil.toEnumArray(fieldType.getComponentType(), texts));
                } else {
                    throw new UnsupportedTypeException(fieldType);
                }
            }
        } catch (Exception exception) {
            final Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[element:%s] -> [attribute:%s] -> %s", annotation.element(), annotation.attribute(), exception.getMessage()), throwable);
        }
    }

    /**
     * @see XmlElement
     */
    static void parseXmlElement(final XmlObject xmlObject, final Field field, final XmlElement annotation, final Element element) {
        try {
            if (element == null) {
                ensureIgnorable(field);
                return;
            }
            final Class fieldType = field.getType();
            final Element child = XmlUtil.getChild(element, annotation.value());
            if (child == null) {
                ensureIgnorable(field);
                return;
            }
            final XmlElementParser specifiedParser = getSpecifiedXmlElementParser(annotation.parser());
            if (specifiedParser != null) {
                if (TypeUtil.isPrimitiveType(fieldType)) {
                    //不支持8种基础类型的自定义,如果需要对基础类型进行自定义,可以使用包装类进行哦
                    throw new UnsupportedTypeException(fieldType);
                }
                TypeUtil.setObject(xmlObject, field, specifiedParser.parseXmlElementToObject(child));
            } else {
                if (TypeUtil.isBasicType(fieldType)) {
                    //基础类型
                    final String text = XmlUtil.getText(child);
                    if (TypeUtil.isByte(fieldType)) {
                        TypeUtil.setByte(xmlObject, field, Byte.valueOf(text));
                    } else if (TypeUtil.isBoolean(fieldType)) {
                        TypeUtil.setBoolean(xmlObject, field, StringUtil.toBoolean(text));
                    } else if (TypeUtil.isCharacter(fieldType)) {
                        TypeUtil.setCharacter(xmlObject, field, CharUtils.toChar(text));
                    } else if (TypeUtil.isShort(fieldType)) {
                        TypeUtil.setShort(xmlObject, field, Short.valueOf(text));
                    } else if (TypeUtil.isInteger(fieldType)) {
                        TypeUtil.setInteger(xmlObject, field, Integer.valueOf(text));
                    } else if (TypeUtil.isLong(fieldType)) {
                        TypeUtil.setLong(xmlObject, field, Long.valueOf(text));
                    } else if (TypeUtil.isFloat(fieldType)) {
                        TypeUtil.setFloat(xmlObject, field, Float.valueOf(text));
                    } else if (TypeUtil.isDouble(fieldType)) {
                        TypeUtil.setDouble(xmlObject, field, Double.valueOf(text));
                    } else if (TypeUtil.isString(fieldType)) {
                        TypeUtil.setString(xmlObject, field, text);
                    } else if (TypeUtil.isEnum(fieldType)) {
                        TypeUtil.setEnum(xmlObject, field, text);
                    } else {
                        throw new UnsupportedTypeException(fieldType);
                    }
                } else {
                    TypeUtil.setObject(xmlObject, field, parseObject(fieldType, child));
                }
            }
        } catch (Exception exception) {
            final Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[element:%s] -> %s", annotation.value(), exception.getMessage()), throwable);
        }
    }

    /**
     * @see XmlElementArray
     */
    static void parseXmlElementArray(final XmlObject xmlObject, final Field field, final XmlElementArray annotation, final Element element) {
        try {
            if (element == null) {
                ensureIgnorable(field);
                return;
            }
            final Class fieldType = field.getType();
            final Element[] children = XmlUtil.getChildren(element, annotation.value());
            if (ArrayUtils.isEmpty(children)) {
                ensureIgnorable(field);
                return;
            }
            final XmlElementParser specifiedParser = getSpecifiedXmlElementParser(annotation.parser());
            if (specifiedParser != null) {
                if (TypeUtil.isPrimitiveTypeArray(fieldType)) {
                    //不支持8种基础类型的自定义,如果需要对基础类型进行自定义,可以使用包装类进行哦
                    throw new UnsupportedTypeException(fieldType);
                }
                final Object array = Array.newInstance(fieldType.getComponentType(), children.length);
                for (int index = 0; index < children.length; index++) {
                    try {
                        Array.set(array, index, specifiedParser.parseXmlElementToObject(children[index]));
                    } catch (Exception exception) {
                        throw new RuntimeException(String.format("[index:%d] -> %s", index, exception.getMessage()), exception);
                    }
                }
                TypeUtil.setObject(xmlObject, field, array);
            } else {
                if (TypeUtil.isBasicTypeArray(fieldType)) {
                    final String[] texts = XmlUtil.getChildrenText(element, annotation.value());
                    if (TypeUtil.isByteArray(fieldType)) {
                        TypeUtil.setByteArray(xmlObject, field, ArrayUtil.toBytePrimitiveArray(texts));
                    } else if (TypeUtil.isBooleanArray(fieldType)) {
                        TypeUtil.setBooleanArray(xmlObject, field, ArrayUtil.toBooleanPrimitiveArray(texts));
                    } else if (TypeUtil.isCharacterArray(fieldType)) {
                        TypeUtil.setCharacterArray(xmlObject, field, ArrayUtil.toCharPrimitiveArray(texts));
                    } else if (TypeUtil.isShortArray(fieldType)) {
                        TypeUtil.setShortArray(xmlObject, field, ArrayUtil.toShortPrimitiveArray(texts));
                    } else if (TypeUtil.isIntegerArray(fieldType)) {
                        TypeUtil.setIntegerArray(xmlObject, field, ArrayUtil.toIntPrimitiveArray(texts));
                    } else if (TypeUtil.isLongArray(fieldType)) {
                        TypeUtil.setLongArray(xmlObject, field, ArrayUtil.toLongPrimitiveArray(texts));
                    } else if (TypeUtil.isFloatArray(fieldType)) {
                        TypeUtil.setFloatArray(xmlObject, field, ArrayUtil.toFloatPrimitiveArray(texts));
                    } else if (TypeUtil.isDoubleArray(fieldType)) {
                        TypeUtil.setDoubleArray(xmlObject, field, ArrayUtil.toDoublePrimitiveArray(texts));
                    } else if (TypeUtil.isStringArray(fieldType)) {
                        TypeUtil.setStringArray(xmlObject, field, texts);
                    } else if (TypeUtil.isEnumArray(fieldType)) {
                        TypeUtil.setObject(xmlObject, field, ArrayUtil.toEnumArray(fieldType.getComponentType(), texts));
                    } else {
                        throw new UnsupportedTypeException(fieldType);
                    }
                } else {
                    final Object array = Array.newInstance(fieldType.getComponentType(), children.length);
                    for (int index = 0; index < children.length; index++) {
                        try {
                            Array.set(array, index, parseObject(fieldType.getComponentType(), children[index]));
                        } catch (Exception exception) {
                            throw new RuntimeException(String.format("[index:%d] -> %s", index, exception.getMessage()), exception);
                        }
                    }
                    TypeUtil.setObject(xmlObject, field, array);
                }
            }
        } catch (Exception exception) {
            Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[element:%s] -> %s", annotation.value(), exception.getMessage()), throwable);
        }
    }

    /**
     * @see XmlElementCollection
     */
    static void parseXmlElementCollection(final XmlObject xmlObject, final Field field, final XmlElementCollection annotation, final Element element) {
        try {
            if (element == null) {
                ensureIgnorable(field);
                return;
            }
            final Class fieldType = field.getType();
            final Element[] children = XmlUtil.getChildren(element, annotation.value());
            if (ArrayUtils.isEmpty(children)) {
                ensureIgnorable(field);
                return;
            }
            final XmlElementParser specifiedParser = getSpecifiedXmlElementParser(annotation.parser());
            if (!ClassUtil.isCollection(fieldType)) {
                //不是集合类,报错
                throw new UnsupportedTypeException(fieldType);
            }
            final Class elementType = CollectionUtil.getCollectionGenericType(field);
            final Collection<Object> collection = ConstructorUtil.newObjectWithNoneParam(fieldType);
            if (specifiedParser != null) {
                for (int index = 0; index < children.length; index++) {
                    try {
                        collection.add(specifiedParser.parseXmlElementToObject(children[index]));
                    } catch (Exception exception) {
                        throw new RuntimeException(String.format("[index:%d] -> %s", index, exception.getMessage()), exception);
                    }
                }
                TypeUtil.setObject(xmlObject, field, collection);
            } else {
                for (int index = 0; index < children.length; index++) {
                    try {
                        collection.add(parseObject(elementType, children[index]));
                    } catch (Exception exception) {
                        throw new RuntimeException(String.format("[index:%d] -> %s", index, exception.getMessage()), exception);
                    }
                }
                TypeUtil.setObject(xmlObject, field, collection);
            }
        } catch (Exception exception) {
            Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[element:%s] -> %s", annotation.value(), exception.getMessage()), throwable);
        }
    }

    /**
     * 不允许出现key配置了, value不配值. 允许整个Map是Ignorable
     *
     * @see XmlElementDeclineMap
     */
    static void parseXmlElementDeclineMap(final XmlObject xmlObject, final Field field, final XmlElementDeclineMap annotation, final Element element) {
        try {
            if (element == null) {
                ensureIgnorable(field);
                return;
            }
            final Class fieldType = field.getType();
            final Element[] children = XmlUtil.getChildren(element, annotation.element());
            if (ArrayUtils.isEmpty(children)) {
                ensureIgnorable(field);
                return;
            }
            final XmlElementParser keySpecifiedParser = getSpecifiedXmlElementParser(annotation.keyParser());
            final XmlElementParser valueSpecifiedParser = getSpecifiedXmlElementParser(annotation.valueParser());
            if (!ClassUtil.isMap(fieldType)) {
                //不是Map类,报错
                throw new UnsupportedTypeException(fieldType);
            }

            final Pair<Class, Class> mapGenericType = CollectionUtil.getMapGenericType(field);
            final Class keyType = mapGenericType.getKey();
            final Class valueType = mapGenericType.getValue();

            final Map<Object, Object> map = ConstructorUtil.newObjectWithNoneParam(fieldType);
            for (int index = 0; index < children.length; index++) {
                try {
                    final Element keyElement = XmlUtil.getChild(children[index], annotation.key());
                    final Object key = keySpecifiedParser == null
                            ? parseObject(keyType, keyElement)
                            : keySpecifiedParser.parseXmlElementToObject(keyElement);

                    final Object value;
                    if (ClassUtil.isArray(valueType)) {
                        final Element[] valueElements = XmlUtil.getChildren(children[index], annotation.value());
                        value = Array.newInstance(valueType.getComponentType(), valueElements.length);
                        try {
                            for (int valueElementIndex = 0; valueElementIndex < valueElements.length; valueElementIndex++) {
                                try {
                                    Array.set(value, valueElementIndex, valueSpecifiedParser == null
                                            ? parseObject(valueType.getComponentType(), valueElements[valueElementIndex])
                                            : valueSpecifiedParser.parseXmlElementToObject(valueElements[valueElementIndex]));
                                } catch (Exception exception) {
                                    throw new RuntimeException(String.format("[index:%d] -> %s", valueElementIndex, exception.getMessage()), exception);
                                }
                            }
                        } catch (Exception exception) {
                            Throwable throwable = ExceptionUtil.getSuperCause(exception);
                            throw new RuntimeException(String.format("[element:%s] -> %s", annotation.value(), exception.getMessage()), throwable);
                        }
                    } else {
                        final Element valueElement = XmlUtil.getChild(children[index], annotation.value());
                        value = valueSpecifiedParser == null
                                ? parseObject(valueType, valueElement)
                                : valueSpecifiedParser.parseXmlElementToObject(valueElement);
                    }
                    map.put(key, value);
                } catch (Exception exception) {
                    throw new RuntimeException(String.format("[index:%d] -> %s", index, exception.getMessage()), exception);
                }
            }
            TypeUtil.setObject(xmlObject, field, map);
        } catch (Exception exception) {
            Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[element:%s] -> %s", annotation.element(), exception.getMessage()), throwable);
        }
    }

    /**
     * 不允许出现key配置了, value不配值. 允许整个Map是Ignorable
     *
     * @see XmlElementExtractMap
     */
    static void parseXmlElementExtractMap(final XmlObject xmlObject, final Field field, final XmlElementExtractMap annotation, final Element element) {
        try {
            if (element == null) {
                ensureIgnorable(field);
                return;
            }
            final Class fieldType = field.getType();
            final Element[] children = XmlUtil.getChildren(element, annotation.value());
            if (ArrayUtils.isEmpty(children)) {
                ensureIgnorable(field);
                return;
            }
            final XmlElementParser keySpecifiedParser = getSpecifiedXmlElementParser(annotation.keyParser());
            final XmlElementParser valueSpecifiedParser = getSpecifiedXmlElementParser(annotation.valueParser());
            if (!ClassUtil.isMap(fieldType)) {
                //不是Map类,报错
                throw new UnsupportedTypeException(fieldType);
            }

            final Pair<Class, Class> mapGenericType = CollectionUtil.getMapGenericType(field);
            final Class keyType = mapGenericType.getKey();
            final Class valueType = mapGenericType.getValue();

            final Map<Object, Object> map = ConstructorUtil.newObjectWithNoneParam(fieldType);
            for (int index = 0; index < children.length; index++) {
                try {
                    final Element keyElement = XmlUtil.getChild(children[index], annotation.key());
                    final Object key = keySpecifiedParser == null
                            ? parseObject(keyType, keyElement)
                            : keySpecifiedParser.parseXmlElementToObject(keyElement);

                    final Element valueElement = children[index];
                    final Object value = valueSpecifiedParser == null
                            ? parseObject(valueType, valueElement)
                            : valueSpecifiedParser.parseXmlElementToObject(valueElement);
                    map.put(key, value);
                } catch (Exception exception) {
                    throw new RuntimeException(String.format("[index:%d] -> %s", index, exception.getMessage()), exception);
                }
            }
            TypeUtil.setObject(xmlObject, field, map);
        } catch (Exception exception) {
            Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[element:%s] -> %s", annotation.value(), exception.getMessage()), throwable);
        }
    }

    private static Object parseObject(final Class clazz, final Element element)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (TypeUtil.isBasicType(clazz)) {
            if (TypeUtil.isByte(clazz)) {
                return Byte.valueOf(XmlUtil.getText(element));
            } else if (TypeUtil.isBoolean(clazz)) {
                return StringUtil.toBoolean(XmlUtil.getText(element));
            } else if (TypeUtil.isCharacter(clazz)) {
                return CharUtils.toChar(XmlUtil.getText(element));
            } else if (TypeUtil.isShort(clazz)) {
                return Short.valueOf(XmlUtil.getText(element));
            } else if (TypeUtil.isInteger(clazz)) {
                return Integer.valueOf(XmlUtil.getText(element));
            } else if (TypeUtil.isLong(clazz)) {
                return Long.valueOf(XmlUtil.getText(element));
            } else if (TypeUtil.isFloat(clazz)) {
                return Float.valueOf(XmlUtil.getText(element));
            } else if (TypeUtil.isDouble(clazz)) {
                return Double.valueOf(XmlUtil.getText(element));
            } else if (String.class.isAssignableFrom(clazz)) {
                return XmlUtil.getText(element);
            } else if (TypeUtil.isEnum(clazz)) {
                return Enum.valueOf(clazz, XmlUtil.getText(element));
            }
        }

        if (XmlObject.class.isAssignableFrom(clazz)) {
            //不能是抽象类,不能是接口,必须有无参构造
            if (ClassUtil.isConcrete(clazz) && ConstructorUtil.containsNoneParamConstructor(clazz)) {
                final XmlObject xmlObject = ConstructorUtil.newObjectWithNoneParam(clazz);
                xmlObject.load(element);
                return xmlObject;
            }
        }

        // 此处出现异常,请检查以下几点:
        // 提示检查.1:检查是否忘记了实现接口XmlObject
        // 提示检查.2:检查是否有无参构造器
        // 提示检查.3:检查注解类型和Field类型是否对应.不要用单个注解载入数组Field
        // 提示检查.4:检查是否没有指定XmlElementParser,导致抽象类无法被加载
        throw new UnsupportedTypeException(clazz);
    }

    private static XmlElementParser getSpecifiedXmlElementParser(Class<? extends XmlElementParser> clazz)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (XmlElementParser.None.class == clazz) {
            return null;
        }
        if (!ClassUtil.isConcrete(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + " must be concrete class");
        }
        return ConstructorUtil.newObjectWithNoneParam(clazz);
    }

}
