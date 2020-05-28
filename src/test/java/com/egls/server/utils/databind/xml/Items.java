package com.egls.server.utils.databind.xml;

import java.util.HashMap;
import java.util.LinkedList;

import com.egls.server.utils.databind.xml.annotation.XmlElementCollection;
import com.egls.server.utils.databind.xml.annotation.XmlElementDeclineMap;
import com.egls.server.utils.databind.xml.annotation.XmlElementExtractMap;

/**
 * @author mayer - [Created on 2018-09-03 18:30]
 */
class Items implements XmlObject {

    public enum ItemId {
        item0,
        item1,
        item2,
        item3
    }

    @XmlElementCollection(value = "item")
    LinkedList<Item> items;

    @XmlElementExtractMap(key = "itemId.id", value = "item")
    HashMap<ItemId, Item> map1;

    @XmlElementDeclineMap(element = "drop", key = "id", value = "item")
    HashMap<ItemId, Item[]> map2;


}
