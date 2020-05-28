package com.egls.server.utils.databind.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 一些对象转换Json的映射规则
 *
 * @author mayer - [Created on 2018-09-20 16:42]
 */
public class ObjectMappers {

    /**
     * 专门为持久化做了一些设置的对象到Json的映射规则.
     */
    public static final ObjectMapper PERSISTENCE_OBJECT_MAPPER = new ObjectMapper();

    static {

        //支持序列化时间,JSR-310
        PERSISTENCE_OBJECT_MAPPER.registerModule(new JavaTimeModule());

        //设置只能关注Field
        PERSISTENCE_OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        PERSISTENCE_OBJECT_MAPPER.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        PERSISTENCE_OBJECT_MAPPER.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        PERSISTENCE_OBJECT_MAPPER.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.NONE);
        PERSISTENCE_OBJECT_MAPPER.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);

        //没有属性的类也需要序列化
        PERSISTENCE_OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        //属性为Object、抽象类、接口时序列化类型信息
        PERSISTENCE_OBJECT_MAPPER.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);

    }

}
