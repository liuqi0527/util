package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.databind.xml.XmlObject;

/**
 * 表示配置在当前节点的子节点上的attribute, 子节点是重复的, 可以声明为数组的.
 * 支持 8种基础类型, 8种基础类型的包装类, String, Enum
 * <pre>
 *     for example:
 *
 *     xml内容:
 *     <root>
 *          <test id="test_id_1" name="test_name_1"></test>
 *          <test id="test_id_2" name="test_name_2"></test>
 *          <test id="test_id_3" name="test_name_3"></test>
 *          <test id="test_id_4" name="test_name_4"></test>
 *          <test id="test_id_5" name="test_name_5"></test>
 *          <test id="test_id_6" name="test_name_6"></test>
 *          <test id="test_id_7" name="test_name_7"></test>
 *     </root>
 *
 *     当前{@link XmlObject}位于root节点,下方演示使用代码:
 *
 *     XmlAttributeArray(element = "test", attribute = "id")
 *     private String[] ids;
 *
 *     XmlAttributeArray(element = "test", attribute = "name")
 *     private String[] names;
 *
 * </pre>
 *
 * @author mayer - [Created on 2018-08-26 01:21]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlAttributeArray {

    /**
     * xml节点名称
     */
    String element();

    /**
     * xml节点的属性名称
     */
    String attribute();

    /**
     * 指定的特殊的解析方式
     */
    Class<? extends XmlElementParser> parser() default XmlElementParser.None.class;

}
