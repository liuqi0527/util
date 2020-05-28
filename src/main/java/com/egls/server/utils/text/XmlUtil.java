package com.egls.server.utils.text;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.egls.server.utils.CharsetUtil;
import com.egls.server.utils.StringUtil;
import com.egls.server.utils.file.FileUtil;
import com.egls.server.utils.function.StringCaster;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 提供操作Xml文本的一些工具方法
 * 注意本类的返回方法凡是返回为空的都是不可修改的
 *
 * @author mayer - [Created on 2018-08-09 22:04]
 */
public final class XmlUtil {

    private static final char ELEMENT_NAME_SEPARATOR_CHAR = '.';

    private static final String XML_TITLE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    private static final Element[] EMPTY_ELEMENT_ARRAY = new Element[0];

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = getDocumentBuilderFactory();

    public static Document loadDocument(final String filePath) throws IOException, ParserConfigurationException, SAXException {
        if (FileUtil.exists(filePath)) {
            return loadDocument(new File(filePath));
        }
        throw new FileNotFoundException("file doesn't exists : " + filePath);
    }

    public static Document loadDocument(final File file) throws IOException, SAXException, ParserConfigurationException {
        if (FileUtil.exists(file)) {
            return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(file);
        }
        throw new FileNotFoundException("file doesn't exists : " + file.getAbsolutePath());
    }

    public static Document loadDocumentFromString(final String content) throws ParserConfigurationException, IOException, SAXException {
        char[] chars = content.toCharArray();
        InputSource inputSource = new InputSource(new CharArrayReader(chars));
        return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(inputSource);
    }

    public static Document loadDocumentFromWithoutTitleString(final String content) throws IOException, SAXException, ParserConfigurationException {
        return loadDocumentFromString(addXmlTitle(content));
    }

    public static Element loadDocumentElement(final String filePath) throws ParserConfigurationException, SAXException, IOException {
        return loadDocument(filePath).getDocumentElement();
    }

    public static Element loadDocumentElement(final File file) throws ParserConfigurationException, SAXException, IOException {
        return loadDocument(file).getDocumentElement();
    }

    public static Element loadDocumentElementFromString(final String content) throws ParserConfigurationException, IOException, SAXException {
        return loadDocumentFromString(content).getDocumentElement();
    }

    public static Element loadDocumentElementFromWithoutTitleString(final String content) throws IOException, SAXException, ParserConfigurationException {
        return loadDocumentFromString(addXmlTitle(content)).getDocumentElement();
    }

    public static String toXmlString(final Document document) {
        return getXmlTitle() + StringUtil.getLineSeparator() + toXmlString(document.getDocumentElement());
    }

    public static String toXmlString(final Element element) {
        StringBuilder stringBuilder = new StringBuilder();
        toXmlString(0, element, stringBuilder);
        return StringUtils.trim(stringBuilder.toString());
    }

    private static String toXmlString(final int layer, final Element element, final StringBuilder stringBuilder) {
        Element[] children = getChildren(element);
        String name = getName(element);
        stringBuilder.append(StringUtil.getLineSeparator());
        for (int i = 0; i < layer; i++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append("<").append(name).append(">");
        if (ArrayUtils.isEmpty(children)) {
            stringBuilder.append(getText(element));
        } else {
            for (Element child : children) {
                toXmlString(layer + 1, child, stringBuilder);
            }
            stringBuilder.append(StringUtil.getLineSeparator());
            for (int i = 0; i < layer; i++) {
                stringBuilder.append("\t");
            }
        }
        stringBuilder.append("</").append(name).append(">");
        return stringBuilder.toString();
    }

    public static String formatXml(final String xml) throws ParserConfigurationException, SAXException, IOException {
        return XmlUtil.toXmlString(XmlUtil.loadDocumentElementFromWithoutTitleString(xml));
    }

    /**
     * 全路径名称
     */
    private static boolean isFullName(final String name) {
        return StringUtils.contains(name, ELEMENT_NAME_SEPARATOR_CHAR);
    }

    /**
     * fullName like "root.config.type.str"
     */
    public static Element getElementByFullName(final Document document, final String fullName) {
        return getElementByFullName(document.getDocumentElement(), fullName);
    }

    /**
     * fullName like "root.config.type.str",相对于参数的Element来说的full name
     */
    public static Element getElementByFullName(final Element element, final String fullName) {
        String[] array = StringUtils.split(fullName, ELEMENT_NAME_SEPARATOR_CHAR);
        Element result = element;
        for (int i = 1; i < array.length; i++) {
            result = getChild(result, array[i]);
        }
        return result;
    }

    public static String getElementFullName(final Element element) {
        Element temp = element;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getName(temp));
        Element parent;
        while ((parent = getParent(temp)) != null) {
            stringBuilder.insert(0, ELEMENT_NAME_SEPARATOR_CHAR).insert(0, getName(parent));
            temp = parent;
        }
        return stringBuilder.toString();
    }

