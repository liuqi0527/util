package com.egls.server.utils.task.schedule;

/**
 * @author mayer - [Created on 2018-09-29 14:58]
 */
@FunctionalInterface
public interface ScheduledTask {

    /**
     * 任务的具体逻辑
     */
    void execute();

    /**
     * 获取任务的名称,具有一定的标识作用.
     *
     * @return 名称
     */
    default String getScheduledTaskName() {
        return this.getClass().getName();
    }

}
