package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.databind.xml.XmlObject;

/**
 * 表示在当前节点,提取Value节点中的某个节点作为Key.所以命名为ExtractMap
 * 支持 8种基础类型, 8种基础类型的包装类, String, Enum, {@link XmlObject}
 * <pre>
 *     for example:
 *
 *     xml内容:
 *     <root>
 *          <title>
 *              <id>title_id_1</id>
 *              <name>title_name_1</name>
 *              <price>100</price>
 *              <buff>buff_1</buff>
 *          </title>
 *          <title>
 *              <id>title_id_1</id>
 *              <name>title_name_1</name>
 *              <price>100</price>
 *              <buff>buff_1</buff>
 *          </title>
 *     </root>
 *
 * 	    当前{@link XmlObject}位于root节点, 必须是实现类型,不能是接口或者抽象类,下方演示使用代码,:
 *
 *      title是组成Map[String,Title]的,其中的key是title节点的id节点
 * 	    XmlElementExtractMap(key = "id", value = "title")
 * 	    private HashMap map;
 * </pre>
 *
 * @author mayer - [Created on 2018-08-26 01:56]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlElementExtractMap {

    /**
     * key的节点名称
     */
    String key();

    /**
     * 指定的特殊的解析方式
     */
    Class<? extends XmlElementParser> keyParser() default XmlElementParser.None.class;

    /**
     * value的节点名称
     */
    String value();

    /**
     * 指定的特殊的解析方式
     */
    Class<? extends XmlElementParser> valueParser() default XmlElementParser.None.class;

}