    public static Element getParent(final Element element) {
        Node node = element.getParentNode();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return (Element) node;
        }
        return null;
    }

    public static Element[] getChildren(final Element parent) {
        if (parent == null) {
            return EMPTY_ELEMENT_ARRAY;
        }
        NodeList nodes = parent.getChildNodes();
        LinkedHashSet<Element> set = new LinkedHashSet<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                set.add((Element) node);
            }
        }
        return set.isEmpty() ? EMPTY_ELEMENT_ARRAY : set.toArray(new Element[]{});
    }

    public static String[] getChildrenName(final Element parent) {
        Element[] elements = getChildren(parent);
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (Element element : elements) {
            set.add(element.getNodeName());
        }
        return set.isEmpty() ? ArrayUtils.EMPTY_STRING_ARRAY : set.toArray(new String[]{});
    }

    public static String[] getRepeatedChildrenName(final Element parent) {
        Element[] elements = getChildren(parent);
        List<String> list = new ArrayList<>(elements.length);
        for (Element element : elements) {
            list.add(element.getNodeName());
        }
        return list.isEmpty() ? ArrayUtils.EMPTY_STRING_ARRAY : list.toArray(new String[]{});
    }

    public static Element[] getChildren(final Element parent, final String name) {
        if (isFullName(name)) {
            return getChildrenByFullName(parent, name);
        }
        if (parent == null) {
            return EMPTY_ELEMENT_ARRAY;
        }
        NodeList nodes = parent.getChildNodes();
        List<Element> list = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals(name)) {
                list.add((Element) node);
            }
        }
        return list.isEmpty() ? EMPTY_ELEMENT_ARRAY : list.toArray(new Element[]{});
    }

    /**
     * 根据full name读取节点, full name "a.b.c" , a.b单节点，c多节点
     */
    public static Element[] getChildrenByFullName(final Element parent, final String fullName) {
        if (!isFullName(fullName)) {
            return EMPTY_ELEMENT_ARRAY;
        }
        String[] path = StringUtils.split(fullName, ELEMENT_NAME_SEPARATOR_CHAR);
        Element e = parent;
        for (int i = 0; i < path.length - 1; i++) {
            e = getChild(e, path[i]);
            if (e == null) {
                return EMPTY_ELEMENT_ARRAY;
            }
        }
        return getChildren(e, path[path.length - 1]);
    }

    public static Element getChild(final Element parent, final String name) {
        Element[] array = getChildren(parent, name);
        if (ArrayUtils.isEmpty(array)) {
            return null;
        } else if (array.length > 1) {
            throw new IllegalStateException(String.format("too many '%s' elements be found, sum : (%d).", name, array.length));
        } else {
            return array[0];
        }
    }

    public static String getName(final Element element) {
        return element == null ? null : element.getNodeName();
    }

    public static String getText(final Element element) {
        return element == null ? null : element.getTextContent();
    }

    /**
     * <pre>
     *     for example:
     *     XmlUtil.getValue(element, Integer::valueOf) >= 0
     * </pre>
     */
    public static <T> T getValue(final Element element, final StringCaster<T> stringCaster) {
        return stringCaster.cast(getText(element));
    }

    public static String[] getTexts(final Element[] elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        String[] ret = new String[elements.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getText(elements[i]);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] getValues(final Element[] elements, final StringCaster<T> stringCaster) {
        if (ArrayUtils.isEmpty(elements)) {
            return null;
        }
        T first = getValue(elements[0], stringCaster);
        T[] array = (T[]) Array.newInstance(first.getClass(), elements.length);
        array[0] = first;
        for (int i = 1; i < elements.length; i++) {
            Array.set(array, i, getValue(elements[i], stringCaster));
        }
        return array;
    }

    public static String getAttribute(final Element element, final String name) {
        return element == null ? null : element.getAttribute(name);
    }

    /**
     * <pre>
     *     for example:
     *     XmlUtil.getAttribute(element, "price", Integer::valueOf) >= 0
     * </pre>
     */
    public static <T> T getAttribute(final Element element, final String name, final StringCaster<T> stringCaster) {
        return stringCaster.cast(getAttribute(element, name));
    }

    public static Map<String, String> getAttributes(final Element element) {
        Map<String, String> result = new LinkedHashMap<>();
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                result.put(node.getNodeName(), node.getNodeValue());
            }
        }
        return result;
    }

    public static <T> Map<String, T> getAttributes(final Element element, final StringCaster<T> stringCaster) {
        Map<String, String> temp = getAttributes(element);
        Map<String, T> result = new LinkedHashMap<>();
        temp.forEach((key, value) -> result.put(key, stringCaster.cast(value)));
        return result;
    }

    public static String getChildAttribute(final Element parent, final String name, final String attribute) {
        return getAttribute(getChild(parent, name), attribute);
    }

    public static <T> T getChildAttributeValue(final Element parent, final String name, final String attribute, final StringCaster<T> stringCaster) {
        return stringCaster.cast(getChildAttribute(parent, name, attribute));
    }

    public static String[] getChildrenAttribute(final Element parent, final String name, final String attribute) {
        Element[] children = getChildren(parent, name);
        if (ArrayUtils.isEmpty(children)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        String[] result = new String[children.length];
        for (int i = 0; i < children.length; i++) {
            result[i] = getAttribute(children[i], attribute);
        }
        return result;
    }

    /**
     * <pre>
     *     for example:
     *     XmlUtil.getChildrenAttribute(element, "price", "sell", Integer::valueOf).size() >= 0
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] getChildrenAttribute(final Element parent, final String name, final String attribute, final StringCaster<T> stringCaster) {
        String[] childrenAttribute = getChildrenAttribute(parent, name, attribute);
        if (ArrayUtils.isEmpty(childrenAttribute)) {
            return null;
        }
        T first = stringCaster.cast(childrenAttribute[0]);
        T[] array = (T[]) Array.newInstance(first.getClass(), childrenAttribute.length);
        array[0] = first;
        for (int i = 1; i < childrenAttribute.length; i++) {
            Array.set(array, i, stringCaster.cast(childrenAttribute[i]));
        }
        return array;
    }

    public static String getChildText(final Element parent, final String name) {
        return getText(getChild(parent, name));
    }

    /**
     * <pre>
     *     for example:
     *     XmlUtil.getChildValue(element, "price", Integer::valueOf) >= 0
     * </pre>
     */
    public static <T> T getChildValue(final Element parent, final String name, final StringCaster<T> stringCaster) {
        return stringCaster.cast(getChildText(parent, name));
    }

    public static String[] getChildrenText(final Element parent, final String name) {
        Element[] children = getChildren(parent, name);
        if (ArrayUtils.isEmpty(children)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        String[] result = new String[children.length];
        for (int i = 0; i < children.length; i++) {
            result[i] = getText(children[i]);
        }
        return result;
    }

    /**
     * <pre>
     *     for example:
     *     XmlUtil.getChildrenValue(element, "price", Integer::valueOf).size() >= 0
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] getChildrenValue(final Element parent, final String name, final StringCaster<T> stringCaster) {
        String[] childrenText = getChildrenText(parent, name);
        if (ArrayUtils.isEmpty(childrenText)) {
            return null;
        }
        T first = stringCaster.cast(childrenText[0]);
        T[] array = (T[]) Array.newInstance(first.getClass(), childrenText.length);
        array[0] = first;
        for (int i = 1; i < childrenText.length; i++) {
            Array.set(array, i, stringCaster.cast(childrenText[i]));
        }
        return array;
    }

    public static void saveDocumentToFile(final String filePath, final Document document) throws IOException, TransformerException {
        DOMSource domSource = new DOMSource(document);
        File file = new File(filePath);
        FileUtil.createFileOnNoExists(file);
        StreamResult streamResult = new StreamResult(file);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Properties properties = transformer.getOutputProperties();
        properties.setProperty(OutputKeys.ENCODING, CharsetUtil.defaultEncoding());
        properties.setProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperties(properties);
        transformer.transform(domSource, streamResult);
    }

    public static Document createBlankDocument(final String rootElementName) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.appendChild(document.createElement(rootElementName));
        return document;
    }

    public static void createChild(final Document document, final Element parent, final String name, final String text) {
        createChild(document, parent, name, text, null);
    }

    public static void createChild(final Document document, final Element parent, final String name, final String text, final String comment) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(text);
        Element child = document.createElement(name);
        child.appendChild(document.createTextNode(text));
        if (comment != null) {
            parent.appendChild(document.createComment(comment));
        }
        parent.appendChild(child);
    }

    public static void createComment(final Document document, final Element parent, final String comment) {
        Objects.requireNonNull(comment);
        parent.appendChild(document.createComment(comment));
    }

    public static void setElementText(final Document document, final Element parent, final String name, final String text) {
        Element element = getChild(parent, name);
        if (element != null) {
            element.setTextContent(text);
        } else {
            createChild(document, parent, name, text);
        }
    }

    public static String replaceXmlTextContentSpecialCharacters(final String string) {
        String xmlTextContent = string;
        xmlTextContent = StringUtils.replace(xmlTextContent, "\"", "&quot;");
        xmlTextContent = StringUtils.replace(xmlTextContent, "'", "&apos;");
        xmlTextContent = StringUtils.replace(xmlTextContent, "&", "&amp;");
        xmlTextContent = StringUtils.replace(xmlTextContent, "<", "&lt;");
        xmlTextContent = StringUtils.replace(xmlTextContent, ">", "&gt;");
        return xmlTextContent;
    }

    public static String getXmlTitle() {
        return XML_TITLE;
    }

    public static boolean isXmlText(final String text) {
        try {
            Document document = loadDocumentFromString(text);
            if (document != null) {
                return true;
            }
            document = loadDocumentFromWithoutTitleString(text);
            return document != null;
        } catch (Exception exception) {
            return false;
        }
    }

    private static String addXmlTitle(final String content) {
        return getXmlTitle() + StringUtil.getLineSeparator() + content;
    }

    private static DocumentBuilderFactory getDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(false);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setValidating(false);
        factory.setCoalescing(true);
        return factory;
    }

}