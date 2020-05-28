package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.databind.xml.XmlObject;

/**
 * 表示配置在当前节点的attribute, 支持 8种基础类型, 8种基础类型的包装类, String, Enum
 * <pre>
 *     for example:
 *
 *     xml内容:
 *     <root name="name_1"></root>
 *
 *     当前{@link XmlObject}位于root节点,下方演示使用代码:
 *
 *     XmlAttribute("name")
 *     private String name;
 * </pre>
 *
 * @author mayer - [Created on 2018-08-26 01:12]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlAttribute {

    /**
     * xml节点的属性名称
     */
    String value();

    /**
     * 指定的特殊的解析方式
     */
    Class<? extends XmlElementParser> parser() default XmlElementParser.None.class;

}
