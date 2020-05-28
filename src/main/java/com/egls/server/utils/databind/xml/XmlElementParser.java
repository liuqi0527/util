package com.egls.server.utils.databind.xml;

import org.w3c.dom.Element;

/**
 * 特殊的转换规则, 使用此接口进行定义.
 *
 * @author mayer - [Created on 2018-08-26 02:05]
 */
public interface XmlElementParser {

    /**
     * This marker class is only to be used with annotations, to indicate that no parser is configured.
     */
    interface None extends XmlElementParser {
        //
    }

    /**
     * 有一些特殊的节点转换, 这里自定义转换方法
     *
     * @param element 节点
     * @return 解析后的对象
     */
    Object parseXmlElementToObject(Element element);

}
