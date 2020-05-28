package com.egls.server.utils.structure;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import com.egls.server.utils.StringUtil;
import com.egls.server.utils.math.MathUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 运行时间记录对象.
 * 提供最大,最小,总运行时间,平均运行时间,运行次数.
 * 时间的单位在这里没有定义,使用者应该明白自己的单位.
 *
 * @author mayer - [Created on 2018-08-21 17:00]
 */
public final class ElapsedTimeRecorder implements Comparable<ElapsedTimeRecorder> {

    public static String toShowString(String title, Collection<ElapsedTimeRecorder> elapsedRecorders) {
        int maxNameLength = 1;
        long maxNumber = 1;

        for (ElapsedTimeRecorder elapsedRecorder : elapsedRecorders) {
            maxNameLength = MathUtil.max(maxNameLength, elapsedRecorder.getName().length());
            maxNumber = MathUtil.max(
                    maxNumber,
                    elapsedRecorder.totalElapsedCount.get(),
                    elapsedRecorder.totalElapsedTime.get(),
                    elapsedRecorder.minElapsedTime.get(),
                    elapsedRecorder.maxElapsedTime.get(),
                    elapsedRecorder.avgElapsedTime.get()
            );
        }

        int maxNumberLength = String.valueOf(maxNumber).length();

        int borderLength = title.length();

        final SortedList<ElapsedTimeRecorder> recorderSortedList = new SortedList<>(elapsedRecorders);
        StringBuilder stringBuilder = new StringBuilder();
        for (ElapsedTimeRecorder elapsedRecorder : recorderSortedList) {
            String showString = elapsedRecorder.toShowString(maxNameLength, maxNumberLength);
            if (borderLength < showString.length()) {
                borderLength = showString.length();
            }
            stringBuilder.append(showString).append(StringUtil.getLineSeparator());
        }

        String borderHead = StringUtils.center(title, borderLength, '↓') + StringUtil.getLineSeparator();
        String borderTail = StringUtils.center(title, borderLength, '↑') + StringUtil.getLineSeparator();
        return borderHead + stringBuilder.toString() + borderTail;
    }

    private final String name;

    private final AtomicLong minElapsedTime = new AtomicLong(Long.MAX_VALUE);

    private final AtomicLong maxElapsedTime = new AtomicLong(Long.MIN_VALUE);

    private final AtomicLong avgElapsedTime = new AtomicLong();

    private final AtomicLong totalElapsedTime = new AtomicLong();

    private final AtomicLong totalElapsedCount = new AtomicLong();

    public ElapsedTimeRecorder(final String name) {
        this.name = name;
    }

    @Override
    public int compareTo(final ElapsedTimeRecorder o) {
        //大的在前面
        return Long.compare(o.avgElapsedTime.get(), avgElapsedTime.get());
    }

    public String getName() {
        return name;
    }

    public AtomicLong getMinElapsedTime() {
        return minElapsedTime;
    }

    public AtomicLong getMaxElapsedTime() {
        return maxElapsedTime;
    }

    public AtomicLong getAvgElapsedTime() {
        return avgElapsedTime;
    }

    public AtomicLong getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public AtomicLong getTotalElapsedCount() {
        return totalElapsedCount;
    }

    public void reset() {
        minElapsedTime.getAndSet(Long.MAX_VALUE);
        maxElapsedTime.getAndSet(Long.MIN_VALUE);
        avgElapsedTime.getAndSet(0);
        totalElapsedTime.getAndSet(0);
        totalElapsedCount.getAndSet(0);
    }

    public void addElapsedTime(final long increment) {
        if (increment > 0) {
            minElapsedTime.accumulateAndGet(increment, (oldValue, newValue) -> NumberUtils.min(oldValue, newValue));
            maxElapsedTime.accumulateAndGet(increment, (oldValue, newValue) -> NumberUtils.max(oldValue, newValue));
            final long time = totalElapsedTime.addAndGet(increment);
            final long count = totalElapsedCount.incrementAndGet();
            avgElapsedTime.getAndSet(time / count);
        }
    }

    public String toShowString(final int stringLength, final int numberLength) {
        final char padChar = ' ';
        final char delimiter = '|';
        String showString = "";
        showString = showString + padChar + StringUtils.rightPad(name, stringLength, padChar) + padChar + delimiter;
        showString = showString + padChar + StringUtils.rightPad(String.valueOf(totalElapsedCount.get()), numberLength, padChar) + padChar + delimiter;
        showString = showString + padChar + StringUtils.rightPad(String.valueOf(totalElapsedTime.get()), numberLength, padChar) + padChar + delimiter;
        showString = showString + padChar + StringUtils.rightPad(String.valueOf(minElapsedTime.get()), numberLength, padChar) + padChar + delimiter;
        showString = showString + padChar + StringUtils.rightPad(String.valueOf(maxElapsedTime.get()), numberLength, padChar) + padChar + delimiter;
        showString = showString + padChar + StringUtils.rightPad(String.valueOf(avgElapsedTime.get()), numberLength, padChar) + padChar + delimiter;
        return showString;
    }

}
