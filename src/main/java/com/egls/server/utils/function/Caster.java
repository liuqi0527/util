package com.egls.server.utils.function;

/**
 * 转换器
 *
 * @author mayer - [Created on 2018-08-09 21:17]
 */
@FunctionalInterface
public interface Caster<S, T> {

    /**
     * 转换方法
     *
     * @param s 源对象
     * @return 目标对象
     */
    T cast(final S s);

}
