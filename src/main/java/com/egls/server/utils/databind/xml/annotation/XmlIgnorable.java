package com.egls.server.utils.databind.xml.annotation;

import java.lang.annotation.*;

/**
 * 这是一个标记, 出现在某些可选数据上面.
 *
 * @author mayer - [Created on 2018-08-26 01:30]
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlIgnorable {
}
