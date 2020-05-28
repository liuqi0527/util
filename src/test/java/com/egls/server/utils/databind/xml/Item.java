package com.egls.server.utils.databind.xml;

import com.egls.server.utils.databind.xml.annotation.XmlAttribute;
import com.egls.server.utils.databind.xml.annotation.XmlElement;
import com.egls.server.utils.databind.xml.annotation.XmlIgnorable;

/**
 * @author mayer - [Created on 2018-09-03 18:24]
 */
class Item implements XmlObject {

    @XmlElement("itemId.id")
    String itemId;

    @XmlIgnorable
    @XmlElement("itemId.nid")
    String itemNid = "none";

    @XmlElement("buyPrice")
    int buyPrice;

    @XmlElement("sellPrice")
    int sellPrice;

    @XmlAttribute("name")
    String name;

}