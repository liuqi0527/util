package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.databind.xml.XmlObject;

/**
 * 表示在当前节点开始,下降节点层级获取同级的Key和Value.所以定为DeclineMap.
 * 支持 8种基础类型, 8种基础类型的包装类, String, Enum, {@link XmlObject}
 * <pre>
 *     for example:
 *
 *     xml内容:
 *     <root>
 * 			<attribute>
 * 				<id>boss</id>
 * 				<target>boss</target>
 * 			</attribute>
 * 			<attribute>
 * 				<id>fast</id>
 * 				<target>monster1</target>
 * 			</attribute>
 *
 * 			<drop>
 * 				<id>noDrop</id>
 * 				<target>
 * 				    <pro>500</pro>
 * 				    <item></item>
 * 				    <currency></currency>
 * 				    <count></count>
 * 				</target>
 * 			</drop>
 * 			<drop>
 * 				<id>monster1</id>
 * 				<target>
 * 				    <pro>500</pro>
 * 				    <item></item>
 * 				    <currency></currency>
 * 				    <count></count>
 * 				</target>
 * 			</drop>
 * 		</root>
 *
 * 	    当前{@link XmlObject}位于root节点, 必须是实现类型,不能是接口或者抽象类,下方演示使用代码,:
 *
 *      普通类型, attribute是组合成Map[String,String]的.
 *      XmlElementDeclineMap(element = "attribute", key = "id", value = "target")
 *      private HashMap map;
 *
 *      高级类型, drop是组合成Map[String,Drop]的.Drop是{@link XmlObject}的子类
 *      XmlElementDeclineMap(element ="drop", key = "id", value = "target")
 *      private HashMap map;
 *
 * </pre>
 *
 * @author mayer - [Created on 2018-08-26 01:49]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlElementDeclineMap {

    /**
     * xml节点名称
     */
    String element();

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
