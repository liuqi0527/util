package com.egls.server.utils.task.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.egls.server.utils.function.Ticker;
import com.egls.server.utils.structure.SortedList;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现{@link ScheduledTask}的运行和生命周期管理.
 * 方法 {@link #tick()} 的调用间隔必须是毫秒级.
 * <p>
 * 本类是线程安全的.
 *
 * @author mayer - [Created on 2018-09-29 14:58]
 */
public class ScheduledTaskManager implements Ticker {

    /**
     * 对调度任务进行一下包装,方便控制
     */
    private static final class ScheduledTaskWrapper implements ScheduledTask, Comparable<ScheduledTaskWrapper> {

        private final long executeTime;

        private final ScheduledTask scheduledTask;

        private ScheduledTaskWrapper(long delayMills, ScheduledTask scheduledTask) {
            this.executeTime = System.currentTimeMillis() + delayMills;
            this.scheduledTask = scheduledTask;
        }

        @Override
        public int compareTo(ScheduledTaskWrapper o) {
            return Long.compare(executeTime, o.executeTime);
        }

        @Override
        public void execute() {
            this.scheduledTask.execute();
        }

        @Override
        public String getScheduledTaskName() {
            return this.scheduledTask.getScheduledTaskName();
        }

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskManager.class);

    private final Object doLock = new Object();

    private final Object putLock = new Object();

    private final SortedList<ScheduledTaskWrapper> tasks = new SortedList<>();

    private final List<ScheduledTaskWrapper> pendingTasks = new ArrayList<>();

    private final long warningElapsedMilliseconds;

    public ScheduledTaskManager(long warningElapsedMilliseconds) {
        this.warningElapsedMilliseconds = warningElapsedMilliseconds;
    }

    @Override
    public void tick() {
        synchronized (doLock) {

            for (Iterator<ScheduledTaskWrapper> itr = tasks.iterator(); itr.hasNext(); ) {
                ScheduledTaskWrapper scheduledTaskWrapper = itr.next();
                if (System.currentTimeMillis() >= scheduledTaskWrapper.executeTime) {
                    try {
                        final long milliseconds = System.currentTimeMillis();
                        scheduledTaskWrapper.execute();
                        final long cost = System.currentTimeMillis() - milliseconds;
                        if (cost >= warningElapsedMilliseconds) {
                            LOGGER.warn(String.format("ScheduledTask %s elapsed %d millis", scheduledTaskWrapper.getScheduledTaskName(), cost));
                        }
                    } catch (Exception e) {
                        LOGGER.error("Exception in ScheduledTask " + scheduledTaskWrapper.getScheduledTaskName(), e);
                    } finally {
                        itr.remove();
                    }
                } else {
                    break;
                }
            }

        }

        //脱离doLock域
        addPendingTask();
    }

    private void addPendingTask() {
        List<ScheduledTaskWrapper> list = Collections.emptyList();
        synchronized (putLock) {
            if (!pendingTasks.isEmpty()) {
                list = Lists.newArrayList(pendingTasks);
                pendingTasks.clear();
            }
        }

        synchronized (doLock) {
            if (!list.isEmpty()) {
                for (ScheduledTaskWrapper scheduledTaskWrapper : list) {
                    //因为任务只运行1次,所以不像周期任务一样进行去重了.
                    tasks.add(scheduledTaskWrapper);
                }
            }
        }
    }

    public void schedule(final long delayMills, final ScheduledTask scheduledTask) {
        synchronized (putLock) {
            pendingTasks.add(new ScheduledTaskWrapper(delayMills, scheduledTask));
        }
    }

    public void schedule(final ScheduledTask scheduledTask) {
        schedule(0L, scheduledTask);
    }

}
