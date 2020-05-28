package com.egls.server.utils.file.loader;

import com.egls.server.utils.databind.xml.XmlObject;
import com.egls.server.utils.databind.xml.annotation.XmlElement;

/**
 * @author mayer - [Created on 2018-09-03 20:04]
 */
class Reputation implements XmlObject {

    @XmlElement("id")
    String id;

    @XmlElement("name")
    String oldName;

    @XmlElement("buff")
    String buff;

    @XmlElement(value = "name", parser = ReputationNames.class)
    String newName;

}
