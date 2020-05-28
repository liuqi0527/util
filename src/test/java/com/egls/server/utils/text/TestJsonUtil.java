package com.egls.server.utils.text;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-08-09 22:08]
 */
public class TestJsonUtil {

    @Test
    public void test1() {
        String jsonString = "{type:\"forceQuest\", para:{quest:\"testId\",type:\"unachieve\"}, display:[{item:\"dust\",count:1},{item:\"dust\",count:1}]}";

        JSONObject jsonObject = new JSONObject(jsonString);
        String xmlString = JsonUtil.toXml("function", jsonObject);

        Assert.assertTrue(XmlUtil.isXmlText(xmlString));
    }

    @Test
    public void test2() {
        String jsonString = "{type:\"forceQuest\", para:{quest:\"testId\",type:\"unachieve\",para:{quest:\"testId\",type:\"unachieve\"}}, display:[{item:\"dust\",count:1},{item:\"dust\",count:1},[{item:\"dust\",count:1},{item:\"dust\",count:1}]]}";

        JSONObject jsonObject = new JSONObject(jsonString);
        String xmlString = JsonUtil.toXml("function", jsonObject);

        Assert.assertTrue(XmlUtil.isXmlText(xmlString));
    }

}
