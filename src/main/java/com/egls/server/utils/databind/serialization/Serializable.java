package com.egls.server.utils.databind.serialization;

/**
 * 表示一个可序列化的对象.接口的实现方法,是顺序方式进行序列化数据的.
 * Note: 实现此接口的类,必须拥有一个无参构造器.
 *
 * @author mayer - [Created on 2018-08-28 20:06]
 */
public interface Serializable {

    /**
     * 序列化方法,通过{@link Serialization}将数据变成字符串
     *
     * @return 序列化字符串
     */
    String serialize();

    /**
     * 反序列化方法,通过{@link Serialization}将字符串变为数据
     *
     * @param serializedString 序列化字符串
     */
    void deserialize(final String serializedString);

}
