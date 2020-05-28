package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.databind.xml.XmlObject;

/**
 * 表示配置在当前节点的子节点, 子节点只能有1个.
 * 支持 8种基础类型, 8种基础类型的包装类, String, Enum, {@link XmlObject}
 * <pre>
 *     for example:
 *
 *     xml内容:
 *     <root>
 *          <id>id_1</id>
 *          <hobby>football</hobby>
 *          <hobby>basketball</hobby>
 *     </root>
 *
 *     当前{@link XmlObject}位于root节点,下方演示使用代码:
 *
 *     XmlElement("id")
 *     private String id;
 *
 *     XmlElement("hobby")
 *     private String[] hobbies;
 *
 * </pre>
 *
 * @author mayer - [Created on 2018-08-26 01:35]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlElement {

    /**
     * xml节点名称
     */
    String value();

    /**
     * 指定的特殊的解析方式
     */
    Class<? extends XmlElementParser> parser() default XmlElementParser.None.class;

}
