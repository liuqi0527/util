package com.egls.server.utils.task.period;

import com.egls.server.utils.date.DateTimeUnit;

/**
 * 一种抽象的周期任务,此接口抽象出来周期控制的属性
 *
 * @author mayer - [Created on 2018-08-20 18:27]
 */
public interface PeriodicTask {

    /**
     * 具体的执行逻辑
     */
    void doPeriodicTask();

    /**
     * 以什么样的时间单位进行计时
     *
     * @return 时间单位
     */
    DateTimeUnit getDateTimeUnit();

    /**
     * 获取运行间隔,默认是1
     *
     * @return 运行间隔
     */
    default long getInterval() {
        return 1L;
    }

    /**
     * 是否可以被销毁.在每次运行之后,都会判断此方法.来确定任务是否可以被销毁.
     *
     * @return true 销毁 . false 不销毁.
     */
    default boolean isDestroyable() {
        return false;
    }

    /**
     * 获取任务的名称,具有一定的标识作用.
     *
     * @return 名称
     */
    default String getPeriodicTaskName() {
        return this.getClass().getName();
    }

}
