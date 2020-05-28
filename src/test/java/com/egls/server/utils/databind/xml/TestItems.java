package com.egls.server.utils.databind.xml;

import java.util.HashMap;
import java.util.LinkedList;

import com.egls.server.utils.text.XmlUtil;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 * @author mayer - [Created on 2018-09-04 11:33]
 */
public class TestItems {

    @Test
    public void test0() throws Exception {
        Element element = XmlUtil.loadDocumentElement("./src/test/java/com/egls/server/utils/databind/xml/items.xml");
        Items items = new Items();
        items.load(element);
        //list
        Assert.assertEquals(4, items.items.size());
        Assert.assertTrue(items.items instanceof LinkedList);
        Assert.assertEquals("item1", items.items.get(1).itemId);
        Assert.assertEquals("none", items.items.get(2).itemNid);
        Assert.assertEquals("item1_name", items.items.get(1).name);
        Assert.assertEquals(1, items.items.get(1).buyPrice);
        Assert.assertEquals(1, items.items.get(1).sellPrice);

        //map1
        Assert.assertEquals(4, items.map1.size());
        Assert.assertTrue(items.map1 instanceof HashMap);
        Assert.assertEquals("item1", items.map1.get(Items.ItemId.item1).itemId);
        Assert.assertEquals("item1_name", items.map1.get(Items.ItemId.item1).name);
        Assert.assertEquals(1, items.map1.get(Items.ItemId.item1).buyPrice);
        Assert.assertEquals(1, items.map1.get(Items.ItemId.item1).sellPrice);

        //map2
        Assert.assertEquals(4, items.map2.size());
        Assert.assertTrue(items.map2 instanceof HashMap);
        Assert.assertEquals(1, items.map2.get(Items.ItemId.item1).length);
        Assert.assertEquals("item1_name", items.map2.get(Items.ItemId.item1)[0].name);
        Assert.assertEquals(1, items.map2.get(Items.ItemId.item1)[0].buyPrice);
        Assert.assertEquals(1, items.map2.get(Items.ItemId.item1)[0].sellPrice);

    }

}
