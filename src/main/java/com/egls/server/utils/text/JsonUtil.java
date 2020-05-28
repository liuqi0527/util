package com.egls.server.utils.text;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 提供一些操作json的工具方法
 *
 * @author mayer - [Created on 2018-08-09 22:01]
 */
public final class JsonUtil {

    public static String toXml(final String parentElementName, final JSONObject jsonObject) {
        StringBuilder xml = new StringBuilder();
        xml.append(newXmlElementBracket(parentElementName, true));

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);
            appendToXml(xml, key, value);
        }

        xml.append(newXmlElementBracket(parentElementName, false));
        return xml.toString();
    }

    public static String toXml(final String parentElementName, final JSONArray jsonArray) {
        StringBuilder xml = new StringBuilder();
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            Object object = jsonArray.get(i);
            appendToXml(xml, parentElementName, object);
        }
        return xml.toString();
    }

    private static void appendToXml(final StringBuilder xml, final String parentElementName, final Object object) {
        if (JSONObject.NULL.equals(object)) {
            //do nothing
        } else if (object instanceof JSONObject) {
            xml.append(toXml(parentElementName, (JSONObject) object));
        } else if (object instanceof JSONArray) {
            xml.append(toXml(parentElementName, (JSONArray) object));
        } else {
            xml.append(newXmlElementBracket(parentElementName, true));
            xml.append(object);
            xml.append(newXmlElementBracket(parentElementName, false));
        }
    }

    private static String newXmlElementBracket(String name, boolean begin) {
        return begin ? "<" + name + ">" : "</" + name + ">";
    }

}
