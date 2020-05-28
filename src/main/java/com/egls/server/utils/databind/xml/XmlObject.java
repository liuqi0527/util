package com.egls.server.utils.databind.xml;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import com.egls.server.utils.databind.xml.annotation.*;
import com.egls.server.utils.exception.ExceptionUtil;
import com.egls.server.utils.reflect.FieldUtil;
import com.egls.server.utils.text.XmlUtil;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Element;

/**
 * <pre>
 *      xml对象的接口,此接口表示一个节点{@link Element}映射成一个java对象.
 *
 *      Note: 必须保证存在无参构造函数
 *      Note: 利用反射,无需声明Set方法
 *      Note: 静态成员内部类可以实现本接口达到载入的目的.
 *      Warning: 当作为内部类使用本接口时,非静态成员内部类不能通过实现本接口达到载入的目的.
 *               非静态成员内部类在编译之后,会自动被编译器加上一个带有一个参数的构造函数,参数是外部类的引用.
 *               这样就没有无参构造器,具体请看Java的成员内部类的编译字节码.
 * </pre>
 *
 * <pre>
 *     默认值:
 *         8种基础类型和String的默认值是可以进行设定的
 *         8种基础类型的数组和String数组的默认值也可以指定
 *
 *         设置默认值主要是应对重载和加载的时候的一些特殊情况.
 *            加载的时候,声明一个变量,认为这个变量文件配置了使用文件配置的.没配置就是用注解指定的默认值.
 *            重载的时候,如果某变量上次加载,是有过值的.这次重载,并没有载入到值(节点被删除了).这种时候不应当使用上次的值.应当恢复默认值.
 *
 *         对于删除节点的情况
 *            对象数组的默认值.是该类型的0长度数组.(节点全被删除)
 *            对象Collection的是一个0长度的Collection(节点全被删除)
 *            对象Map的默认值是0长度的Map(节点全被删除)
 *            对象.默认值是null.(节点被删除)
 *            枚举,如果找不到节点内容.就使用默认值的name设置枚举,如果没有指定默认值,为null
 * </pre>
 *
 * @author mayer - [Created on 2018-08-26 01:14]
 */
public interface XmlObject {

    /**
     * 判断一个属性是否数据绑定
     *
     * @param field 属性
     * @return true 含有, false 没有
     */
    static boolean isXmlDatabindAnnotationPresent(Field field) {
        return field.isAnnotationPresent(XmlAttribute.class)
                || field.isAnnotationPresent(XmlElement.class)
                || field.isAnnotationPresent(XmlElementArray.class)
                || field.isAnnotationPresent(XmlAttributeArray.class)
                || field.isAnnotationPresent(XmlElementDeclineMap.class)
                || field.isAnnotationPresent(XmlElementCollection.class)
                || field.isAnnotationPresent(XmlElementExtractMap.class);
    }

    /**
     * 判断一个属性是否是可忽略的
     *
     * @param field 属性
     * @return true 可以忽略, false 不能忽略
     */
    static boolean isXmlIgnorable(Field field) {
        return field.isAnnotationPresent(XmlIgnorable.class);
    }

    /**
     * 一个文件直接对应一个对象,就是root节点转为对象
     *
     * @param file xml文件
     */
    default void load(final File file) {
        try {
            this.load(XmlUtil.loadDocumentElement(file));
        } catch (Exception exception) {
            final Throwable throwable = ExceptionUtil.getSuperCause(exception);
            throw new RuntimeException(String.format("[file:%s] -> %s", FilenameUtils.normalize(file.getAbsolutePath()), exception.getMessage()), throwable);
        }
    }

    /**
     * 将一个Xml节点转为本对象
     *
     * @param element 节点
     */
    default void load(final Element element) {
        this.load(element, FieldUtil.getAllFieldsIncludeStatic(this.getClass()));
        this.handleSpeField(element);
    }

    /**
     * 将Xml节点的内容转换为成员变量的值.
     *
     * @param element 节点
     * @param fields  成员变量
     */
    default void load(final Element element, List<Field> fields) {
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XmlElement.class)) {
                final XmlElement annotation = field.getAnnotation(XmlElement.class);
                ParseXml.parseXmlElement(this, field, annotation, element);
            } else if (field.isAnnotationPresent(XmlElementArray.class)) {
                final XmlElementArray annotation = field.getAnnotation(XmlElementArray.class);
                ParseXml.parseXmlElementArray(this, field, annotation, element);
            } else if (field.isAnnotationPresent(XmlElementCollection.class)) {
                final XmlElementCollection annotation = field.getAnnotation(XmlElementCollection.class);
                ParseXml.parseXmlElementCollection(this, field, annotation, element);
            } else if (field.isAnnotationPresent(XmlElementDeclineMap.class)) {
                final XmlElementDeclineMap annotation = field.getAnnotation(XmlElementDeclineMap.class);
                ParseXml.parseXmlElementDeclineMap(this, field, annotation, element);
            } else if (field.isAnnotationPresent(XmlElementExtractMap.class)) {
                final XmlElementExtractMap annotation = field.getAnnotation(XmlElementExtractMap.class);
                ParseXml.parseXmlElementExtractMap(this, field, annotation, element);
            } else if (field.isAnnotationPresent(XmlAttribute.class)) {
                final XmlAttribute annotation = field.getAnnotation(XmlAttribute.class);
                ParseXml.parseXmlAttribute(this, field, annotation, element);
            } else if (field.isAnnotationPresent(XmlAttributeArray.class)) {
                final XmlAttributeArray annotation = field.getAnnotation(XmlAttributeArray.class);
                ParseXml.parseXmlAttributeArray(this, field, annotation, element);
            }
        }
    }

    /**
     * 这个方法是提供给子类实现，用来扩展特殊处理的。可用于加载某些特殊节点或对数据执行某些验证。这里什么也不做，但是可以在子类中定制。
     * 注意:为了正确地嵌套多个重写，子类通常应该在这个方法的末尾调用super.handleSpeField()
     * <p> may be used to loads some special nodes or performs some validation on the data.
     * <p> This implementation does nothing, but may be customized in subclasses.
     * <p> Note: To properly nest multiple overridings, subclasses should generally invoke super.handleSpeField at the end of this method.
     *
     * @param element xml element
     */
    default void handleSpeField(final Element element) {
    }

}
