package com.egls.server.utils.databind.json;

import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 这是一个标记型接口,用来标记一个对象是Json数据绑定对象
 * 由于javax.databind的json功能特别的强大, 在这里增加了一些限定规则,方便项目使用和维护
 * Note: 这里并不支持所有Json的使用方法,只是用Json来进行当作工程可用、人类可读的序列化工具来使用的,Json本身工具已经非常丰富了.
 * Note: 这里设计上就不支持直接反序列化List或者Map等对象.工程上不具有可扩展性.
 *
 * <pre>
 *     所有序列化对象必须实现本接口, 使用规则和检查规则如下:
 *
 *     使用规则: 只有实现本接口的类, 才能被接受作为序列化方法的参数
 *
 *     使用&检查规则: 所有实现本接口的类, 必须存在无参构造函数
 *     使用&检查规则: 所有实现本接口的类, {@link JsonProperty}不能被标注在Method上
 *     使用&检查规则: 所有实现本接口的类, {@link JsonProperty}不能被标注在static Field上
 *     使用&检查规则: 所有实现本接口的类, Field必须标注{@link JsonIgnore}或{@link JsonProperty}, 即Field要么是不序列化的, 要序列化必须指定名称
 *     使用&检查规则: 所有实现本接口的类, 如果本接口被标注在父类,则子类必须存在{@link JsonTypeName}, 父类必须存在{@link JsonTypeInfo}, 保证多态反序列化能够顺利执行
 *     使用&检查规则: 所有实现本接口的类, Field除8种基础类型, String, Enum之外的对象都必须实现本接口. {@link Collection} 和 {@link Map}的内部泛型也是检测的.
 *     使用&检查规则: 所有实现本接口的类, 所有被标注{@link JsonTypeName}的值不允许重复,防止多态反序列化冲突.会自动调用{@link ObjectMapper#registerSubtypes}进行注册多态.
 * </pre>
 *
 * @author mayer - [Created on 2018-08-29 19:58]
 * @see JsonObjectMapper
 */
public interface JsonObject {

    /**
     * 设计对象保存为Json时的一些映射方式. 专门是为了持久化方式做了一些考量的.
     */
    JsonObjectMapper JSON_OBJECT_MAPPER = new JsonObjectMapper(ObjectMappers.PERSISTENCE_OBJECT_MAPPER);

    /**
     * 本方法使用者应当进行调用,用来执行规则检查.
     * Note: 在使用之前必须调用此方法,因为必须要进行子类注册,否则无法使用多态.
     *
     * @param databindPackageNames 需要进行检测的包,如果不指定,则检测ClassLoader的所有Urls
     */
    static void ensureJsonObjectsValid(String... databindPackageNames) {
        JSON_OBJECT_MAPPER.ensureJsonObjectsValid(databindPackageNames);
    }

    /**
     * 序列化方法,将对象变成Json字符串
     *
     * @param object 对象
     * @return Json字符串
     */
    static String serialize(final Object object) {
        return JSON_OBJECT_MAPPER.serialize(object);
    }

    /**
     * 反序列化方法,将Json字符串变为对象.
     * 这里设计上就不支持直接反序列化List或者Map等对象.工程上不具有可扩展性.
     *
     * @param string    Json字符串
     * @param valueType 对象的类型
     * @return 对象
     */
    static <T> T deserialize(final String string, final Class<T> valueType) {
        return JSON_OBJECT_MAPPER.deserialize(string, valueType);
    }

}
