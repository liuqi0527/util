package com.egls.server.utils.function;

/**
 * 本接口描述的也是一个可执行的对象.
 * <p>
 * 与{@link Runnable}所不同的地方,{@link Runnable}更多的描述含义是一个运行1次的对象.
 * <p>
 * 而本接口,具有多次被call的性质.{@link #tick()}方法是被外层循环多次调用的
 *
 * @author mayer - [Created on 2018-08-20 18:19]
 */
@FunctionalInterface
public interface Ticker {

    /**
     * 心跳方法
     */
    void tick();

    /**
     * 获取心跳对象的名称,具有一定的标识作用.
     *
     * @return 名称
     */
    default String getTickerName() {
        return this.getClass().getName();
    }

}
