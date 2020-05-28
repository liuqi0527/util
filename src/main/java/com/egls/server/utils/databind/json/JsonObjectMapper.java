package com.egls.server.utils.databind.json;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import com.egls.server.utils.clazz.ClassUtil;
import com.egls.server.utils.exception.UnsupportedTypeException;
import com.egls.server.utils.file.FileType;
import com.egls.server.utils.reflect.ConstructorUtil;
import com.egls.server.utils.reflect.FieldUtil;
import com.egls.server.utils.reflect.MethodUtil;
import com.egls.server.utils.reflect.TypeUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object 和 Json之间的转换器. 可以自定义Object 和 Json之间的映射规则.
 * <p>
 * Note: 这里并不支持所有Json的使用方法,只是用Json来进行当作工程可用、人类可读的序列化工具来使用的,Json本身工具已经非常丰富了.
 * Note: 这里设计上就不支持直接反序列化List或者Map等对象.工程上不具有可扩展性.
 *
 * @author mayer - [Created on 2018-08-30 13:53]
 */
public final class JsonObjectMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonObjectMapper.class);

    private final ObjectMapper objectMapper;

    public JsonObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String serialize(final Object object) {
        if (object instanceof JsonObject) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException exception) {
                throw new RuntimeException(exception);
            }
        }
        throw new UnsupportedTypeException(object.getClass());
    }

    public <T> T deserialize(final String string, final Class<T> valueType) {
        if (JsonObject.class.isAssignableFrom(valueType)) {
            try {
                return objectMapper.readValue(string, valueType);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        throw new UnsupportedTypeException(valueType);
    }

    /**
     * 检测指定包的class
     *
     * @param databindPackageNames 指定的包, 不指定检查所有包
     * @see ConfigurationBuilder#build(Object...)
     * @see ConfigurationBuilder#ConfigurationBuilder()
     */
    public void ensureJsonObjectsValid(String... databindPackageNames) {
        final ConfigurationBuilder configuration;
        if (ArrayUtils.isEmpty(databindPackageNames)) {
            //检查所有的class
            configuration = ConfigurationBuilder.build();
            configuration.setExpandSuperTypes(false);
            configuration.setScanners(new SubTypesScanner());
            configuration.setInputsFilter(new FilterBuilder.Include(FileType.CLASS.getPatternString()));
        } else {
            //检查指定的包的class
            configuration = new ConfigurationBuilder();
            configuration.setExpandSuperTypes(false);
            configuration.setScanners(new SubTypesScanner());
            for (String databindPackageName : databindPackageNames) {
                configuration.addUrls(ClasspathHelper.forPackage(databindPackageName));
            }
            configuration.setInputsFilter(new FilterBuilder.Include(FileType.CLASS.getPatternString()));
        }
        ensureJsonObjectsValid(new Reflections(configuration));
    }

    private void ensureJsonObjectsValid(final Reflections reflections) {
        final List<Class<? extends JsonObject>> jsonObjectClasses = Lists.newArrayList(reflections.getSubTypesOf(JsonObject.class));

        //1.必须存在无参构造函数
        for (Class<? extends JsonObject> jsonObjectClass : jsonObjectClasses) {
            ensureNoneParamConstructor(jsonObjectClass);
        }

        //2.JsonProperty不能被标注在Method上
        for (Class<? extends JsonObject> jsonObjectClass : jsonObjectClasses) {
            ensureNoneJsonPropertyOnMethod(jsonObjectClass);
        }

        //3.JsonProperty不能被标注在static Field上
        for (Class<? extends JsonObject> jsonObjectClass : jsonObjectClasses) {
            ensureNoneJsonPropertyOnStaticField(jsonObjectClass);
        }

        //4.Field必须标注JsonIgnore或JsonProperty
        for (Class<? extends JsonObject> jsonObjectClass : jsonObjectClasses) {
            ensureCorrectFieldAnnotation(jsonObjectClass);
        }

        //5.检查JsonTypeInfo和JsonTypeName
        for (Class<? extends JsonObject> jsonObjectClass : jsonObjectClasses) {
            ensureSubtypes(reflections, jsonObjectClass);
        }

        //6.检查Field是否全部是基础类型或者是实现接口的.
        for (Class<? extends JsonObject> jsonObjectClass : jsonObjectClasses) {
            ensureCorrectFieldType(jsonObjectClass);
        }

        //7.注册JsonTypeName
        registerSubtypes(jsonObjectClasses);
    }

    private void registerSubtypes(final List<Class<? extends JsonObject>> jsonObjectClasses) {
        final Set<String> nameSet = new HashSet<>();
        for (Class<? extends JsonObject> jsonObjectClass : jsonObjectClasses) {
            if (jsonObjectClass.isAnnotationPresent(JsonTypeName.class)) {
                final String name = jsonObjectClass.getAnnotation(JsonTypeName.class).value();
                if (nameSet.contains(name)) {
                    LOGGER.error("JsonTypeName {} is conflicting on {}", name, jsonObjectClass.getName());
                } else {
                    nameSet.add(name);
                    objectMapper.registerSubtypes(jsonObjectClass);
                }
            }
        }
    }

    private void ensureCorrectFieldType(final Class<? extends JsonObject> jsonObjectClass) {
        final List<Field> fields = FieldUtil.getAllFields(jsonObjectClass);
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (validateType(fieldType)) {
                continue;
            }
            if (Collection.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(fieldType)) {
                if (validateGenericType(field.getGenericType())) {
                    continue;
                }
            }
            LOGGER.error("JsonObject has an illegal type field, class: {}, field: {}", jsonObjectClass.getName(), field.getName());
        }
    }

    private boolean validateType(Class<?> type) {
        if (TypeUtil.isBasicType(type) || TypeUtil.isBasicTypeArray(type)) {
            return true;
        }
        if (JsonObject.class.isAssignableFrom(type)) {
            return true;
        }
        if (type.isArray()) {
            return validateType(type.getComponentType());
        }
        //其他的没办法,一点一点加. 比如JSR-310里的LocalDateTime
        return LocalDate.class.isAssignableFrom(type)
                || LocalDateTime.class.isAssignableFrom(type)
                || ZonedDateTime.class.isAssignableFrom(type);
    }

    private boolean validateGenericType(final Type genericType) {
        if (genericType instanceof ParameterizedType) {
            final Type[] genericActualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            for (Type genericActualType : genericActualTypes) {
                if (genericActualType instanceof Class) {
                    if (!validateType((Class<?>) genericActualType)) {
                        return false;
                    }
                } else if (genericActualType instanceof ParameterizedType) {
                    if (!validateGenericType(genericActualType)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureSubtypes(final Reflections reflections, final Class jsonObjectClass) {
        final Set<Class> subtypes = (Set<Class>) reflections.getSubTypesOf(jsonObjectClass);
        if (subtypes.isEmpty()) {
            return;
        }
        if (!jsonObjectClass.isAnnotationPresent(JsonTypeInfo.class)) {
            LOGGER.error("missing annotation JsonTypeInfo on abstract class, class: {}", jsonObjectClass.getName());
        }
        if (ClassUtil.isConcrete(jsonObjectClass)) {
            if (!jsonObjectClass.isAnnotationPresent(JsonTypeName.class)) {
                LOGGER.error("missing annotation JsonTypeName on derived class, class: {}", jsonObjectClass.getName());
            }
        }
        for (Class subtype : subtypes) {
            if (!subtype.isAnnotationPresent(JsonTypeName.class)) {
                LOGGER.error("missing annotation JsonTypeName on derived class, class: {}", subtype.getName());
            }
        }
    }

    private void ensureCorrectFieldAnnotation(final Class<? extends JsonObject> jsonObjectClass) {
        final List<Field> fields = FieldUtil.getAllFields(jsonObjectClass);
        for (Field field : fields) {
            if (!field.isAnnotationPresent(JsonIgnore.class) && !field.isAnnotationPresent(JsonProperty.class)) {
                LOGGER.error("missing annotation JsonIgnore or JsonProperty on field, class: {}, field: {}", jsonObjectClass.getName(), field.getName());
            }
            if (field.isAnnotationPresent(JsonProperty.class)) {
                final JsonProperty annotation = field.getAnnotation(JsonProperty.class);
                if (StringUtils.isBlank(annotation.value())) {
                    LOGGER.error("missing JsonProperty annotation value, class: {}, field: {}", jsonObjectClass.getName(), field.getName());
                }
            }
        }
    }

    private void ensureNoneJsonPropertyOnStaticField(final Class<? extends JsonObject> jsonObjectClass) {
        final List<Field> fields = FieldUtil.getAllFieldsIncludeStatic(jsonObjectClass);
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(JsonProperty.class)) {
                LOGGER.error("can't annotate JsonProperty on static field, class: {}, field: {}", jsonObjectClass.getName(), field.getName());
            }
        }
    }

    private void ensureNoneJsonPropertyOnMethod(final Class<? extends JsonObject> jsonObjectClass) {
        final List<Method> methods = MethodUtil.getAllMethodsIncludeStatic(jsonObjectClass);
        for (Method method : methods) {
            if (method.isAnnotationPresent(JsonProperty.class)) {
                LOGGER.error("can't annotate JsonProperty on method, class: {}, method: {}", jsonObjectClass.getName(), method.getName());
            }
        }
    }

    private void ensureNoneParamConstructor(final Class<? extends JsonObject> jsonObjectClass) {
        if (jsonObjectClass.isEnum() || !ClassUtil.isConcrete(jsonObjectClass)) {
            return;
        }
        if (!ConstructorUtil.containsNoneParamConstructor(jsonObjectClass)) {
            LOGGER.error(jsonObjectClass.getName() + " can't found none param constructor");
        }
    }

}
