package com.egls.server.utils.function;

/**
 * @author mayer - [Created on 2018-08-20 20:04]
 */
@FunctionalInterface
public interface Operation {

    /**
     * 一个无参的操作.用作{@link java.util.function}的补充.
     */
    void operate();

}