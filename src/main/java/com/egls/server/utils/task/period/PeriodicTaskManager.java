package com.egls.server.utils.task.period;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

import com.egls.server.utils.date.DateTimeUnit;
import com.egls.server.utils.function.Ticker;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现{@link PeriodicTask}的运行和生命周期管理.
 * 方法 {@link #tick()} 的调用间隔必须是毫秒级.
 * <p>
 * 本类是线程安全的.
 *
 * @author mayer - [Created on 2018-08-20 20:22]
 */
public final class PeriodicTaskManager implements Ticker {

    private static final class TaskAndCounter {

        private long count = 0;

        private final PeriodicTask periodicTask;

        private TaskAndCounter(final PeriodicTask periodicTask) {
            this.periodicTask = periodicTask;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodicTaskManager.class);

    private final Object doLock = new Object();

    private final Object putLock = new Object();

    private final List<TaskAndCounter> tasks = new ArrayList<>();

    private final List<PeriodicTask> pendingTasks = new ArrayList<>();

    private volatile long currentSecond, currentMinute, currentHour, currentDay, currentWeek, currentMonth, currentYear;
    private volatile long lastSecond, lastMinute, lastHour, lastDay, lastWeek, lastMonth, lastYear;

    /**
     * 是否丢弃出现错误的任务, true 丢弃, false 不丢弃
     */
    private final boolean discardErrorTask;

    private final long warningElapsedMilliseconds;

    public PeriodicTaskManager(final boolean discardErrorTask, final long warningElapsedMilliseconds) {
        this.discardErrorTask = discardErrorTask;
        this.warningElapsedMilliseconds = warningElapsedMilliseconds;
        this.catchCurrentInstant();
        this.pastCurrentInstant();
    }

    private void catchCurrentInstant() {
        LocalDateTime now = LocalDateTime.now();
        currentSecond = now.getSecond();
        currentMinute = now.getMinute();
        currentHour = now.getHour();
        currentDay = now.getDayOfMonth();
        currentWeek = now.get(WeekFields.ISO.weekOfWeekBasedYear());
        currentMonth = now.getMonthValue();
        currentYear = now.getYear();
    }

    private void pastCurrentInstant() {
        lastSecond = currentSecond;
        lastMinute = currentMinute;
        lastHour = currentHour;
        lastDay = currentDay;
        lastWeek = currentWeek;
        lastMonth = currentMonth;
        lastYear = currentYear;
    }

    private boolean isPast(final DateTimeUnit dateTimeUnit) {
        switch (dateTimeUnit) {
            case SECOND:
                //这里要注意, 如果卡住1分钟, 1分3秒和2分3秒,秒是同一个,但是实际上不是同一秒.依次类推
                return currentSecond != lastSecond || currentMinute != lastMinute || currentHour != lastHour || currentDay != lastDay || currentWeek != lastWeek;
            case MINUTE:
                return currentMinute != lastMinute || currentHour != lastHour || currentDay != lastDay || currentWeek != lastWeek;
            case HOUR:
                return currentHour != lastHour || currentDay != lastDay || currentWeek != lastWeek;
            case DAY:
                return currentDay != lastDay || currentWeek != lastWeek;
            case WEEK:
                //周要特别注意,周是可能出现不在同一个月,或者不在同一年的.所以最长就处理到周就可以了.
                //如果出现卡了一周,那只能说明使用者太笨了.
                return currentWeek != lastWeek;
            case MONTH:
                return currentMonth != lastMonth;
            case YEAR:
                return currentYear != lastYear;
            default:
                return false;
        }
    }

    private boolean execute(final TaskAndCounter taskAndCounter) {
        boolean remove = false;

        final long milliseconds = System.currentTimeMillis();

        final PeriodicTask periodicTask = taskAndCounter.periodicTask;
        if (isPast(periodicTask.getDateTimeUnit())) {
            if (++taskAndCounter.count >= periodicTask.getInterval()) {
                taskAndCounter.count = 0;
                periodicTask.doPeriodicTask();
                remove = periodicTask.isDestroyable();
            }
        }

        final long cost = System.currentTimeMillis() - milliseconds;
        if (cost >= warningElapsedMilliseconds) {
            LOGGER.warn(String.format("PeriodicTask %s elapsed %d millis", periodicTask.getPeriodicTaskName(), cost));
        }

        return remove;
    }

    @Override
    public final void tick() {
        synchronized (doLock) {

            catchCurrentInstant();
            try {
                final Iterator<TaskAndCounter> iterator = tasks.iterator();
                while (iterator.hasNext()) {
                    boolean remove = false;
                    final TaskAndCounter taskAndCounter = iterator.next();
                    try {
                        remove = execute(taskAndCounter);
                    } catch (Exception exception) {
                        remove = discardErrorTask;
                        LOGGER.error("PeriodicTaskWorker execute error", exception);
                    } finally {
                        if (remove) {
                            iterator.remove();
                        }
                    }
                }
            } finally {
                pastCurrentInstant();
            }

        }

        //脱离doLock域
        addPendingTask();
    }

    private void addPendingTask() {
        List<PeriodicTask> list = Collections.emptyList();
        synchronized (putLock) {
            if (!pendingTasks.isEmpty()) {
                list = Lists.newArrayList(pendingTasks);
                pendingTasks.clear();
            }
        }

        synchronized (doLock) {
            if (!list.isEmpty()) {
                for (PeriodicTask periodicTask : list) {
                    if (tasks.stream().noneMatch(taskAndCounter -> Objects.equals(taskAndCounter.periodicTask, periodicTask))) {
                        tasks.add(new TaskAndCounter(periodicTask));
                    }
                }
            }
        }
    }

    public final void addTask(final PeriodicTask periodicTask) {
        if (periodicTask.getInterval() <= 0) {
            throw new IllegalArgumentException("interval must be positive");
        }
        if (periodicTask.getDateTimeUnit() == null) {
            throw new IllegalArgumentException("getDateTimeUnit() can't return null");
        }
        synchronized (putLock) {
            pendingTasks.add(periodicTask);
        }
    }

}
