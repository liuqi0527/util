package com.egls.server.utils.text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import com.egls.server.utils.file.TextFileUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;

/**
 * @author mayer - [Created on 2018-09-03 21:08]
 */
public class TestXmlUtil {

    @Test
    public void test() throws Exception {
        final String xmlContent = TextFileUtil.read(new File("./src/test/java/com/egls/server/utils/text/family.xml"), false);

        Document document = XmlUtil.loadDocumentFromString(xmlContent);
        Element root = document.getDocumentElement();
        Assert.assertNotNull(root);

        final HashSet<String> nameSet = new HashSet<>();
        final List<String> nameList = new ArrayList<>();
        final Element[] elements = XmlUtil.getChildren(root);
        for (Element element : elements) {
            nameSet.add(XmlUtil.getName(element));
            nameList.add(XmlUtil.getName(element));
        }

        assertEquals(nameList.size(), XmlUtil.getRepeatedChildrenName(root).length);
        assertEquals(nameSet.size(), XmlUtil.getChildrenName(root).length);

        assertEquals("familyNote", XmlUtil.getName(XmlUtil.getChild(root, "familyNote")));
        assertEquals("String_familyNote", XmlUtil.getText(XmlUtil.getChild(root, "familyNote")));
        assertEquals(4, XmlUtil.getChildren(root, "familylevelbonus").length);
        assertEquals(4, XmlUtil.getChildren(root, "familylevelbonus").length);
        assertEquals(2, XmlUtil.getChildren(root, "memberlevelupreward").length);
        assertEquals(2, XmlUtil.getChildren(root, "memberlevelupreward").length);

        document = XmlUtil.loadDocumentFromString(xmlContent);
        root = document.getDocumentElement();
        assertEquals(4, XmlUtil.getChildren(root, "familylevelbonus").length);
        assertEquals(4, XmlUtil.getChildren(root, "familylevelbonus").length);
        assertEquals(2, XmlUtil.getChildren(root, "memberlevelupreward").length);
        assertEquals(2, XmlUtil.getChildren(root, "memberlevelupreward").length);
        assertEquals("String_familyNote", XmlUtil.getChildValue(root, "familyNote", String::valueOf));
        assertEquals("String_familyNote", XmlUtil.getChildValue(root, "familyNote", String::valueOf));
        assertEquals(3, XmlUtil.getChildren(root, "support").length);
        assertEquals(3, XmlUtil.getChildren(root, "support").length);
        Element[] list = XmlUtil.getChildren(root, "support");
        for (Element element : list) {
            Assert.assertTrue(XmlUtil.getChildValue(element, "pic", String::valueOf).contains("button_support"));
            Assert.assertTrue(XmlUtil.getChildValue(element, "cant", String::valueOf).contains("String_guild_cant_support"));
            Assert.assertTrue(XmlUtil.getChildValue(element, "currency", Integer::decode) > 0);
            Assert.assertTrue(XmlUtil.getChildValue(element, "price", Integer::decode) >= 0);
        }
        assertEquals(15, (int) XmlUtil.getChildValue(root, "applyLevel", Integer::valueOf));
    }

    @Test
    public void test1() throws IOException, SAXException, ParserConfigurationException {
        final String xmlContent = "<root><a><b>testtest</b></a></root>";
        Document document = XmlUtil.loadDocumentFromString(xmlContent);
        Element root = document.getDocumentElement();
        Assert.assertTrue(StringUtils.isNotBlank(XmlUtil.getChildText(root, "a.b")));
    }

    @Test
    public void test2() throws IOException, SAXException, ParserConfigurationException {
        String xmlContent = "<root><a invincible=\"1\" stun=\"0\" slience=\"0\" disarm=\"0\" disable=\"0\">a</a></root>";
        Document document = XmlUtil.loadDocumentFromString(xmlContent);
        Element root = document.getDocumentElement();
        Map<String, Integer> map = XmlUtil.getAttributes(XmlUtil.getChild(root, "a"), Integer::decode);
//        map.forEach((k, v) -> System.out.println(k + "=" + v));
        assertEquals(1, (int) map.get("invincible"));
        assertEquals(0, (int) map.get("stun"));
        assertEquals(0, (int) map.get("slience"));
        assertEquals(0, (int) map.get("disarm"));
        assertEquals(0, (int) map.get("disable"));
    }

}
