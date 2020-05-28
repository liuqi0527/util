package com.egls.server.utils.date;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.egls.server.utils.NumberUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * 提供一些操作日期的工具方法
 *
 * @author mayer - [Created on 2018-08-20 16:53]
 */
public final class DateTimeUtil {

    /**
     * 格林威治时区
     */
    public static final ZoneId GMT = ZoneId.of("GMT");

    /**
     * 将纳秒转成毫秒
     *
     * @param nanoseconds 纳秒
     * @return 毫秒
     */
    public static double computeNanosToMillis(final long nanoseconds) {
        final double nanosecondsPerMilliseconds = ChronoUnit.MILLIS.getDuration().toNanos();
        double milliseconds = nanoseconds / nanosecondsPerMilliseconds;
        //保留3位小数
        return NumberUtil.convertPrecision(milliseconds, 3);
    }

    public static ZonedDateTime toZonedDateTime(final LocalDateTime localDateTime, final ZoneId zoneId) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId);
    }

    public static ZonedDateTime toZonedDateTime(final ZoneId zoneId) {
        return toZonedDateTime(LocalDateTime.now(), zoneId);
    }

    public static ZonedDateTime toSystemZonedDateTime(final LocalDateTime localDateTime) {
        return toZonedDateTime(localDateTime, ZoneId.systemDefault());
    }

    public static ZonedDateTime getSystemZonedDateTime() {
        return toSystemZonedDateTime(LocalDateTime.now());
    }

    /**
     * 将给定的当地时间转换为给定时区的当地时间
     */
    public static LocalDateTime toLocalDateTimeWithZone(final LocalDateTime localDateTime, final ZoneId zoneId) {
        return toZonedDateTime(localDateTime, zoneId).toLocalDateTime();
    }

    /**
     * 将系统的当地时间转换为另外一个时区的当地时间
     */
    public static LocalDateTime toLocalDateTimeWithZone(final ZoneId zoneId) {
        return toLocalDateTimeWithZone(LocalDateTime.now(), zoneId);
    }

    public static long toMillis(final ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static long toMillis(final LocalDateTime localDateTime) {
        return toMillis(toSystemZonedDateTime(localDateTime));
    }

    public static Date toUtilDate(final LocalDateTime localDateTime) {
        return new Date(toMillis(localDateTime));
    }

    public static LocalDateTime toLocalDateTime(final Instant instant, final ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDateTime toLocalDateTime(final Instant instant) {
        return toLocalDateTime(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(final long millis, final ZoneId zoneId) {
        return toLocalDateTime(Instant.ofEpochMilli(millis), zoneId);
    }

    public static LocalDateTime toLocalDateTime(final long millis) {
        return toLocalDateTime(millis, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(final Date date, final ZoneId zoneId) {
        return toLocalDateTime(date.getTime(), zoneId);
    }

    public static LocalDateTime toLocalDateTime(final Date date) {
        return toLocalDateTime(date.getTime(), ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(final ZonedDateTime zonedDateTime) {
        return toLocalDateTime(zonedDateTime.toInstant());
    }

    /**
     * 使用一个统一的时间线,来计算两个本地时间的差值
     */
    public static Duration minusWithZone(final ZoneId zoneId, final LocalDateTime startInclusive, final LocalDateTime endExclusive) {
        return Duration.between(toZonedDateTime(startInclusive, zoneId), toZonedDateTime(endExclusive, zoneId));
        //return Duration.ofMillis(one.atZone(zoneId).toInstant().toEpochMilli() - another.atZone(zoneId).toInstant().toEpochMilli());
    }

    /**
     * <pre>
     *      计算两个本地时间的差值长度,使用的是系统默认时间线.
     *      在夏令时和冬令时也不会出现问题.
     *      因为使用的是统一的当前系统默认时间线.在夏令时和冬令时的时候,相应的时间线也会改变.
     *      也就是使用冬令时的时间与夏令时的时间计算差值的时候,由于此时两个时间不在同一个时间线上.所以出现问题.
     *      这里统一了时间线,所以不会出现问题.
     *      表示的是逻辑上的时间.实际上夏令时和冬令时是人为的切换一下时区.
     *      时间线上的时间是固定的,并不是增加或减少了时间.
     * </pre>
     */
    public static Duration minusWithSystemZone(final LocalDateTime startInclusive, final LocalDateTime endExclusive) {
        return Duration.between(startInclusive, endExclusive);
        //return minusWithZone(ZonedDateTime.now().getOffset(), startInclusive, endExclusive);
    }

    public static Duration minusWithSystemZone(final Date startInclusive, final Date endExclusive) {
        return minusWithSystemZone(toLocalDateTime(startInclusive), toLocalDateTime(endExclusive));
    }

    public static Duration minusWithSystemZone(final long startInclusive, final long endExclusive) {
        return minusWithSystemZone(toLocalDateTime(startInclusive), toLocalDateTime(endExclusive));
    }

    /**
     * <pre>
     *      格式化一个时间长度对象, HH:mm:ss
     * </pre>
     */
    public static String durationToString(final Duration duration) {
        //HH:mm:ss
        long hour = duration.toHours();
        long minute = duration.toMinutes() % 60;
        long second = (duration.toMillis() / 1000) % 60;

        final String[] parts;
        if (hour > 0) {
            parts = new String[]{
                    StringUtils.leftPad(String.valueOf(hour), 2, '0'),
                    StringUtils.leftPad(String.valueOf(minute), 2, '0'),
                    StringUtils.leftPad(String.valueOf(second), 2, '0')
            };
        } else {
            parts = new String[]{
                    StringUtils.leftPad(String.valueOf(minute), 2, '0'),
                    StringUtils.leftPad(String.valueOf(second), 2, '0')
            };
        }
        return StringUtils.join(parts, ':');
    }

    public static String durationToString(final long milliseconds) {
        return durationToString(Duration.ofMillis(milliseconds));
    }

    /**
     * @see DateTimeUnit#equals(LocalDateTime, LocalDateTime)
     */
    public static boolean equals(final DateTimeUnit dateTimeUnit, final LocalDateTime one, final LocalDateTime another) {
        return dateTimeUnit.equals(one, another);
    }

    public static boolean equals(final DateTimeUnit dateTimeUnit, final Date one, final Date another) {
        return dateTimeUnit.equals(toLocalDateTime(one), toLocalDateTime(another));
    }

    public static boolean equals(final DateTimeUnit dateTimeUnit, final long one, final long another) {
        return dateTimeUnit.equals(toLocalDateTime(one), toLocalDateTime(another));
    }

    /**
     * @see DateTimeUnit#adjust(LocalDateTime, boolean, int)
     */
    public static LocalDateTime adjust(final DateTimeUnit dateTimeUnit, final LocalDateTime localDateTime, final boolean startWithZero, final int amount) {
        return dateTimeUnit.adjust(localDateTime, startWithZero, amount);
    }

    public static long adjust(final DateTimeUnit dateTimeUnit, final long milli, final boolean startWithZero, final int amount) {
        return toMillis(dateTimeUnit.adjust(toLocalDateTime(milli), startWithZero, amount));
    }

    public static Date adjust(final DateTimeUnit dateTimeUnit, final Date date, final boolean startWithZero, final int amount) {
        return toUtilDate(dateTimeUnit.adjust(toLocalDateTime(date), startWithZero, amount));
    }

}
