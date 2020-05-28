package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.databind.xml.XmlObject;

/**
 * 表示配置在当前节点的子节点, 子节点可以是多个声明为数组
 * 支持 8种基础类型, 8种基础类型的包装类, String, Enum, {@link XmlObject}
 * <pre>
 *    for example:
 *
 *    xml内容:
 *    <root>
 *         <reputation>reputation1</reputation>
 *         <reputation>reputation2</reputation>
 *         <reputation>reputation3</reputation>
 *         <reputation>reputation4</reputation>
 *         <reputation>reputation5</reputation>
 *         <reputation>reputation6</reputation>
 *    </root>
 *
 *    当前{@link XmlObject}位于root节点, 必须是实现类型,不能是接口或者抽象类, 下方演示使用代码:
 *
 *    XmlElementArray("reputation")
 *    private String[] reputations;
 * </pre>
 *
 * @author mayer - [Created on 2018-08-27 20:26]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlElementArray {

    /**
     * xml节点名称
     */
    String value();

    /**
     * 指定的特殊的解析方式
     */
    Class<? extends XmlElementParser> parser() default XmlElementParser.None.class;
}
