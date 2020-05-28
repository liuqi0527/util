package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.databind.xml.XmlObject;

/**
 * 表示配置在当前节点的子节点, 子节点可以是多个声明为集合
 * 支持 8种基础类型, 8种基础类型的包装类, String, Enum, {@link XmlObject}
 * <pre>
 *     for example:
 *
 *     xml内容:
 *     <root>
 *         <title>
 *             <id>title_id_1</id>
 *             <name>title_name_1</name>
 *             <price>100</price>
 *             <buff>buff_1</buff>
 *         </title>
 *         <title>
 *             <id>title_id_1</id>
 *             <name>title_name_1</name>
 *             <price>100</price>
 *             <buff>buff_1</buff>
 *         </title>
 *     </root>
 *
 *     当前{@link XmlObject}位于root节点, 必须是实现类型,不能是接口或者抽象类, 下方演示使用代码:
 *
 *     XmlElementCollection(element = "title")
 *     private ArrayList list;
 * </pre>
 *
 * @author mayer - [Created on 2018-08-26 01:42]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlElementCollection {

    /**
     * xml节点名称
     */
    String value();

    /**
     * 指定的特殊的解析方式
     */
    Class<? extends XmlElementParser> parser() default XmlElementParser.None.class;

}
