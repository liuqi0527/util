package com.egls.server.utils.file.loader;

import java.util.HashMap;
import java.util.Map;

import com.egls.server.utils.databind.xml.XmlElementParser;
import com.egls.server.utils.text.XmlUtil;

import org.w3c.dom.Element;

/**
 * @author mayer - [Created on 2018-09-04 11:23]
 */
class ReputationNames implements XmlElementParser {

    private static final String DEFAULT_NAME = "reputation";

    private static final Map<String, String> map = new HashMap<>();

    static {
        map.put("repu1_name", "reputation1_name");
        map.put("repu2_name", "reputation2_name");
    }

    private static String getName(String name) {
        return map.getOrDefault(name, DEFAULT_NAME);
    }

    @Override
    public Object parseXmlElementToObject(Element element) {
        return getName(XmlUtil.getText(element));
    }

}
